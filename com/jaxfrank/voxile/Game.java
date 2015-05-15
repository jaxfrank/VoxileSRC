package com.jaxfrank.voxile;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;

import java.util.HashMap;

import com.jaxfrank.voxile.rendering.Window;
import com.jaxfrank.voxile.util.time.Time;

public class Game {

	Window window;

	private HashMap<Integer, Screen> screens;
	private int currentScreen;

	private boolean initialized = false;

	private long previousTime;

	protected int frames = 0;
	
	public Game() {
		screens = new HashMap<>();
	}

	public void run() {
		try {
			init();
			loop();
		} finally {
			window.release();
		}
	}

	private void init() {
		window = new Window(1280, 720, "Voxile");
		window.init();
		initialized = true;
		if (currentScreen >= 0) {
			screens.get(currentScreen).onEnter();
		}
	}

	private void loop() {
		int frames = 0;
		long frameCounter = 0;

		final double frameTime = 1.0 / 60.0;

		long lastTime = Time.getTime();
		double unprocessedTime = 0;

		while (!window.isCloseRequested()) {
			Screen screen = screens.get(currentScreen);
			boolean render = false;

			long startTime = Time.getTime();
			long passedTime = startTime - lastTime;
			lastTime = startTime;

			unprocessedTime += passedTime / (double) Time.SECOND;
			frameCounter += passedTime;

			while (unprocessedTime > frameTime) {
				render = true;

				unprocessedTime -= frameTime;

				Time.setDelta(frameTime);

				glfwPollEvents();
				screen.onUpdate();

				if (frameCounter >= Time.SECOND) {
					this.frames = frames;
					frames = 0;
					frameCounter = 0;
				}
			}
			if (render) {
				screen.onRender();
				window.update();
				frames++;
			} else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		// previousTime = Time.getTime();
		// while (!window.isCloseRequested()) {
		// long currentTime = Time.getTime();
		// double deltaTime = (currentTime - previousTime) / Time.SECOND;
		// Time.setDelta(deltaTime);
		// previousTime = currentTime;
		// Screen screen = screens.get(currentScreen);
		//
		// glfwPollEvents();
		// screen.onUpdate();
		//
		// screen.onRender();
		// window.update();
		// }
	}

	public void addScreen(Screen screen) {
		if (screen.getID() < 0)
			return;
		screens.put(screen.getID(), screen);
	}

	public boolean setCurrentScreen(int screenID) {
		if (screens.containsKey(screenID)) {
			if (currentScreen >= 0 && screens.containsKey(currentScreen)
					&& initialized)
				screens.get(currentScreen).onExit();
			currentScreen = screenID;
			if (initialized)
				screens.get(screenID).onEnter();
			return true;
		} else {
			return false;
		}
	}

	public boolean nextScreen() {
		int nextScreenID = screens.get(currentScreen).nextScreenID();
		if (nextScreenID >= 0 && screens.containsKey(nextScreenID)) {
			if (currentScreen >= 0 && screens.containsKey(currentScreen)
					&& initialized)
				screens.get(currentScreen).onExit();
			currentScreen = nextScreenID;
			if (initialized)
				screens.get(currentScreen).onEnter();
			return true;
		} else {
			return false;
		}
	}

	public boolean previousScreen() {
		int previousScreenID = screens.get(currentScreen).previousScreenID();
		if (previousScreenID >= 0 && screens.containsKey(previousScreenID)) {
			if (currentScreen >= 0 && screens.containsKey(currentScreen)
					&& initialized)
				screens.get(currentScreen).onExit();
			currentScreen = previousScreenID;
			if (initialized)
				screens.get(currentScreen).onEnter();
			return true;
		} else {
			return false;
		}
	}

}
