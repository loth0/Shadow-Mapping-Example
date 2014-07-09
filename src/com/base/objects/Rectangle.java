package com.base.objects;

import org.lwjgl.util.vector.Vector3f;

import com.base.util.Vertex;

public class Rectangle {
	private Vector3f topLeft;
	private Vector3f topRight;
	private Vector3f bottomLeft;
	private Vector3f bottomRight;
	
	private int topLeftIndex = 0;
	private int topRightIndex = 1;
	private int bottomRightIndex= 2;
	private int bottomLeftIndex = 3;
	
	private Vector3f color;
	private Vertex[] vertices;
	private int[] indices;
	
	private Vector3f normal;
	
	public Rectangle() {
		this(new Vector3f(-0.5f, 0, -0.5f), new Vector3f(0.5f, 0, -0.5f), new Vector3f(0.5f, 0, 0.5f), new Vector3f(-0.5f, 0, 0.5f));
	}
	
	public Rectangle(Vector3f topLeft, Vector3f topRight, Vector3f bottomRight, Vector3f bottomLeft) {
		this(topLeft, topRight, bottomRight, bottomLeft, new Vector3f(1, 1, 1));
	}

	public Rectangle(Vector3f topLeft, Vector3f topRight, Vector3f bottomRight, Vector3f bottomLeft, Vector3f color) {
		this.topLeft = topLeft;
		this.topRight = topRight;
		this.bottomRight = bottomRight;
		this.bottomLeft = bottomLeft;
		this.color = color;
	}
	
	private void calcVertices() {
		vertices = new Vertex[4];
		normal = new Vector3f();
		Vector3f a = new Vector3f();
		Vector3f b = new Vector3f();
		Vector3f.sub(bottomLeft, topLeft, a);
		if (a.equals(new Vector3f(0, 0, 0))) {
			Vector3f.sub(bottomRight, topRight, a);
		}
		Vector3f.sub(topRight, topLeft, b);
		if (b.equals(new Vector3f(0, 0, 0))) {
			Vector3f.sub(bottomRight, bottomLeft, b);
		}
		Vector3f.cross(a, b, normal);
		normal.normalise(normal);
		
		vertices[0] = new Vertex(topLeft, color, normal);
		vertices[1] = new Vertex(topRight, color, normal);
		vertices[2] = new Vertex(bottomRight, color, normal);
		vertices[3] = new Vertex(bottomLeft, color, normal);
	}
	
	public Vector3f getNormal() {
		return normal;
	}

	private void updateIndices() {
		indices = new int[] {topLeftIndex, topRightIndex, bottomRightIndex, 
				topLeftIndex, bottomRightIndex, bottomLeftIndex};
	}
	
	public void update() {
		calcVertices();
		updateIndices();
	}
	
	public void setIndices(int topLeftIndex, int topRightIndex, int bottomRightIndex, int bottomLeftIndex) {
		this.topLeftIndex = topLeftIndex;
		this.topRightIndex = topRightIndex;
		this.bottomRightIndex = bottomRightIndex;
		this.bottomLeftIndex = bottomLeftIndex;
		updateIndices();
	}

	public int[] getIndices() {
		return indices;
	}

	public Vertex[] getVertices() {
		return vertices;
	}
	
	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}
}