package net.pepdog.engine.gui.component.button;

import org.lwjgl.util.vector.Vector2f;

import net.pepdog.engine.DisplayManager;
import net.pepdog.engine.renderers.MasterRenderer;
import net.pepdog.engine.textures.GuiTexture;

public abstract class AbstractButton implements IButton {
	private GuiTexture normal;
	
	private Vector2f originalScale;
	
	private boolean isHidden = false, isHovering = false;
	
	public AbstractButton(Vector2f position, Vector2f scale) {
		normal = new GuiTexture(MasterRenderer.getInstance().ui_button, position, scale);
		originalScale = scale;
		show();
	}
	
	public void update() {
		if(!isHidden) {
			Vector2f location = normal.getPosition();
			Vector2f scale = normal.getScale();
			
			Vector2f mouseCoords = DisplayManager.getNormalisedMouseCoords();
			
			if(location.y + scale.y > -mouseCoords.y && location.y - scale.y < -mouseCoords.y && location.x + scale.x > mouseCoords.x && location.x - scale.x < mouseCoords.x) {
				whileHovering(this);
				if(!isHovering) {
					isHovering = true;
					onStartHover(this);
				}
				normal.setTextureID(MasterRenderer.getInstance().ui_hover);
			} else {
				if (isHovering) {
					isHovering = false;
					onStopHover(this);
				}

				normal.setTextureID(MasterRenderer.getInstance().ui_button);
			}
		}
	}
	
	public void show() {
		if(isHidden) {
			MasterRenderer.getInstance().getGUIList().add(normal);
			isHidden = false;
		}
	}
	
	public void hide() {
		if(!isHidden) {
			MasterRenderer.getInstance().getGUIList().remove(normal);
			isHidden = true;
		}
	}
	
	public void resetScale() {
		normal.setScale(originalScale);
	}
	
	public void playHoverAnimation(float scaleFactor) {
		normal.setScale(new Vector2f(originalScale.x + scaleFactor, originalScale.y + scaleFactor));
	}
	
	public boolean isHidden() {
		return isHidden;
	}
	
	public boolean isHovering() {
		return isHovering;
	}
	
	public GuiTexture getTexture() {
		return normal;
	}
}
