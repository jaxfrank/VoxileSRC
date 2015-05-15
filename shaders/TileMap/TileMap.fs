#version 330

//Inputs
in vec2 fsTexCoord;
in vec3 fsNormal;
in vec4 fsColor;

//Output
out vec4 fragColor;

void main() {
	fragColor = texture(sampler, fsTexCoord);
	if(fragColor == vec4(0,0,0,0))
        fragColor = vec4(unColor, 1.0);
	else
		fragColor = fragColor * vec4(unColor, 1.0);
}