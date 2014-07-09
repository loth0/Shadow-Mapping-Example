#version 330

varying vec3 theColor;
varying vec4 shadowCoord;

varying vec3 normal0;
varying vec3 worldPos0;

out vec4 outputColor;

uniform sampler2DShadow shadowMap;

uniform vec3 lightPosition;
uniform float ambient;
uniform float lightRange;
uniform float attenConstant;
uniform float attenLinear;
uniform float attenExponent;

vec4 calcPointLight(vec3 normal)
{
    vec3 lightDirection = worldPos0 - lightPosition;
    float distanceToPoint = length(lightDirection);
    
    if(distanceToPoint > lightRange)
        return vec4(0,0,0,0);
    
    lightDirection = normalize(lightDirection);
    
    float diffuseFactor = dot(normal, -lightDirection);
    
    vec4 color = vec4(theColor, 1.0) * diffuseFactor * ambient;
    
    float attenuation = attenConstant +
                         attenLinear * distanceToPoint +
                         attenExponent * distanceToPoint * distanceToPoint +
                         0.0001;
                         
    return color / attenuation;
}

void main()
{
	vec4 someColor = calcPointLight(normalize(normal0));
	
	
	float visibility = textureProj(shadowMap, shadowCoord) / 2.0;
	visibility += 0.5;
	vec3 tempColor = ambient * theColor;
	outputColor = visibility * someColor;
}