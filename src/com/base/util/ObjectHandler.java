package com.base.util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.util.vector.Vector3f;

public class ObjectHandler {
	private FloatBuffer matrix44Buffer;
	private MatrixHandler modelMatrix;
	private boolean shadow;
	private int vbo;
	private int ibo;
	private int abo;
	private int size;

	public ObjectHandler(Vertex[] vertices, int[] indices, boolean hasShadow) {
		matrix44Buffer = BufferTools.reserveFloatData(16);
		modelMatrix = new MatrixHandler();
		shadow = hasShadow;
		vbo = glGenBuffers();
		ibo = glGenBuffers();
		abo = glGenVertexArrays();
		size = 0;
		
		addVertices(vertices, indices);
		setUp();
	}

	private void setUp() {
		glBindVertexArray(abo);
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);

		glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, Vertex.SIZE * 4, 12);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, Vertex.SIZE * 4, 24);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);

		glBindVertexArray(0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);

		if (shadow)
			setUpShadowBuffer();
	}

	public void draw(int shaderProgram, int modelMatrixID) {
		glUseProgram(shaderProgram);

		modelMatrix.store(matrix44Buffer);
		matrix44Buffer.flip();
		glUniformMatrix4(modelMatrixID, false, matrix44Buffer);

		glBindVertexArray(abo);
		glDrawElements(GL_TRIANGLES, size, GL_UNSIGNED_INT, 0);
		glBindVertexArray(0);

		glUseProgram(0);
	}

	private void addVertices(Vertex[] vertices, int[] indices) {
		size = indices.length;

		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, createFlippedBuffer(vertices), GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, createFlippedBuffer(indices), GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	private static FloatBuffer createFlippedBuffer(Vertex[] vertices) {
		FloatBuffer buffer = BufferTools.reserveFloatData(vertices.length * Vertex.SIZE);

		for (int i = 0; i < vertices.length; i++) {
			buffer.put(vertices[i].getPos().getX());
			buffer.put(vertices[i].getPos().getY());
			buffer.put(vertices[i].getPos().getZ());
			buffer.put(vertices[i].getColor().getX());
			buffer.put(vertices[i].getColor().getY());
			buffer.put(vertices[i].getColor().getZ());
			buffer.put(vertices[i].getNormal().getX());
			buffer.put(vertices[i].getNormal().getY());
			buffer.put(vertices[i].getNormal().getZ());
		}

		buffer.flip();

		return buffer;
	}

	private static IntBuffer createFlippedBuffer(int[] indices) {
		IntBuffer buffer = BufferTools.reserveIntData(indices.length);

		buffer.put(indices);

		buffer.flip();

		return buffer;
	}

	public void setUpShadowBuffer() {
		if (shadow) {
			glBindVertexArray(abo);
	
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
	
			glBindVertexArray(0);
		}
	}

	public void updateShadowBuffer(int shadowShaderProgram, int depthModelMatrixID) {
		if (shadow) {
			glUseProgram(shadowShaderProgram);
			modelMatrix.store(matrix44Buffer);
			matrix44Buffer.flip();
			glUniformMatrix4(depthModelMatrixID, false, matrix44Buffer);
			
			glBindVertexArray(abo);
			glDrawElements(GL_TRIANGLES, size, GL_UNSIGNED_INT, 0);
			glBindVertexArray(0);
	
			glUseProgram(0);
		}
	}

	public void setPosition(Vector3f position) {
		modelMatrix.setPosition(position);
	}

	public void rotate(Vector3f angle) {
		modelMatrix.setAngle(angle);
	}
	
	public MatrixHandler getModelMatrix() {
		return modelMatrix;
	}
}
