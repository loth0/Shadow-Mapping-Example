package com.base.objects;

import org.lwjgl.util.vector.Vector3f;

import com.base.util.Vertex;

public class House {
	private float bottomHeight = 3.0f;
	private float topHeight = 5.0f;
	private float width = 3.0f;
	private float length = 10.0f;
	private double horizontalAngleDegree = 10.0;
	private double horizontalAngle = Math.toRadians(horizontalAngleDegree);
	private double verticalAngle = Math.asin((topHeight - bottomHeight) / width);
	private Vector3f[] points;
	private Vertex[] roofVertices;
	private Vertex[] vertices;
	private int[] roofIndices;
	private int[] indices;
	private Vector3f roofNormal;
	private Vector3f roofUp;
	private Vector3f roofCenter;

	public House(float bottomHeight, float topHeight, float width, float length, double horizontalAngleDegree) {
		this.bottomHeight = bottomHeight;
		this.topHeight = topHeight;
		this.width = width;
		this.length = length;
		this.horizontalAngle = Math.toRadians(horizontalAngleDegree);
		this.verticalAngle = Math.asin((topHeight - bottomHeight) / width);
		makePoints();
	}

	public House(float bottomHeight, float topHeight, float length, double horizontalAngleDegree, double verticalAngleDegree) {
		this.bottomHeight = bottomHeight;
		this.topHeight = topHeight;
		this.length = length;
		this.horizontalAngle = Math.toRadians(horizontalAngleDegree);
		this.verticalAngle = Math.toRadians(verticalAngleDegree);
		this.width = (float) ((topHeight - bottomHeight) / Math.sin(verticalAngle));
		makePoints();
	}

	public House() {
		
	}
	
	public void update() {
		makePoints();
		calcRoofVertInd();
		calcHouseVertInd();
	}

	public float getBottomHeight() {
		return bottomHeight;
	}

	public void setBottomHeight(float bottomHeight) {
		this.bottomHeight = bottomHeight;
	}

	public float getTopHeight() {
		return topHeight;
	}

	public void setTopHeight(float topHeight) {
		this.topHeight = topHeight;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public double getHorizontalAngleDegree() {
		return Math.toDegrees(horizontalAngleDegree);
	}

	public void setHorizontalAngleDegree(double horizontalAngleDegree) {
		this.horizontalAngleDegree = Math.toRadians(horizontalAngleDegree);
	}

	public double getHorizontalAngle() {
		return Math.toDegrees(horizontalAngle);
	}

	public void setHorizontalAngle(double horizontalAngle) {
		this.horizontalAngle = Math.toRadians(horizontalAngle);
	}

	public double getVerticalAngle() {
		return verticalAngle;
	}

	public void setVerticalAngle(double verticalAngle) {
		this.verticalAngle = Math.toRadians(verticalAngle);
	}

	public Vector3f getRoofNormal() {
		return roofNormal;
	}

	public Vector3f getRoofCenter() {
		return roofCenter;
	}

	public Vector3f getRoofUp() {
		return roofUp;
	}

	private void makePoints() {
		// Roof frontside
		float[] x = new float[10];
		float[] y = new float[10];
		float[] z = new float[10];
		
		points = new Vector3f[10];
		
		x[0] = (float) (-length * Math.cos(horizontalAngle) / 2.0);
		x[1] = (float) (length * Math.cos(horizontalAngle) / 2.0);
		x[2] = (float) (length * Math.cos(horizontalAngle) / 2.0 + width * Math.cos(verticalAngle) * Math.sin(horizontalAngle));
		x[3] = (float) (-length * Math.cos(horizontalAngle) / 2.0 + width * Math.cos(verticalAngle) * Math.sin(horizontalAngle) / 2.0);
		x[4] = x[2];
		x[5] = x[3];
		x[6] = x[0] - (x[3] - x[0]);
		x[7] = x[1] - (x[2] - x[1]);
		x[8] = x[6];
		x[9] = x[7];
		
		y[0] = topHeight;
		y[1] = topHeight;
		y[2] = bottomHeight;
		y[3] = bottomHeight;
		y[4] = 0;
		y[5] = 0;
		y[6] = bottomHeight;
		y[7] = bottomHeight;
		y[8] = 0;
		y[9] = 0;
		
		z[0] = (float) (length * Math.sin(horizontalAngle) / 2.0);
		z[1] = (float) (-length * Math.sin(horizontalAngle) / 2.0);
		z[2] = (float) (-length * Math.sin(horizontalAngle) / 2.0 + width * Math.cos(verticalAngle) * Math.cos(horizontalAngle));
		z[3] = (float) (length * Math.sin(horizontalAngle) / 2.0 + width * Math.cos(verticalAngle) * Math.cos(horizontalAngle));
		z[4] = z[2];
		z[5] = z[3];
		z[6] = z[0] - (z[3] - z[0]);
		z[7] = z[1] - (z[2] - z[1]);
		z[8] = z[6];
		z[9] = z[7];
		
		roofCenter = new Vector3f((x[2] + x[0]) / 2.0f, (y[2] + y[0]) / 2.0f, (z[2] + z[0]) / 2.0f);

		for (int i = 0; i < 10; i++) {
			points[i] = new Vector3f(x[i], y[i], z[i]);
		}

	}
	
	public void calcRoofVertInd() {
		Rectangle rect;
		int rectangles = 1;
		
		roofVertices = new Vertex[rectangles * 4];
		roofIndices = new int[rectangles * 6];
		
		Vector3f topLeft; 
		Vector3f topRight;
		Vector3f bottomRight;
		Vector3f bottomLeft;
		
		//Roof frontside
		topLeft 	= points[0]; 
		topRight 	= points[1];
		bottomRight = points[2];
		bottomLeft 	= points[3];
		
		rect = new Rectangle(topLeft, topRight, bottomRight, bottomLeft);
		rect.setIndices(0, 1, 2, 3);
		rect.setColor(new Vector3f(0.5f, 0.5f, 0.5f));
		rect.update();
		
		for (int i = 0; i < 6; i++) {
			if (i < 4)
				roofVertices[i] = rect.getVertices()[i];
			roofIndices[i] = rect.getIndices()[i];
		}
	}
	
	public Vertex[] getRoofVertices() {
		calcRoofVertInd();
		return roofVertices;
	}
	
	public int[] getRoofIndices() {
		return roofIndices;
	}

	public void calcHouseVertInd() {
		Rectangle rect;
		int rectangles = 8;
		int currentRectangle = 0;
		
		vertices = new Vertex[rectangles * 4];
		indices = new int[rectangles * 6];
		
		Vector3f topLeft; 
		Vector3f topRight;
		Vector3f bottomRight;
		Vector3f bottomLeft;
		
		//Roof frontside
		topLeft 	= points[0]; 
		topRight 	= points[1];
		bottomRight = points[2];
		bottomLeft 	= points[3];
		
		rect = new Rectangle(topLeft, topRight, bottomRight, bottomLeft);
		rect.setIndices(currentRectangle * 4, currentRectangle * 4 + 1, 
						currentRectangle * 4 + 2, currentRectangle * 4 + 3);
		rect.setColor(new Vector3f(0.5f, 0.5f, 0.5f));
		rect.update();
		
		for (int i = 0; i < 6; i++) {
			if (i < 4)
				vertices[i + currentRectangle * 4] = rect.getVertices()[i];
			indices[i + currentRectangle * 6] = rect.getIndices()[i];
		}
		currentRectangle ++;
		
		//Roof backside
		topLeft 	= points[1]; 
		topRight 	= points[0];
		bottomRight = points[6];
		bottomLeft 	= points[7];
		
		rect = new Rectangle(topLeft, topRight, bottomRight, bottomLeft);
		rect.setIndices(currentRectangle * 4, currentRectangle * 4 + 1, 
						currentRectangle * 4 + 2, currentRectangle * 4 + 3);
		rect.setColor(new Vector3f(0.5f, 0.5f, 0.5f));
		rect.update();
		
		for (int i = 0; i < 6; i++) {
			if (i < 4)
				vertices[i + currentRectangle * 4] = rect.getVertices()[i];
			indices[i + currentRectangle * 6] = rect.getIndices()[i];
		}
		currentRectangle ++;
		
		//Front wall
		topLeft 	= points[3]; 
		topRight 	= points[2];
		bottomRight = points[4];
		bottomLeft 	= points[5];
		
		rect = new Rectangle(topLeft, topRight, bottomRight, bottomLeft);
		rect.setIndices(currentRectangle * 4, currentRectangle * 4 + 1, 
						currentRectangle * 4 + 2, currentRectangle * 4 + 3);
		rect.setColor(new Vector3f(0.545f, 0.271f, 0.075f));
		rect.update();
		
		for (int i = 0; i < 6; i++) {
			if (i < 4)
				vertices[i + currentRectangle * 4] = rect.getVertices()[i];
			indices[i + currentRectangle * 6] = rect.getIndices()[i];
		}
		currentRectangle ++;
		
		//Back wall
		topLeft 	= points[7]; 
		topRight 	= points[6];
		bottomRight = points[8];
		bottomLeft 	= points[9];
		
		rect = new Rectangle(topLeft, topRight, bottomRight, bottomLeft);
		rect.setIndices(currentRectangle * 4, currentRectangle * 4 + 1, 
						currentRectangle * 4 + 2, currentRectangle * 4 + 3);
		rect.setColor(new Vector3f(0.545f, 0.271f, 0.075f));
		rect.update();
		
		for (int i = 0; i < 6; i++) {
			if (i < 4)
				vertices[i + currentRectangle * 4] = rect.getVertices()[i];
			indices[i + currentRectangle * 6] = rect.getIndices()[i];
		}
		currentRectangle ++;
		
		//Left wall
		topLeft 	= points[6]; 
		topRight 	= points[3];
		bottomRight = points[5];
		bottomLeft 	= points[8];
		
		rect = new Rectangle(topLeft, topRight, bottomRight, bottomLeft);
		rect.setIndices(currentRectangle * 4, currentRectangle * 4 + 1, 
						currentRectangle * 4 + 2, currentRectangle * 4 + 3);
		rect.setColor(new Vector3f(0.545f, 0.271f, 0.075f));
		rect.update();
		
		for (int i = 0; i < 6; i++) {
			if (i < 4)
				vertices[i + currentRectangle * 4] = rect.getVertices()[i];
			indices[i + currentRectangle * 6] = rect.getIndices()[i];
		}
		currentRectangle ++;
		
		//Right wall
		topLeft 	= points[2]; 
		topRight 	= points[7];
		bottomRight = points[9];
		bottomLeft 	= points[4];
		
		rect = new Rectangle(topLeft, topRight, bottomRight, bottomLeft);
		rect.setIndices(currentRectangle * 4, currentRectangle * 4 + 1, 
						currentRectangle * 4 + 2, currentRectangle * 4 + 3);
		rect.setColor(new Vector3f(0.545f, 0.271f, 0.075f));
		rect.update();
		
		for (int i = 0; i < 6; i++) {
			if (i < 4)
				vertices[i + currentRectangle * 4] = rect.getVertices()[i];
			indices[i + currentRectangle * 6] = rect.getIndices()[i];
		}
		currentRectangle ++;
		
		//Left top wall
		topLeft 	= points[0]; 
		topRight 	= points[3];
		bottomRight = points[6];
		bottomLeft 	= points[0];
		
		rect = new Rectangle(topLeft, topRight, bottomRight, bottomLeft);
		rect.setIndices(currentRectangle * 4, currentRectangle * 4 + 1, 
						currentRectangle * 4 + 2, currentRectangle * 4 + 3);
		rect.setColor(new Vector3f(0.545f, 0.271f, 0.075f));
		rect.update();
		
		for (int i = 0; i < 6; i++) {
			if (i < 4)
				vertices[i + currentRectangle * 4] = rect.getVertices()[i];
			indices[i + currentRectangle * 6] = rect.getIndices()[i];
		}
		currentRectangle ++;
		
		//Right top wall
		topLeft 	= points[1]; 
		topRight 	= points[7];
		bottomRight = points[2];
		bottomLeft 	= points[1];
		
		rect = new Rectangle(topLeft, topRight, bottomRight, bottomLeft);
		rect.setIndices(currentRectangle * 4, currentRectangle * 4 + 1, 
						currentRectangle * 4 + 2, currentRectangle * 4 + 3);
		rect.setColor(new Vector3f(0.545f, 0.271f, 0.075f));
		rect.update();
		
		for (int i = 0; i < 6; i++) {
			if (i < 4)
				vertices[i + currentRectangle * 4] = rect.getVertices()[i];
			indices[i + currentRectangle * 6] = rect.getIndices()[i];
		}
	}
	
	public Vertex[] getVertices() {
		return vertices;
	}
	
	public int[] getIndices() {
		return indices;
	}
}
