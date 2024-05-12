package net.pepdog.engine;

import java.text.DecimalFormat;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import net.pepdog.engine.models.TexturedModel;
import net.pepdog.engine.textures.ModelTexture;
import net.pepdog.toolbox.Toolbox;
import net.pepdog.toolbox.obj.OBJLoader;

public class Entity {
	public TexturedModel model;
	Vector3f position;
	Vector3f rotation;
	float scale;
	private int textureIndex = 0;
	
	public Entity(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
		this.model = model;
		this.init(position, rotation, scale);
	}
	
	public Entity(String model, Vector3f position, Vector3f rotation) {
		init(model, position, rotation, 1);
	}
	
	public Entity(TexturedModel model, Vector3f position, Vector3f rotation, float scale, int index) {
		this.textureIndex = index;
		this.model = model;
		this.init(position, rotation, scale);
	}

	public Entity(String model, Vector3f position, Vector3f rotation, float scale) {
		this.init(model, position, rotation, scale);
	}
	
	public Entity(String model, Vector3f position, Vector3f rotation, float scale, int index) {
		this.textureIndex = index;
		
		this.init(model, position, rotation, scale);
	}
	
	public void setTextureIndex(int index) {
		this.textureIndex = index;
	}
	
	private void init(Vector3f position, Vector3f rotation, float scale) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale/2;
	}
	
	private void init(String model, Vector3f position, Vector3f rotation, float scale) {
		this.model = new TexturedModel(OBJLoader.loadOBJ(model), new ModelTexture(ResourceLoader.loadTexture("models/"+model)));
		this.position = position;
		this.rotation = rotation;
		this.scale = scale/2;
	}
	
	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}
	
	public void increaseRotation(float dx, float dy, float dz) {
		this.rotation.x += dx;
		this.rotation.y += dy;
		this.rotation.z += dz;
	}
	
	public void setRotation(float dx, float dy, float dz) {
		this.rotation.x = dx;
		this.rotation.y = dy;
		this.rotation.z = dz;
	}
	
	public void setRotationLerp(float dx, float dy, float dz) {
		this.rotation.x = Toolbox.lerp(this.rotation.x, dx, 0.75f * DisplayManager.getFrameTimeSeconds()*10);
		this.rotation.y = Toolbox.lerp(this.rotation.y, dy, 0.75f * DisplayManager.getFrameTimeSeconds()*10);
		this.rotation.z = Toolbox.lerp(this.rotation.z, dz, 0.75f * DisplayManager.getFrameTimeSeconds()*10);
	}
	
	public void setRotation(Vector3f rotation) {
		if(rotation == null) { return; }
		this.rotation = rotation;
	}
	
	public Vector3f getRotation() {
		return this.rotation;
	}
	
	public TexturedModel getModel() {
		return model;
	}
	public void setModel(TexturedModel model) {
		this.model = model;
	}
	public Vector3f getPosition() {
		return position;
	}
	
	DecimalFormat format = new DecimalFormat("#.#");
	public String getPrintPosition() {
		return "X:"+format.format(getPosition().x)+", Y:"+format.format(getPosition().y)+", Z:"+format.format(getPosition().z);
	}
	public void setPosition(Vector3f position) {
		if(position == null) { return; }
		this.position = position;
	}
	public void setPosition(float x, float y, float z) {
		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}
	public float getRotX() {
		return rotation.x;
	}
	public void setRotX(float rotX) {
		this.rotation.x = rotX;
	}
	public float getRotY() {
		return rotation.y;
	}
	public void setRotY(float rotY) {
		this.rotation.y = rotY;
	}
	public float getRotZ() {
		return rotation.z;
	}
	public void setRotZ(float rotZ) {
		this.rotation.z = rotZ;
	}
	public float getScale() {
		return scale;
	}
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public float getTextureXOffset(){
		int column = textureIndex%model.getTexture().getNumberOfRows();
		return (float)column/(float)model.getTexture().getNumberOfRows();
	}
	
	public float getTextureYOffset(){
		int row = textureIndex/model.getTexture().getNumberOfRows();
		return (float)row/(float)model.getTexture().getNumberOfRows();
	}
	
	public Vector2f getCoords() {
		return new Vector2f(getPosition().x, getPosition().z);
	}
	
	//public abstract void tick();
}