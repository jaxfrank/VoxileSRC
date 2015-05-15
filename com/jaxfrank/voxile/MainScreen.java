package com.jaxfrank.voxile;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import java.util.Random;

import com.jaxfrank.voxile.math.Matrix4f;
import com.jaxfrank.voxile.math.Vector2f;
import com.jaxfrank.voxile.math.Vector2i;
import com.jaxfrank.voxile.math.Vector3f;
import com.jaxfrank.voxile.rendering.Camera;
import com.jaxfrank.voxile.rendering.StandardMesh;
import com.jaxfrank.voxile.rendering.Shader;
import com.jaxfrank.voxile.rendering.Texture;
import com.jaxfrank.voxile.rendering.Transform;
import com.jaxfrank.voxile.rendering.Vertex;
import com.jaxfrank.voxile.util.Util;
import com.jaxfrank.voxile.util.time.Time;
import com.jaxfrank.voxile.world.Tile;
import com.jaxfrank.voxile.world.TileChunk;
import com.jaxfrank.voxile.world.TileMap;

public class MainScreen extends Screen {

//	private Transform transform;
	private Camera camera;
	
//	private Shader passThrough;
//	private Mesh mesh;
//	private Texture brickTexture;
	
	private TileMap map;
	
	@Override
	public void onEnter() {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		camera = new Camera();
		camera.setForward(new Vector3f(0.0f, 0.0f, -1.0f));
		Transform.setCamera(camera);
//		Transform.setProjection(90.0f, 1280, 720, 0f, 100f);
		
		Transform.setOrthographic(0f, 1280f, 720f, 0f, -1.0f, 1.0f);
		
//		transform = new Transform();
//		transform.setTranslation(0.0f, 0.0f, -2.0f);
		//transform.setScale(2.0f, 2.0f, 1.0f);
		
//		passThrough = new Shader();
//		passThrough.addFragmentShaderFromFile("shaders/PassThrough/PassThrough.fs");
//		passThrough.addVertexShaderFromFile("shaders/PassThrough/PassThrough.vs");
//		passThrough.compileShader();
//		passThrough.bind();
//		passThrough.addUniform("unMVP");
//		
//		mesh = new Mesh(new Vertex[] {
//				new Vertex(new Vector3f(-0.5f, -0.5f, 0.0f), new Vector2f(3.0f,3.0f), new Vector3f(0.0f, 0.0f, 1.0f)),
//				new Vertex(new Vector3f(-0.5f, 0.5f, 0.0f), new Vector2f(3.0f,0.0f), new Vector3f(0.0f, 0.0f, 1.0f)),
//				new Vertex(new Vector3f(0.5f, 0.5f, 0.0f), new Vector2f(0.0f,0.0f), new Vector3f(0.0f, 0.0f, 1.0f)), 
//				new Vertex(new Vector3f(0.5f, -0.5f, 0.0f), new Vector2f(0.0f,3.0f), new Vector3f(0.0f, 0.0f, 1.0f)), 
//				}, 
//				new int[] {
//				0, 1, 2,
//				0, 2, 3
//				});
//
//		brickTexture = new Texture("textures/bricks_light_top.png");
//		brickTexture.bind();
		
		TileMap.registerTile(new Tile(0, new Vector3f(1, 1, 1)));
		TileMap.registerTile(new Tile(1, new Vector3f(0, 0, 1)));
		TileMap.registerTile(new Tile(2, new Vector3f(1, 0, 0)));
		TileMap.registerTile(new Tile(3, new Vector3f(0, 1, 0)));
		
		map = new TileMap();
		TileChunk chunk = map.getChunk();
		Random rand = new Random();
		for(int i = 0; i < 5; i++) {
			chunk.setTile(new Vector2i(rand.nextInt(TileChunk.getSize()), rand.nextInt(TileChunk.getSize())), rand.nextInt(3) + 1);
		}
		for(int i = 0; i < 12; i++) {
			for(int j = 0; j < 4; j++) {
				chunk.setTile(new Vector2i(i, j), 1);
			}
		}
//		chunk.setTile(new Vector2i(4, 4), 3);
		map.init();
	
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		glOrtho(0, 1280, 720, 0, -1, 1);
	}

	@Override
	public void onUpdate() {
//		transform.setRotation(new Vector3f(0.0f, 0.0f, 45 * (float)Math.sin(Time.getTime() / (double)Time.SECOND / 0.5)));
	}

	@Override
	public void onRender() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		map.render();
//		glUniformMatrix4fv(passThrough.getUniformLocation("unMVP"), false, Util.createFlippedBuffer(transform.getProjectedTransformation()));
//		mesh.draw();
	}

	@Override
	public void onExit() {
//		mesh.delete();
	}

	@Override
	public int nextScreenID() {
		return -1;
	}

	@Override
	public int getID() {
		return 0;
	}

	@Override
	public int previousScreenID() {
		return -1;
	}

}
