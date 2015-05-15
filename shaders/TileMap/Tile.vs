#version 330

//Uniforms
uniform mat4 unMVP;

//Inputs
in vec3 position;
in vec4 color;


//Outputs
out vec4 fsColor;

void main() {
	fsColor = color;
	gl_Position = vec4(position, 1.0) * unMVP;
}
