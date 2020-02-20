#version 330 core

layout (location = 0) in vec3 aPosition;
layout (location = 1) in vec2 aTexCoords;
layout (location = 2) in vec3 aNormal;

uniform mat4 uModel;
uniform mat4 uView;
uniform mat4 uProjection;
uniform mat3 uNormal;

out vec2 ioTexCoords;
out vec3 ioNormal;
out vec3 ioFragPos;

void main() {
    ioTexCoords = aTexCoords;
    ioNormal    = uNormal * aNormal;
    ioFragPos   = vec3(uModel * vec4(aPosition, 1.0));
    gl_Position = uProjection * uView * uModel * vec4(aPosition, 1.0);
}