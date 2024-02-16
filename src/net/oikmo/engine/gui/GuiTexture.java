package net.oikmo.engine.gui;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

public class GuiTexture {
	
	private int textureID;
	private Vector2f position;
	private Vector2f scale;
	private int tilingSize = 1;
	private int zOrder;
	
	public GuiTexture(int textureID, Vector2f position, Vector2f scale) {
		this.textureID = textureID;
		
		float offset = Display.getWidth() - Display.getHeight();
		float scaleX =  (scale.x / Display.getWidth()) * offset;
		float scaleY =  (scale.y / Display.getHeight()) * offset;
		
		this.scale = new Vector2f(scaleX, scaleY);
		//Vector2f pos = Maths.getNormalizedDeviceCoords(position, scale);
		this.position =  position;
		
	}
	
	public void setTextureID(int texture) {
		this.textureID = texture;
	}

	public int getTextureID() {
		return textureID;
	}

	public Vector2f getPosition() {
		return position;
	}
	
	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public Vector2f getScale() {
		return scale;
	}

	public void setScaleP(Vector2f scale) {
		float offset = Display.getWidth() - Display.getHeight();
		float scaleX =  (scale.x / Display.getWidth()) * offset;
		float scaleY =  (scale.y / Display.getHeight()) * offset;
		
		this.scale = new Vector2f(scaleX, scaleY);
	}
	
	public void setScale(Vector2f scale) {
		this.scale = scale;
	}

	public int getTilingSize() {
		return tilingSize;
	}

	public void setTilingSize(int tilingSize) {
		this.tilingSize = tilingSize;
	}
	
	public int getZOrder() {
		return zOrder;
	}
}
