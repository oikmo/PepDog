#version 140

in vec2 position;

out vec2 textureCoords;

void main(void){
	textureCoords = position * 0.5 + 0.5;
	gl_Position = vec4(position, 0.0, 1.0);
}