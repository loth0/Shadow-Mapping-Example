package com.base.util;

import org.lwjgl.util.vector.Vector3f;

public class Vertex {
	@Override
	public String toString() {
		return "Vertex [pos=" + pos + ", color=" + color + ", normal=" + normal + "]";
	}

	public static final int SIZE = 9;

	private Vector3f pos;
	private Vector3f color;
	private Vector3f normal;

	public Vertex(Vector3f pos) {
		this(pos, new Vector3f(0, 0, 0));
	}

	public Vertex(Vector3f pos, Vector3f texCoord) {
		this(pos, texCoord, new Vector3f(0, 0, 0));
	}

	public Vertex(Vector3f pos, Vector3f color, Vector3f normal) {
		this.pos = pos;
		this.color = color;
		this.normal = normal;
	}

	public Vector3f getPos() {
		return pos;
	}

	public void setPos(Vector3f pos) {
		this.pos = pos;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public Vector3f getNormal() {
		return normal;
	}

	public void setNormal(Vector3f normal) {
		this.normal = normal;
	}
}
