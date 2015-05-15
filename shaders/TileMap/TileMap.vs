#version 330

//Uniforms
uniform mat4 unMVP;

//Inputs
in vec3 position;
in vec2 texCoord;
in vec3 normal;
in vec4 color;


//Outputs
out vec2 fsTexCoord;
out vec3 fsNormal;
out vec4 fsColor;

void main() {
	fsTexCoord = texCoord;
	fsNormal = normal;
	gl_Position = unMVP * vec4(position, 1.0);
}
