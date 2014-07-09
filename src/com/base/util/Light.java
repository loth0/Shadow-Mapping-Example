package com.base.util;

import static org.lwjgl.opengl.GL20.*;

import org.lwjgl.util.vector.Vector3f;

public class Light {
	
	private Vector3f position;
	private float ambient;
	private float range;
	private float attenConstant;
	private float attenLinear;
	private float attenExponent;
	
	public Light(Vector3f position, float ambient, float range, float attenConstant, float attenLinear, float attenExponent) {
		this.position = position;
		this.ambient = ambient;
		this.range = range;
		this.attenConstant = attenConstant;
		this.attenLinear = attenLinear;
		this.attenExponent = attenExponent;
	}
	
	public void update(int shaderProgram, int lightPositionID, int ambientID, int lightRangeID, int attenConstantID, int attenLinearID, int attenExponentID) {
		glUseProgram(shaderProgram);
		
		glUniform3f(lightPositionID, position.x, position.y, position.z);
		glUniform1f(ambientID, ambient);
		glUniform1f(lightRangeID, range);
		glUniform1f(attenConstantID, attenConstant);
		glUniform1f(attenLinearID, attenLinear);
		glUniform1f(attenExponentID, attenExponent);

		glUseProgram(0);
	}
	
	public Vector3f getPosition() {
		return position;
	}
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	public float getAmbient() {
		return ambient;
	}
	public void setAmbient(float ambient) {
		this.ambient = ambient;
	}
	public float getRange() {
		return range;
	}
	public void setRange(float range) {
		this.range = range;
	}
	public float getAttenConstant() {
		return attenConstant;
	}
	public void setAttenConstant(float attenConstant) {
		this.attenConstant = attenConstant;
	}
	public float getAttenLinear() {
		return attenLinear;
	}
	public void setAttenLinear(float attenLinear) {
		this.attenLinear = attenLinear;
	}
	public float getAttenExponent() {
		return attenExponent;
	}
	public void setAttenExponent(float attenExponent) {
		this.attenExponent = attenExponent;
	}
	
	
}
