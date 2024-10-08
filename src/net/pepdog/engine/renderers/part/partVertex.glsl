#version 150

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 pass_textureCoords;
out vec3 surfaceNormal;
out vec3 toLightVector;
out vec3 toCameraVector;
out float visibility;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;

uniform float useFakeLighting;

uniform float numberOfRows;
uniform vec2 offset;

uniform vec4 plane;

const float density = 0; //0.003
const float gradient = 100000; //1.5

void main(void) {
    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);

    gl_ClipDistance[0] = dot(worldPosition, plane);

    vec4 positionRelativeToCam = viewMatrix * worldPosition;
    gl_Position = projectionMatrix * positionRelativeToCam;
    pass_textureCoords = (textureCoords / numberOfRows) + offset;

    vec3 actualNormal = normal;
    if (useFakeLighting > 0.5) {
        actualNormal = vec3(0.0, 1.0, 0.0);
    }

    surfaceNormal = normalize((transformationMatrix * vec4(actualNormal, 0.0)).xyz);
    toLightVector = normalize(lightPosition - worldPosition.xyz);
    toCameraVector = normalize((inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz);

    float distance = length(positionRelativeToCam.xyz);
    visibility = exp(-pow((distance * density), gradient));
    visibility = clamp(visibility, 0.0, 1.0);
}
