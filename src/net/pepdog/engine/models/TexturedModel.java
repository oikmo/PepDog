package net.pepdog.engine.models;

import net.pepdog.engine.ResourceLoader;
import net.pepdog.engine.textures.ModelTexture;
import net.pepdog.toolbox.obj.OBJLoader;

public class TexturedModel {
	
	private RawModel rawModel;
	private ModelTexture texture;
	
	public TexturedModel(RawModel rawModel, ModelTexture texture) {
		this.rawModel = rawModel;
		this.texture = texture;
	}
	
	public TexturedModel(String model) {
		this.rawModel = OBJLoader.loadOBJ(model);
		this.texture = new ModelTexture(ResourceLoader.loadTexture("models/"+model));
	}
	
	public TexturedModel(String model, String texture) {
		this.rawModel = OBJLoader.loadOBJ(model);
		this.texture = new ModelTexture(ResourceLoader.loadTexture("models/"+texture));
	}

	public void setRawModel(RawModel model) {
		this.rawModel = model;
	}
	
	public RawModel getRawModel() {
		return rawModel;
	}

	public ModelTexture getTexture() {
		return texture;
	}
}
