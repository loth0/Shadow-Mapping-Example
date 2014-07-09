package com.base.objects;

import org.lwjgl.util.vector.Vector3f;

import com.base.util.Vertex;

public class Tree {
	private float treeHeight;
	private float trunkHeight;
	private float topRadius;
	private float trunkRadius;
	private float bottomRadius;
	private int stacks;
	
	private Vertex[] vertices;
	private int[] indices;

	private Cylinder foliage;
	private Cylinder trunk;
	
	public Tree() {
		this(10.0f, 1.0f, 0.0f, 0.75f, 0.4f);
	}
	
	public Tree(float treeHeight, float trunkHeight, float topRadius, float bottomRadius, float trunkRadius) {
		this.treeHeight = treeHeight;
		this.trunkHeight = trunkHeight;
		this.topRadius = topRadius;
		this.trunkRadius = trunkRadius;
		this.bottomRadius = bottomRadius;
		stacks = 100;
	}
	
	private void calcVertices() {
		vertices = new Vertex[stacks * 4 * 2];
		indices = new int[stacks * 6 * 2];
		
		trunk = new Cylinder(stacks, trunkRadius, trunkRadius, 0, trunkHeight + (treeHeight - trunkHeight) / 4.0f);
		trunk.setColor(new Vector3f(0.4f, 0.1f, 0.0f));
		trunk.update();
		
		foliage = new Cylinder(stacks, bottomRadius, topRadius, trunkHeight, treeHeight);
		foliage.setColor(new Vector3f(0.1f, 0.5f, 0.1f));
		foliage.update();
		
		System.arraycopy(trunk.getVertices(), 0, vertices, 0, trunk.getVertices().length);
		System.arraycopy(foliage.getVertices(), 0, vertices, trunk.getVertices().length, foliage.getVertices().length);
		
		int[] tempIndices = new int[foliage.getIndices().length];
		for (int i = 0; i < foliage.getIndices().length; i++) {
			tempIndices[i] = foliage.getIndices()[i] + trunk.getIndices()[trunk.getIndices().length - 1] + 1;
		}
		
		
		System.arraycopy(trunk.getIndices(), 0, indices, 0, trunk.getIndices().length);
		System.arraycopy(tempIndices, 0, indices, trunk.getIndices().length, tempIndices.length);
	}
	
	public void update() {
		calcVertices();
	}
	
	public float getTreeHeight() {
		return treeHeight;
	}

	public void setTreeHeight(float treeHeight) {
		this.treeHeight = treeHeight;
	}

	public float getTrunkHeight() {
		return trunkHeight;
	}

	public void setTrunkHeight(float trunkHeight) {
		this.trunkHeight = trunkHeight;
	}

	public float getTopRadius() {
		return topRadius;
	}

	public void setTopRadius(float topRadius) {
		this.topRadius = topRadius;
	}

	public float getTrunkRadius() {
		return trunkRadius;
	}

	public void setTrunkRadius(float trunkRadius) {
		this.trunkRadius = trunkRadius;
	}

	public float getBottomRadius() {
		return bottomRadius;
	}

	public void setBottomRadius(float bottomRadius) {
		this.bottomRadius = bottomRadius;
	}

	public int getStacks() {
		return stacks;
	}

	public void setStacks(int stacks) {
		this.stacks = stacks;
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
