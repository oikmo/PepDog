#version 140

in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D guiTexture;
uniform int tilingSize;

void main(void){
	vec2 tiledCoords = textureCoords * tilingSize;
	out_Color = texture(guiTexture,tiledCoords);

}