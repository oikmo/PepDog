package net.oikmo.main.entity;

import org.lwjgl.util.vector.Vector3f;

import net.oikmo.engine.Entity;
import net.oikmo.engine.models.TexturedModel;

public class Cube extends Entity {

	public Cube(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
		super(model, position, rotation, scale);
		this.name = "cube";
		
		System.out.println(model + " " + position + " " + rotation + " " + scale);
	}
	
	public Cube(TexturedModel texturedModel, Vector3f vector3f, float f) {
		super(texturedModel, vector3f, new Vector3f(), f);
	}

	public void tick() {
		this.increasePosition(0, 1, 0);
	}
}
