package com.jaxfrank.voxile;

public abstract class Screen {

	public abstract void onEnter();
	public abstract void onUpdate();
	public abstract void onRender();
	public abstract void onExit();
	
	public abstract int nextScreenID();
	public abstract int getID();
	public abstract int previousScreenID();
	
	@Override
	public int hashCode() {
		return getID();
	}
	
}
