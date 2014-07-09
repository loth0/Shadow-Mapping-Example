#version 330 core

layout(location = 0) in vec3 position;

uniform mat4 depthMatrix;
uniform mat4 depthModelMatrix;

void main(){
	gl_Position =  depthMatrix * depthModelMatrix * vec4(position,1);
}