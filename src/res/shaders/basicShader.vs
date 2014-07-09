#version 330

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 color;
layout(location = 2) in vec3 normal;

varying vec3 theColor;
varying vec4 shadowCoord;
varying vec3 normal0;
varying vec3 worldPos0;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform mat4 depthBiasMatrix;

void main()
{
	gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(position, 1);
	normal0 = (modelMatrix * vec4(normal, 0.0)).xyz;
	worldPos0 = (modelMatrix * vec4(position, 1.0)).xyz;
	shadowCoord = depthBiasMatrix * modelMatrix * vec4(position, 1);
	theColor = color;
}