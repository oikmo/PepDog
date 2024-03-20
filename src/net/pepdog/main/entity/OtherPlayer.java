package net.pepdog.main.entity;

import org.lwjgl.util.vector.Vector3f;

import net.pepdog.engine.Entity;

public class OtherPlayer extends Entity {
	
	private int id;
	
	public OtherPlayer(int id) {
		super("spartan", new Vector3f(0,0,0), new Vector3f(0,0,0),0.15f);
		this.id = id;
	}
	
	public int getID() {
		return id;
	}
	
	public void updatePosition(float x, float y, float z) {
		this.setPosition(x,y,z);
	}
	
}
