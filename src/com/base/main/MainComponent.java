package com.base.main;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import com.base.objects.*;
import com.base.util.*;

/***
 * Here is an example of shadow mapping with LWJGL and shaders.
 * I'm an openGL beginner and the code probably isn't ther best written
 * but it works and should be used as a guideline.
 * 
 * Notice that the light is a point light and the shadows are made with
 * orthogonal projection (used for directional light) you might notice that
 * the shadow DOES NOT CORRESPOND TO A POINT LIGHT!
 * 
 * 
 * 
 *
 * @author Johan
 *
 */

public class MainComponent {

	private static Core core;

	private static int projectionMatrixID;
	private static int viewMatrixID;
	private static int modelMatrixID;
	private static int depthMatrixID;
	private static int depthModelMatrixID;
	private static int depthBiasMatrixID;
	private static int shadowMapID;
	
	private static int lightPositionID;
	private static int ambientID;
	private static int lightRangeID;
	private static int attenConstantID;
	private static int attenLinearID;
	private static int attenExponentID;
	
	private static boolean updateCamera;
	private static Camera camera;
	private static House house;
	private static Light light;
	private static ArrayList<ObjectHandler> objects = new ArrayList<ObjectHandler>();
	private static ObjectHandler groundObject;
	private static ObjectHandler houseObject;
	private static ObjectHandler treeObject;
	private static ObjectHandler sphereObject;

	private static int shaderProgram;
	private static int shadowShaderProgram;

	public static void main(String[] args) {
		core = new Core(800, 600, 60);
		core.createWindow("Shadow example");
		core.start();
	}

	public static void setUp() {
		//initiate openGL and shaders
		setUpStates();
		setUpBasicShaders();
		setUpShadowShaders();
		//initiate objects
		Ground ground = new Ground();
		ground.update();
		groundObject = new ObjectHandler(ground.getVertices(), ground.getIndices(), true);
		objects.add(groundObject);
		
		Sphere sphere = new Sphere();
		sphere.setColor(new Vector3f(0.2f, 0, 1));
		sphere.update();
		sphereObject = new ObjectHandler(sphere.getVertices(), sphere.getIndices(), true);
		sphereObject.setPosition(new Vector3f(-3, 5, 12));
		objects.add(sphereObject);
		//create light
		light = new Light(new Vector3f(-6, 10, 23), 1.2f, 65, 1f, 0.01f, 0.00001f);
		
		Tree tree = new Tree();
		tree.update();
		treeObject = new ObjectHandler(tree.getVertices(), tree.getIndices(), true);
		treeObject.setPosition(new Vector3f(0, 0, 10));
		objects.add(treeObject);
		
		house = new House();
		house.update();
		houseObject = new ObjectHandler(house.getVertices(), house.getIndices(), true);
		objects.add(houseObject);
		//set up shadows
		Shadow.setUp();
		
		for (ObjectHandler obj : objects) {
			obj.setUpShadowBuffer();;
		}
		//set up camera
		setUpCamera();
	}

	public static void render() {
		//
		Shadow.culling(GL_FRONT);
		Shadow.updateShadowBuffer(shadowShaderProgram, depthMatrixID, light.getPosition());
		
		for (ObjectHandler obj : objects) {
			obj.updateShadowBuffer(shadowShaderProgram, depthModelMatrixID);
		}
		
		Shadow.culling(GL_BACK);
		
		Window.bindAsRenderTarget();
		clearScreen();
		
		Shadow.updateShadowBias(shaderProgram, depthBiasMatrixID, shadowMapID, modelMatrixID);
		light.update(shaderProgram, lightPositionID, ambientID, lightRangeID, attenConstantID, attenLinearID, attenExponentID);
		
		for (ObjectHandler obj : objects) {
			obj.draw(shaderProgram, modelMatrixID);
		}
		if (updateCamera)
			camera.updateCamera();

		checkInput();
	}
	
	private static void setUpStates() {
		glClearColor(0.0f, 0.15f, 0.35f, 0.0f);

		glFrontFace(GL_CW);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);

		glEnable(GL_DEPTH_TEST);
		glEnable(GL_DEPTH_CLAMP);
		glEnable(GL_BLEND);

		glEnable(GL_TEXTURE_2D);
	}

	private static void setUpBasicShaders() {
		ArrayList<Integer> shaderList = new ArrayList<>();
		shaderList.add(Framework.loadShader(GL_VERTEX_SHADER, "basicShader.vs"));
		shaderList.add(Framework.loadShader(GL_FRAGMENT_SHADER, "basicShader.fs"));

		shaderProgram = Framework.createProgram(shaderList);

		projectionMatrixID = glGetUniformLocation(shaderProgram, "projectionMatrix");
		viewMatrixID = glGetUniformLocation(shaderProgram, "viewMatrix");
		modelMatrixID = glGetUniformLocation(shaderProgram, "modelMatrix");
		depthBiasMatrixID = glGetUniformLocation(shaderProgram, "depthBiasMatrix");
		shadowMapID = glGetUniformLocation(shaderProgram, "shadowMap");
		
		lightPositionID = glGetUniformLocation(shaderProgram, "lightPosition");
		ambientID = glGetUniformLocation(shaderProgram, "ambient");
		lightRangeID = glGetUniformLocation(shaderProgram, "lightRange");
		attenConstantID = glGetUniformLocation(shaderProgram, "attenConstant");
		attenLinearID = glGetUniformLocation(shaderProgram, "attenLinear");
		attenExponentID = glGetUniformLocation(shaderProgram, "attenExponent");
	}
	private static void setUpShadowShaders() {
		ArrayList<Integer> shaderList = new ArrayList<>();
		shaderList.add(Framework.loadShader(GL_VERTEX_SHADER, "shadowShader.vs"));
		shaderList.add(Framework.loadShader(GL_FRAGMENT_SHADER, "shadowShader.fs"));

		shadowShaderProgram = Framework.createProgram(shaderList);
		
		depthMatrixID = glGetUniformLocation(shadowShaderProgram, "depthMatrix");
		depthModelMatrixID = glGetUniformLocation(shadowShaderProgram, "depthModelMatrix");
	}
	
	private static void setUpCamera() {
		
		camera = new Camera(projectionMatrixID, viewMatrixID, shaderProgram);
		camera.setPerspectiveProjection(60.0f, (float)Window.getWidth() / (float)Window.getHeight(), 0.01f, 100f);
		
		camera.setPosition(new Vector3f(-30, 18, 36));
		camera.setVerticalAngle(-23);
		camera.setHorizontalAngle(136);
		
		updateCamera = true;
	}

	private static void clearScreen() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	private static void checkInput() {
		if (Mouse.isGrabbed()) {
			camera.applyMouse(core.getDelta());
		}
		camera.applyKeyboard(core.getDelta());
		if (Mouse.isButtonDown(0)) {
			Mouse.setGrabbed(true);
		} else if (Mouse.isButtonDown(1)) {
			Mouse.setGrabbed(false);
		}
		while (Keyboard.next()) {
			if (Keyboard.getEventKey() == Keyboard.KEY_C && Keyboard.getEventKeyState()) {
				//print camerainfo
				System.out.println(camera.toString());
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_R && Keyboard.getEventKeyState()) {
				//Reset cameraposition
				camera.setPosition(new Vector3f(-11, 15, 19));
				camera.setVerticalAngle(-32);
				camera.setHorizontalAngle(150);
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_O && Keyboard.getEventKeyState()) {
				//Set orthogonal projection
				camera.setOrthogonalProjection(-20, 20, -20, 20, -20, 60);
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_P && Keyboard.getEventKeyState()) {
				//Set perspective projection
				camera.setPerspectiveProjection(60.0f, (float)Window.getWidth() / (float)Window.getHeight(), 0.01f, 100f);
				updateCamera = true;
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
				core.stop();
			}
		}
	}

	public static void cleanUp() {
		glDeleteShader(shaderProgram);
		glDeleteShader(shadowShaderProgram);
		glUseProgram(0);
	}
}
