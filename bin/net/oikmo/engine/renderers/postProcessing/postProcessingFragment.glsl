#version 140

in vec2 textureCoords;

out vec4 out_Colour;

uniform sampler2D colourTexture;

const float contrast = 0;

const float scale = 1.0;

const float offset = 5.0;

float find_closest(int x, int y, float c0) {
	vec4 dither[4];

	dither[0] = vec4( 10.0, 33.0, 9.0, 41.0);
	dither[1] = vec4(49.0,17.0, 57.0, 25.0);
	dither[2] = vec4(13.0, 45.0, 5.0, 37.0);
	dither[3] = vec4(61.0,29.0, 53.0, 21.0);
	
	
	for(int i = 0; i<4; i++) {
		dither[i].x += offset;
		dither[i].y += offset;
		dither[i].z += offset;
		dither[i].w += offset;
	}
	
	float limit = 0.0;
	if(x < 4) {
		limit = (dither[x][y]+1.0)/64.0;
	}

	if(c0 < limit) {
		return 0.0;
	} else {
		return 1.0;
	}
}


void main() {
	vec2 p = textureCoords;
	float pixels = 512;
	p.x -= mod(p.x, 1.0 / pixels);
	p.y -= mod(p.y, 1.0 / pixels);
	vec3 color = texture(colourTexture, p).rgb;
	vec3 luminosity = vec3(0.30, 0.59, 0.11);
	float lum = dot(luminosity, color);
	
	vec3 rgb = color;

	vec2 xy = gl_FragCoord.xy * scale;
	int x = int(mod(xy.x, 4.0));
	int y = int(mod(xy.y, 4.0));

	vec3 finalRGB;

	finalRGB.r = find_closest(x, y, rgb.r);
	finalRGB.g = find_closest(x, y, rgb.g);
	finalRGB.b = find_closest(x, y, rgb.b);

	//float final = find_closest(x, y, grayscale);
    
	out_Colour = vec4(finalRGB, 1.0);
	out_Colour.rgb = (out_Colour.rgb - 0.5) * (1 + contrast) + 0.5;	
}