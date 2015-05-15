package com.jaxfrank.voxile.rendering;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class Shader {

	int program;
	private HashMap<String, Integer> uniforms;

	public Shader() {
		program = glCreateProgram();
		uniforms = new HashMap<String, Integer>();

		if (program == 0) {
			System.err
					.println("Shader creation failed: Could not find valid memory location in constructor");
			System.exit(1);
		}
	}

	public void bind() {
		glUseProgram(program);
	}

	public void unBind() {
		glUseProgram(0);
	}

	public void addUniform(String uniform) {
		int uniformLocation = glGetUniformLocation(program, uniform);

		if (uniformLocation == 0xFFFFFFFF) {
			System.err.println("Error: Could not find uniform: " + uniform);
			new Exception().printStackTrace();
			System.exit(1);
		}

		uniforms.put(uniform, uniformLocation);
	}

	public int getUniformLocation(String uniform) {
		return uniforms.get(uniform);
	}
	
	public void addVertexShaderFromFile(String text) {
		addProgram(loadShader(text), GL_VERTEX_SHADER);
	}

	public void addGeometryShaderFromFile(String text) {
		addProgram(loadShader(text), GL_GEOMETRY_SHADER);
	}

	public void addFragmentShaderFromFile(String text) {
		addProgram(loadShader(text), GL_FRAGMENT_SHADER);
	}

	public void addVertexShader(String text) {
		addProgram(text, GL_VERTEX_SHADER);
	}

	public void addGeometryShader(String text) {
		addProgram(text, GL_GEOMETRY_SHADER);
	}

	public void addFragmentShader(String text) {
		addProgram(text, GL_FRAGMENT_SHADER);
	}

	public void compileShader() {
		glLinkProgram(program);
		
		if (glGetProgrami(program, GL_LINK_STATUS) == 0) {
			System.err.println(glGetProgramInfoLog(program, 1024));
			System.exit(1);
		}

		glValidateProgram(program);

		if (glGetProgrami(program, GL_VALIDATE_STATUS) == 0) {
			System.err.println(glGetProgramInfoLog(program, 1024));
			System.exit(1);
		}
	}

	private void addProgram(String text, int type) {
		int shader = glCreateShader(type);

		if (shader == 0) {
			System.err.println("Shader creation failed: Could not find valid memory location when adding shader");
			System.exit(1);
		}

		glShaderSource(shader, text);
		glCompileShader(shader);

		if (glGetShaderi(shader, GL_COMPILE_STATUS) == 0) {
			System.err.println(glGetShaderInfoLog(shader, 1024));
			System.exit(1);
		}

		glAttachShader(program, shader);
	}

	private static String loadShader(String fileName) {
		StringBuilder shaderSource = new StringBuilder();
		BufferedReader shaderReader = null;

		try {
			shaderReader = new BufferedReader(new FileReader("./res/" + fileName));
			String line;

			while ((line = shaderReader.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}

			shaderReader.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		return shaderSource.toString();
	}

}
