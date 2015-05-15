package com.jaxfrank.voxile.rendering;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import com.jaxfrank.voxile.math.Vector2f;
import com.jaxfrank.voxile.math.Vector3f;
import com.jaxfrank.voxile.math.Vector4f;
import com.jaxfrank.voxile.util.Util;

public class ConfigurableMesh {

	private int vboID;
	private int iboID;
	private int size;
	private int stride;
	
	private boolean uploaded = false;
	
	private ArrayList<ArrayList<Float>> vertexData;
	private ArrayList<VertexDataType> vertexLayout;
	private ArrayList<Integer> indices;
	
	public ConfigurableMesh() {
		vertexData = new ArrayList<>();
		vertexLayout = new ArrayList<>();
		indices = new ArrayList<>();
	}
	
	public void addDataF(ArrayList<Float> data) {
		if(uploaded) return;
		vertexLayout.add(VertexDataType.FLOAT);
		for(int i = 0; i < data.size(); i++) {
			if(vertexData.size() < data.size()) {
				vertexData.add(new ArrayList<Float>());
			}
			vertexData.get(i).add(data.get(i));
		}
	}
	
	public void addDataVec2f(ArrayList<Vector2f> data) {
		if(uploaded) return;
		vertexLayout.add(VertexDataType.VEC2);
		for(int i = 0; i < data.size(); i++) {
			if(vertexData.size() < data.size()) {
				vertexData.add(new ArrayList<Float>());
			}
			vertexData.get(i).add(data.get(i).getX());
			vertexData.get(i).add(data.get(i).getY());
		}
	}
	
	public void addDataVec3f(ArrayList<Vector3f> data) {
		if(uploaded) return;
		vertexLayout.add(VertexDataType.VEC3);
		for(int i = 0; i < data.size(); i++) {
			if(vertexData.size() < data.size()) {
				vertexData.add(new ArrayList<Float>());
			}
			vertexData.get(i).add(data.get(i).getX());
			vertexData.get(i).add(data.get(i).getY());
			vertexData.get(i).add(data.get(i).getZ());
		}
	}

	public void addDataVec4f(ArrayList<Vector4f> data) {
		if(uploaded) return;
		vertexLayout.add(VertexDataType.VEC4);
		for(int i = 0; i < data.size(); i++) {
			if(vertexData.size() < data.size()) {
				vertexData.add(new ArrayList<Float>());
			}
			vertexData.get(i).add(data.get(i).getX());
			vertexData.get(i).add(data.get(i).getY());
			vertexData.get(i).add(data.get(i).getZ());
			vertexData.get(i).add(data.get(i).getW());
		}
	}
	
	public void addIndices(ArrayList<Integer> indices) {
		if(uploaded) return;
		this.indices.addAll(indices);
	}
	
	public void done() {
		if(uploaded) return;
		uploaded = true;
		
		vboID = glGenBuffers();
		iboID = glGenBuffers();
		size = indices.size();
		
		stride = 0;
		for(int i = 0; i < vertexLayout.size(); i++) {
			stride += vertexLayout.get(i).size();
		}
		
		FloatBuffer vertexBuffer = Util.createFloatBuffer(vertexData.size() * vertexLayout.size() * (stride / 4));
		for(ArrayList<Float> vertex : vertexData) {
			for(Float f : vertex) {
				vertexBuffer.put(f);
			}
		}
		vertexBuffer.flip();
		vertexData.clear();
		vertexData = null;
		
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
		
		IntBuffer indexBuffer = Util.createIntBuffer(indices.size());
		for(Integer i : indices) {
			indexBuffer.put(i);
		}
		indexBuffer.flip();
		
		indices.clear();
		indices = null;
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);
	}
	
	public void draw() {
		if(!uploaded) return;
		
		for(int i = 0; i < vertexLayout.size(); i++) glEnableVertexAttribArray(i);

		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		int offset = 0;
		for(int i = 0; i < vertexLayout.size(); i++) {
			glVertexAttribPointer(i, vertexLayout.get(i).numFloats(), GL_FLOAT, false, stride, offset);
			offset += vertexLayout.get(i).size();
		}

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
		glDrawElements(GL_TRIANGLES, size, GL_UNSIGNED_INT, 0);

		for(int i = 0; i < vertexLayout.size(); i++) glDisableVertexAttribArray(i);
	}
	
	public void release() {
		glDeleteBuffers(vboID);
		glDeleteBuffers(iboID);
	}
	
}
