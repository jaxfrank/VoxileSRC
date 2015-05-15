#version 330

//Inputs
in vec2 fsTexCoord;
in vec3 fsNormal;

//Output
out vec4 fragColor;

//Uniforms
uniform sampler2D sampler;

void main() {
	fragColor = texture(sampler, fsTexCoord);
	if(fragColor == vec4(0,0,0,0))
        fragColor = vec4(1.0, 1.0, 1.0, 1.0);
}