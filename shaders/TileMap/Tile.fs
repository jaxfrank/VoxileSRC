#version 330

//Inputs
in vec4 fsColor;

//Output
out vec4 fragColor;

//Uniforms
uniform vec3 unColor;

void main() {
	fragColor = vec4(unColor, 1.0) * fsColor;
}