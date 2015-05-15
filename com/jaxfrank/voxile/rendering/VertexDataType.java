package com.jaxfrank.voxile.rendering;

public enum VertexDataType {
	FLOAT(1),
	VEC2(2),
	VEC3(3),
	VEC4(4);
	
	private int length;
	VertexDataType(int numFloats) {
		this.length = numFloats;
	}
	
	public int size() {
		return length * 4;
	}
	
	public int numFloats() {
		return length;
	}
}
