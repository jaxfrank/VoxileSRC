package com.jaxfrank.voxile.world;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import com.jaxfrank.voxile.math.Vector2f;
import com.jaxfrank.voxile.math.Vector2i;
import com.jaxfrank.voxile.math.Vector3f;
import com.jaxfrank.voxile.rendering.ConfigurableMesh;
import com.jaxfrank.voxile.rendering.StandardMesh;
import com.jaxfrank.voxile.rendering.Shader;
import com.jaxfrank.voxile.rendering.TileMapMesh;
import com.jaxfrank.voxile.rendering.Transform;
import com.jaxfrank.voxile.rendering.Vertex;
import com.jaxfrank.voxile.util.Util;

public class TileMap {

	private static ArrayList<Tile> tiles = new ArrayList<>();
	
	private TileChunk chunk;
	
	private Shader tileShader;
	private ConfigurableMesh tileMesh;
	private Transform tileTransform;
	
	public TileMap() {
		chunk = new TileChunk(0);
		
		tileTransform = new Transform();
		tileTransform.setTranslation(0.0f, 0.0f, 0.0f);
		//transform.setScale(2.0f, 2.0f, 1.0f);
		
		tileShader = new Shader();
		tileShader.addFragmentShaderFromFile("shaders/TileMap/Tile.fs");
		tileShader.addVertexShaderFromFile("shaders/TileMap/Tile.vs");
		tileShader.compileShader();
		tileShader.bind();
		tileShader.addUniform("unMVP");
		tileShader.addUniform("unColor");
		
//		ArrayList<Vector3f> positions = new ArrayList<>();
//		positions.add(new Vector3f(0f, 0f, 0.0f));
//		positions.add(new Vector3f(0f, 1f, 0.0f));
//		positions.add(new Vector3f(1f, 1f, 0.0f));
//		positions.add(new Vector3f(1f, 0f, 0.0f));
//		ArrayList<Integer> indices = new ArrayList<>();
//		indices.add(0);
//		indices.add(1);
//		indices.add(2);
//		indices.add(0);
//		indices.add(2);
//		indices.add(3);
//		tileMesh = new ConfigurableMesh();
//		tileMesh.addDataVec3f(positions);
//		tileMesh.addIndices(indices);
//		tileMesh.done();
	}
	
	public void init() {
		tileMesh = chunk.getMesh();
	}
	
	public void update() {
		
	}
	
	public void render() {
		tileShader.bind();
		int colorLocation = tileShader.getUniformLocation("unColor");
		int unMVPLocation = tileShader.getUniformLocation("unMVP");
		tileTransform.setScale(16, 16, 1);
		glUniform3f(colorLocation, 1.0f, 1.0f, 1.0f);
		tileTransform.setTranslation(new Vector3f(0, 0, 0f));
		glUniformMatrix4fv(unMVPLocation, false, Util.createFlippedBuffer(tileTransform.getOrthographicTransformation()));
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		tileMesh.draw();
		
		tileTransform.setTranslation(new Vector3f(0, 16 * 16, 0f));
		glUniformMatrix4fv(unMVPLocation, false, Util.createFlippedBuffer(tileTransform.getOrthographicTransformation()));
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		tileMesh.draw();
	}
	
	public TileChunk getChunk() {
		return chunk;
	}
	
	public static int numRegisteredTiles() {
		return tiles.size();
	}
	
	public static boolean registerTile(Tile tile) {
		if(tile.getID() != tiles.size()) return false;
		tiles.add(tile);
		return true;
	}
	
	public static Tile getRegisteredTile(int id) {
		if(id < 0 || id >= tiles.size()) return null;
		return tiles.get(id);
	}
}
