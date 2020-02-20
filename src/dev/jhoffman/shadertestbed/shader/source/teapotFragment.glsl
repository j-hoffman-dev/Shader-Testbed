#version 330 core

in vec2 ioTexCoords;
in vec3 ioNormal;
in vec3 ioFragPos;

struct Material {
    sampler2D diffuse;
    sampler2D specular;
    float     shininess;
};

struct Light {
    float linear;
    float quadratic;
    vec3  position;
    vec3  ambient;
    vec3  diffuse;
    vec3  specular;
};

uniform vec3     uViewPosition;
uniform Material uMaterial;
uniform Light    uLight;

out vec4 ioResult;

void main() {
    vec3 ambient = uLight.ambient;

    vec3 norm     = normalize(ioNormal);
    vec3 lightDir = normalize(uLight.position - ioFragPos);
    float diff    = max(dot(norm, lightDir), -0.5);
    vec3 diffuse  = uLight.diffuse * diff;

    ioResult = vec4(ambient + diffuse, 1.0);
}