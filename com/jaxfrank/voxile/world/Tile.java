package com.jaxfrank.voxile.world;

import com.jaxfrank.voxile.math.Vector3f;

public class Tile {

	private int id;
	private Vector3f color;

	
	public Tile(int id, Vector3f color) {
		this.id = id;
		this.color = color;
	}
	
	public Vector3f getColor() {
		return color;
	}
	
	public int getID() {
		return id;
	}
}
