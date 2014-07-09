package com.base.objects;

import org.lwjgl.util.vector.Vector3f;

import com.base.util.Vertex;

public class Sphere {
	private int stacks;
	private float radius;
	private Cylinder cylinder;
	
	private Vector3f color;
	private Vertex[] vertices;
	private int[] indices;
	
	public Sphere(){
		this(25, 0.5f);
	}
	
	public Sphere(int stacks, float radius) {
		this(stacks, radius, new Vector3f(1, 1, 1));
	}
	
	public Sphere(int stacks, float radius, Vector3f color) {
		this.stacks = stacks;
		this.radius = radius;
		this.color = color;
	}
	
	private void calcVertices() {
		vertices = new Vertex[stacks * stacks * 4];
		indices = new int[stacks * stacks * 6];
		float tempBottomRadius;
		float tempTopRadius;
		float tempBottomHeight;
		float tempTopHeight;
		int nextIndex = 0;
		for (int i = 0; i < stacks; i++) {
			tempTopRadius = radius * (float)Math.sin(i * Math.PI / stacks);
			tempBottomRadius = radius * (float)Math.sin((i + 1) * Math.PI / stacks);
			
			tempTopHeight = radius * (float)Math.cos(i * Math.PI / stacks);
			tempBottomHeight = radius * (float)Math.cos((i + 1) * Math.PI / stacks);
			
			cylinder = new Cylinder(stacks, 
					tempBottomRadius, 
					tempTopRadius, 
					tempBottomHeight, 
					tempTopHeight);
			cylinder.setColor(color);
			cylinder.update();
			for (int j = 0; j < cylinder.getVertices().length; j++) {
				vertices[i * stacks * 4 + j] = cylinder.getVertices()[j];
			}
			
			for (int j = 0; j < cylinder.getIndices().length; j++) {
				indices[i * stacks * 6 + j] = cylinder.getIndices()[j] + nextIndex;
			}
			
			nextIndex += cylinder.getIndices()[cylinder.getIndices().length - 1] + 1;
		}
	}
	
	public void update() {
		calcVertices();
	}

	public int getStacks() {
		return stacks;
	}

	public void setStacks(int stacks) {
		this.stacks = stacks;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public Vertex[] getVertices() {
		return vertices;
	}

	public void setVertices(Vertex[] vertices) {
		this.vertices = vertices;
	}

	public int[] getIndices() {
		return indices;
	}

	public void setIndices(int[] indices) {
		this.indices = indices;
	}
}
