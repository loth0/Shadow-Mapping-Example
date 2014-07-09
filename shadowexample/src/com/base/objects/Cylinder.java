package com.base.objects;

import org.lwjgl.util.vector.Vector3f;

import com.base.util.Vertex;

public class Cylinder {
	private int stacks;
	private float bottomRadius;
	private float topRadius;
	private float bottomHeight;
	private float topHeight;
	
	private Vector3f color;
	private Vertex[] vertices;
	private int[] indices;
	
	public Cylinder(){
		this(100, 0.5f, 0.5f, -0.5f, 0.5f);
	}
	
	public Cylinder(int stacks, float bottomRadius, float topRadius, float bottomHeight, float topHeight) {
		this.stacks = stacks;
		this.bottomRadius = bottomRadius;
		this.topRadius = topRadius;
		this.bottomHeight = bottomHeight;
		this.topHeight = topHeight;
		color = new Vector3f(1, 1, 1);
	}
	
	private void calcVertices() {
		vertices = new Vertex[stacks * 4];
		indices = new int[stacks * 6];
		for (int i = 0; i < stacks; i++) {
			Rectangle rect = new Rectangle(
					new Vector3f(topRadius * (float)Math.sin(i * 2.0 * Math.PI /stacks), topHeight, topRadius * (float)Math.cos(i * 2.0 * Math.PI /stacks)), 
					new Vector3f(topRadius * (float)Math.sin((i + 1) * 2.0 * Math.PI /stacks), topHeight, topRadius * (float)Math.cos((i + 1) * 2.0 * Math.PI /stacks)),
					new Vector3f(bottomRadius * (float)Math.sin((i + 1) * 2.0 * Math.PI /stacks), bottomHeight, bottomRadius * (float)Math.cos((i + 1) * 2.0 * Math.PI /stacks)),
					new Vector3f(bottomRadius * (float)Math.sin(i * 2.0 * Math.PI /stacks), bottomHeight, bottomRadius * (float)Math.cos(i * 2.0 * Math.PI /stacks)),
					color);
			
			rect.setIndices(i * 4, i * 4 + 1, i * 4 + 2, i * 4 + 3);
			rect.update();
			
			for (int j = 0; j < 6; j++) {
				if (j < 4)
					vertices[i * 4 + j] = rect.getVertices()[j];
				indices[i * 6 + j] = rect.getIndices()[j];
			}
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

	public float getBottomRadius() {
		return bottomRadius;
	}

	public void setBottomRadius(float bottomRadius) {
		this.bottomRadius = bottomRadius;
	}

	public float getTopRadius() {
		return topRadius;
	}

	public void setTopRadius(float topRadius) {
		this.topRadius = topRadius;
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

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public Vertex[] getVertices() {
		calcVertices();
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
