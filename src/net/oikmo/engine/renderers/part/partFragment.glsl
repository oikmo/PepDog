#version 150

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float visibility;

out vec4 out_Colour;

uniform sampler2D modelTexture;
uniform vec3 lightColour;
uniform vec3 attenuation;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColour;
uniform vec3 partColour;
uniform float alphaValue;

void main(void) {
	
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitVectorToCamera = normalize(toCameraVector);
	
	vec3 totalDiffuse = vec3(0,0,0);
	vec3 totalSpecular = vec3(0,0,0);
	
	float distance = length(toLightVector);
	float attFactor = attenuation.x + (attenuation.y * distance) + (attenuation.z * distance*distance);
	vec3 unitLightVector = normalize(toLightVector);
		
	float nDot1 = dot(unitNormal, unitLightVector);
	float brightness = max(nDot1,0.5);
	
	vec3 lightDir = -unitLightVector;
	vec3 reflectedLightDir = reflect(lightDir,unitNormal);
	
	float specularFactor = dot(reflectedLightDir, unitVectorToCamera);
	specularFactor = max(specularFactor,0.0);
	float dampedFactor = pow(specularFactor,shineDamper);
	
	totalDiffuse = (totalDiffuse + brightness * lightColour)/attFactor;
	totalSpecular = (totalSpecular + dampedFactor * reflectivity * lightColour);
	totalDiffuse = max(totalDiffuse, 0.1);
	vec4 textureColour = texture(modelTexture,pass_textureCoords);
	
	out_Colour = vec4(totalDiffuse,1.0) * (textureColour * vec4(partColour,1.0)) + vec4(totalSpecular,1.0);
	out_Colour.w = alphaValue;
	if(alphaValue < 0.1) {
    	discard;
    }
}