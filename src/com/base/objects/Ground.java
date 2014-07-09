package com.base.objects;

import org.lwjgl.util.vector.Vector3f;

import com.base.util.Vertex;

public class Ground {
	private float groundSize;
	private float groundHeight;
	private Vertex[] vertices;
	private int[] indices;

	public Ground(float groundSize, float groundHeight) {
		this.groundSize = groundSize;
		this.groundHeight = groundHeight;
	}
	
	public Ground() {
		this(40, 0);
	}
	
	public void update() {
		calcVertInd();
	}

	public float getGroundHeight() {
		return groundHeight;
	}

	public void setGroundHeight(float groundHeight) {
		this.groundHeight = groundHeight;
	}

	public float getGroundSize() {
		return groundSize;
	}

	public void setGroundSize(float groundSize) {
		this.groundSize = groundSize;
	}

	private void calcVertInd() {
		Vector3f topLeft = new Vector3f(-groundSize / 2.0f, groundHeight, -groundSize / 2.0f); 
		Vector3f topRight = new Vector3f(groundSize / 2.0f, groundHeight, -groundSize / 2.0f);
		Vector3f bottomRight = new Vector3f(groundSize / 2.0f, groundHeight, groundSize / 2.0f);
		Vector3f bottomLeft = new Vector3f(-groundSize / 2.0f, groundHeight, groundSize / 2.0f);
		
		Rectangle rect = new Rectangle(topLeft, topRight, bottomRight, bottomLeft, new Vector3f(0, 0.75f, 0));
		rect.update();
		
		vertices = rect.getVertices();
		indices = rect.getIndices();
	}

	public Vertex[] getVertices() {
		return vertices;
	}

	public int[] getIndices() {
		return indices;
	}
}