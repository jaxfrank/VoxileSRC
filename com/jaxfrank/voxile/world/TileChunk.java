package com.jaxfrank.voxile.world;

import java.util.ArrayList;
import java.util.Random;

import com.jaxfrank.voxile.math.Vector2f;
import com.jaxfrank.voxile.math.Vector2i;
import com.jaxfrank.voxile.math.Vector3f;
import com.jaxfrank.voxile.math.Vector4f;
import com.jaxfrank.voxile.rendering.ConfigurableMesh;

import static org.lwjgl.opengl.GL11.*;

public class TileChunk {
	
	protected static int maxDepth = 4;

	private TileNode head;
	
	protected ArrayList<Vector3f> meshPositions;
	protected ArrayList<Vector4f> meshColors;
	protected ArrayList<Integer> meshIndices;
	
	public TileChunk(int defaultTile) {
		int size = (int)Math.pow(2, maxDepth) / 2;
		head = new TileNode(this, null, defaultTile, new Vector2f(size, size), 0);
	}
	
	public boolean setTile(Vector2i location, int data) {
		if(!isLocationValid(location)) return false;
		boolean status = head.setTile(location, data);
		
		if(status) {
			head.merge();
		}
		
		return status;
	}
	
	public int getTile(Vector2i location) {
		if(!isLocationValid(location)) return -1;
		return head.getTile(location);
	}
	
	public ConfigurableMesh getMesh() {
		meshPositions = new ArrayList<>();
		meshColors = new ArrayList<>();
		meshIndices = new ArrayList<>();
		head.mesh();
		
		ConfigurableMesh mesh = new ConfigurableMesh();
		mesh.addDataVec3f(meshPositions);
		mesh.addDataVec4f(meshColors);
		mesh.addIndices(meshIndices);
		mesh.done();
		return mesh;
	}
	
	private class TileNode {
		public int depth = -1;
		public int data = -1;
		public TileChunk chunk;
		public TileNode parent;
		public TileNode[] children;
		public Vector2f center;
		public int size;
		
		public TileNode(TileChunk chunk, TileNode parent, int data, Vector2f center, int depth) {
			this.chunk = chunk;
			this.parent = parent;
			this.data = data;
			this.depth = depth;
			this.center = center;
			size = (int)Math.pow(2, maxDepth - depth) / 2;
		}
		
		public void mesh() {
			if(children == null) {
				int vertexStart = chunk.meshColors.size();
				float trueSize = (float)Math.pow(2, maxDepth - depth) / 2;
				chunk.meshPositions.add(new Vector3f(new Vector2f(center.getX(), center.getY()).add(new Vector2f(-trueSize, -trueSize)), 0.0f));
				chunk.meshPositions.add(new Vector3f(new Vector2f(center.getX(), center.getY()).add(new Vector2f(-trueSize, trueSize)), 0.0f));
				chunk.meshPositions.add(new Vector3f(new Vector2f(center.getX(), center.getY()).add(new Vector2f(trueSize, trueSize)), 0.0f));
				chunk.meshPositions.add(new Vector3f(new Vector2f(center.getX(), center.getY()).add(new Vector2f(trueSize, -trueSize)), 0.0f));
				
				chunk.meshColors.add(new Vector4f(TileMap.getRegisteredTile(data).getColor(), 1.0f));
				chunk.meshColors.add(new Vector4f(TileMap.getRegisteredTile(data).getColor(), 1.0f));
				chunk.meshColors.add(new Vector4f(TileMap.getRegisteredTile(data).getColor(), 1.0f));
				chunk.meshColors.add(new Vector4f(TileMap.getRegisteredTile(data).getColor(), 1.0f));
				
				chunk.meshIndices.add(vertexStart + 0);
				chunk.meshIndices.add(vertexStart + 1);
				chunk.meshIndices.add(vertexStart + 2);
				
				chunk.meshIndices.add(vertexStart + 0);
				chunk.meshIndices.add(vertexStart + 2);
				chunk.meshIndices.add(vertexStart + 3);
			} else {
				for(int i = 0; i < children.length; i++) {
					children[i].mesh();
				}
			}
		}
		
		public int getTile(Vector2i loc) {
			if(children == null || depth == maxDepth) {
				return data;
			} else {
				if(loc.getX() >= center.getX()) {
					if(loc.getY() >= center.getY()) { // Top Right
						return children[0].getTile(loc); 
					} else { // Bottom Right
						return children[3].getTile(loc);
					}
				} else {
					if(loc.getY() >= center.getY()) { // Top Left
						return children[1].getTile(loc);
					} else { // Top Right
						return children[2].getTile(loc);
					}
				}
			}
		}
		
		public boolean setTile(Vector2i loc, int data) {
			if(depth == maxDepth) {
				this.data = data;
				return true;
			} else {
				if(children == null) {
					children = new TileNode[4];
					children[0] = new TileNode(this.chunk, this, this.data, center.add(new Vector2f(size / 2f, size / 2f)), depth + 1); // Top Right
					children[1] = new TileNode(this.chunk, this, this.data, center.add(new Vector2f(-size / 2f, size / 2f)), depth + 1); // Top Left
					children[2] = new TileNode(this.chunk, this, this.data, center.add(new Vector2f(-size / 2f, -size / 2f)), depth + 1); // Bottom Left
					children[3] = new TileNode(this.chunk, this, this.data, center.add(new Vector2f(size / 2f, -size / 2f)), depth + 1); // Bottom Right
				}
				if(loc.getX() >= center.getX()) {
					if(loc.getY() >= center.getY()) { // Top Right
						return children[0].setTile(loc, data); 
					} else { // Bottom Right
						return children[3].setTile(loc, data);
					}
				} else {
					if(loc.getY() >= center.getY()) { // Top Left
						return children[1].setTile(loc, data);
					} else { // Top Right
						return children[2].setTile(loc, data);
					}
				}
			}
		}
		
		public void merge() {
			if(children != null) {
				boolean canMerge = true;
				int firstData = -1;
				for(int i = 0; i < children.length; i++) {
					children[i].merge();
					if(firstData == -1) {
						firstData = children[i].data;
					}
					if(children[i].children != null || children[i].data != firstData) {
						canMerge = false;
					}
				}
				if(canMerge) {
					this.data = children[0].data;
					children = null;
				}
			}
		}
	}
	
	protected boolean isLocationValid(Vector2i loc) {
		if(loc.getX() < 0 || loc.getY() < 0) return false;
		int size = (int)Math.pow(2, maxDepth);
		if(loc.getX() >= size || loc.getY() >= size) return false;
		return true;
	}
	
	public static int getSize() {
		return (int)Math.pow(2, maxDepth);
	}
	
	public static int getMaxDepth() {
		return maxDepth;
	}

	public static void setMaxDepth(int maxDepth) {
		TileChunk.maxDepth = maxDepth;
	}
}
