package com.jaxfrank.voxile.rendering;

import java.util.ArrayList;

import com.jaxfrank.voxile.math.Vector3f;
import com.jaxfrank.voxile.math.Vector4f;

public class TileMapMesh extends ConfigurableMesh {
	
	private ArrayList<Vector3f> positions;
	private ArrayList<Vector4f> colors;
	private ArrayList<Integer> indices;
	
	public TileMapMesh() {
		positions = new ArrayList<>();
		colors = new ArrayList<>();
		indices = new ArrayList<>();
	}
	
	public void addVertex(Vector3f position, Vector4f color) {
		if(positions != null && colors != null) {
			positions.add(position);
			colors.add(color);
		}
	}
	
	public void addIndex(int index) {
		if(indices != null) {
			indices.add(index);
		}
	}

	public int vertexCount() {
		return positions.size();
	}
	
	@Override
	public void done() {
		addDataVec3f(positions);
		addDataVec4f(colors);
		addIndices(indices);
		
		super.done();
	}
	
	
	
}
