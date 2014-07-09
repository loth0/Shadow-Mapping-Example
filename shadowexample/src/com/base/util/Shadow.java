package com.base.util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Shadow {

	private static FloatBuffer matrix44Buffer;
	private static MatrixHandler depthMatrix;
	private static int sto;
	private static int sbo;

	public Shadow() {

	}

	public static void setUp() {
		matrix44Buffer = BufferTools.reserveFloatData(16);
		depthMatrix = new MatrixHandler();
		// create texture
		sbo = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, sbo);

		sto = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, sto);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT16, 1024, 1024, 0, GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_FUNC, GL_LEQUAL);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_MODE, GL_COMPARE_R_TO_TEXTURE);

		glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, sto, 0);
		glDrawBuffer(GL_NONE);

		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
			System.err.println("Error setting up shadowbuffer");
		}
		//glEnable(GL_POLYGON_OFFSET_FILL);
		//glPolygonOffset(1f, 1f);
	}

	public static void updateShadowBias(int shaderProgram, int depthBiasMatrixID, int shadowMapID, int modelMatrixID) {
		MatrixHandler biasMatrix = new MatrixHandler();
		MatrixHandler depthBiasMatrix = new MatrixHandler();
		biasMatrix.setBias();
		Matrix4f.mul(biasMatrix, depthMatrix, depthBiasMatrix);

		glUseProgram(shaderProgram);

		depthBiasMatrix.store(matrix44Buffer);
		matrix44Buffer.flip();
		glUniformMatrix4(depthBiasMatrixID, false, matrix44Buffer);

		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, sto);
		glUniform1i(shadowMapID, 0);

		glUseProgram(0);
	}

	public static void updateShadowBuffer(int shadowShaderProgram, int depthMatrixID, Vector3f lightPosition) {
		glBindFramebuffer(GL_FRAMEBUFFER, sbo);
		glViewport(0, 0, 1024, 1024);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		MatrixHandler depthProjectionMatrix = new MatrixHandler();
		MatrixHandler depthViewMatrix = new MatrixHandler();
		
		Vector3f normLightPosition = new Vector3f();
		lightPosition.normalise(normLightPosition);
		
		depthProjectionMatrix.initOrthographicMatrix(-20, 20, -20, 20, -20, 40);
		depthViewMatrix.lookAt(normLightPosition, new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));
		
		Matrix4f.mul(depthProjectionMatrix, depthViewMatrix, depthMatrix);
		
		glUseProgram(shadowShaderProgram);
		depthMatrix.store(matrix44Buffer);
		matrix44Buffer.flip();
		glUniformMatrix4(depthMatrixID, false, matrix44Buffer);
		glUseProgram(0);
	}

	public static void culling(int culling) {
		glCullFace(culling);
	}
	
	public static void polyOffset() {
		glDisable(GL_POLYGON_OFFSET_FILL);
	}
}
