package com.jaxfrank.voxile;

public class Main {
	public static void main(String[] args) {
		Game g = new Game();
		MainScreen mainScreen = new MainScreen();
		g.addScreen(mainScreen);
		g.setCurrentScreen(mainScreen.getID());
		g.run();
	}
}
