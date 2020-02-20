#version 330 core

in vec2 ioTexCoords;
in vec3 ioColor;

uniform int uType;
uniform sampler2D uTexture;

out vec4 ioResult;

void main() {
    switch(uType) {
        case 0: //Used for triangle body.
            ioResult = vec4(ioColor, 0);
            break;

        case 1: //Used for textured rectangle.
            if(texture(uTexture, ioTexCoords).a == 0) discard;
            ioResult = texture(uTexture, ioTexCoords);
            break;
    }
}