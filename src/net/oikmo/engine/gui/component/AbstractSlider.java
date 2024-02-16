package net.oikmo.engine.gui.component;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import net.oikmo.engine.DisplayManager;
import net.oikmo.engine.gui.GuiTexture;
import net.oikmo.engine.gui.component.button.IButton;
import net.oikmo.engine.renderers.MasterRenderer;

public abstract class AbstractSlider implements IButton {
	private GuiTexture normal;
	private GuiTexture background;
	
	private Vector2f originalScale;
	
	private boolean isHidden = false, isHovering = false;
	
	private float sliderValue;
	
	public AbstractSlider(float defaultValue, Vector2f position, Vector2f scale) {
		background = new GuiTexture(MasterRenderer.getInstance().ui_nuhuh, position, scale);
		scale.x /=16;
		
		sliderValue = defaultValue;
		
		normal = new GuiTexture(MasterRenderer.getInstance().ui_button, new Vector2f(position.x, position.y), scale);
		originalScale = scale;
		show();
	}
	
	boolean lockOn = false;
	public void update() {
		if(!isHidden) {
			Vector2f location = normal.getPosition();
			Vector2f location2 = background.getPosition();
			Vector2f scale = normal.getScale();
			Vector2f scale2 = background.getScale();
			
			Vector2f mouseCoords = DisplayManager.getNormalisedMouseCoords();
			//System.out.println(Math.abs((int)(location2.x - (scale2.x/2))+(location.x+(scale.x/2))) + " " + scale.x/2);
			
			
			if(location.y + scale.y > -mouseCoords.y && location.y - scale.y < -mouseCoords.y && location.x + scale.x > mouseCoords.x && location.x - scale.x < mouseCoords.x) {
				if (Mouse.isButtonDown(0)) {
					lockOn = true; 
				}
				
				whileHovering(this);
				if(!isHovering) {
					isHovering = true;
					onStartHover(this);
				}
				normal.setTextureID(MasterRenderer.getInstance().ui_smallhover);
			} else {
				if (isHovering) {
					isHovering = false;
					onStopHover(this);
				}
				normal.setTextureID(MasterRenderer.getInstance().ui_smallbutton);
			}
			
			if(!Mouse.isButtonDown(0)) {
				lockOn = false;
			} 
			
			float normalizedValue = (mouseCoords.x - (location2.x - scale2.x / 2)) / scale2.x;
			if(lockOn) {
				normal.setTextureID(MasterRenderer.getInstance().ui_smallhover);
				
				normalizedValue = Math.max(0, Math.min(1, normalizedValue));
	            float newX = (location2.x-scale2.x+scale.x*2) + normalizedValue * ((scale2.x*2)-scale.x*2) - scale.x*2 / 2;
	            location.x = newX;
				sliderValue = normalizedValue;
			} else {
				normalizedValue = sliderValue;
	            float newX = (location2.x-scale2.x+scale.x*2) + normalizedValue * ((scale2.x*2)-scale.x*2) - scale.x*2 / 2;
	            location.x = newX;
			}
		}
		update2();
	}
	
	public boolean isLocked() {
		return lockOn;
	}

	public abstract void update2();
	
	public void show() {
		if(isHidden) {
			//MasterRenderer.getInstance().getGUIList().add(normal);
			isHidden = false;
		}
	}
	
	public void hide() {
		if(!isHidden) {
			//MasterRenderer.getInstance().getGUIList().remove(normal);
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
	
	public GuiTexture getBackTexture() {
		return background;
	}
	
	public GuiTexture getTexture() {
		return normal;
	}

	public float getValue() {
		return sliderValue;
	}
}
