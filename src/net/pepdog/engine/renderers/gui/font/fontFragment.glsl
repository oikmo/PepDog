#version 140

in vec2 pass_textureCoords;

out vec4 out_colour;

uniform vec3 colour;
uniform sampler2D fontAtlas;

uniform float width = 0.5;
uniform float edge = 0.1;

uniform float borderWidth = 0.5;
uniform float borderEdge = 0.1;

uniform vec3 outlineColour = vec3(1.0,0.0,0.0);
uniform vec2 offset = vec2(0.006,0.006);

float smoothStep(float edge0, float edge1, float x){
    float t = clamp((x - edge0) / (edge1 - edge0), 0.0, 1.0);
    return t * t * (3.0 - 2.0 * t);
}

void main(void){

	float distance = 1.0 - texture(fontAtlas, pass_textureCoords).a;
	float alpha = 1.0 - smoothStep(width, width + edge, distance);
	
	float distance2 = 1.0 - texture(fontAtlas, pass_textureCoords + offset).a;
	float outlineAlpha = 1.0 - smoothStep(borderWidth, borderWidth + borderEdge, distance2);
	
	float overallAlpha = alpha + (1.0 - alpha) * outlineAlpha;
	vec3 overallColour = mix(outlineColour, colour, alpha / overallAlpha);
	
	out_colour = vec4(overallColour, overallAlpha);
}