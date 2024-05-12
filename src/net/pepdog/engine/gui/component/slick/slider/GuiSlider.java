package net.pepdog.engine.gui.component.slick.slider;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import net.pepdog.engine.ResourceLoader;
import net.pepdog.engine.gui.Gui;
import net.pepdog.engine.gui.component.slick.GuiCommand;
import net.pepdog.engine.gui.component.slick.GuiComponent;

public class GuiSlider  extends Gui implements GuiComponent {

	private Texture backgroundTexture;
	private Texture texture;

	private static Texture normalTexture;
	private static Texture hoveredTexture;
	private String text;
	private GuiCommand command;

	private float x, y, width, height, x2, width2;

	private boolean lockButton = false;
	
	private boolean isHovering = false;
	private boolean grabbing = false;

	
	private float sliderValue;

	private void onInit() {
		if(backgroundTexture == null) {
			backgroundTexture = ResourceLoader.loadUITexture("ui/ui_button_background");
		}
		if(normalTexture == null) {
			normalTexture = ResourceLoader.loadUITexture("ui/small/ui_button");
		}
		if(hoveredTexture == null) {
			hoveredTexture = ResourceLoader.loadUITexture("ui/small/ui_button_hover");
		}
	}

	public GuiSlider(float x, float y, float width, float height, String text, GuiCommand command) {
		onInit();
		this.text = text;
		this.command = command;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		this.width2 = 8;
		this.x2 = x-(width/2)+width2/2;
		components.add(this);
	}

	public GuiSlider(float x, float y, float width, float height, String text) {
		onInit();
		this.text = text;
		this.x = x;
		this.y = y;

		this.width = width;
		this.height = height;

		this.width2 = 8;
		this.x2 = x-(width/2)+width2/2;
		
		components.add(this);
	}
	
	@Override
	public void tick() {
		float mouseX = Mouse.getX();
		float mouseY = Math.abs(Display.getHeight()-Mouse.getY());

		if(y + height/2 > mouseY && y-height/2 < mouseY && x2 + width2/2 > mouseX && x2-width2/2 < mouseX) {
			isHovering = true;
			if(Mouse.isButtonDown(0)) {
				if(command != null) {
					grabbing = true; 
					if(!lockButton) {
						if(!lockedRightNow && current != this) {
							Gui.lockedRightNow = true;
							//SoundMaster.playSFX("ui.button.click");
							command.invoke();
							lockButton = true;
							current = this;
						}
						
					}
					command.invoke(sliderValue);
				}
			} else {
				lockButton = false;
			}
		} else {
			isHovering = false;
			texture = normalTexture;
			
		}

		if (grabbing && Mouse.isButtonDown(0)) {
			isHovering = true;
			
			float handleHalfWidth = width2 / 2;
			float minSliderX = x - (width / 2) + handleHalfWidth;
			
			x2 = Math.max(minSliderX, Math.min(x + (width / 2) - handleHalfWidth, mouseX));
			sliderValue = (x2 - (x - width / 2) - handleHalfWidth) / (width - width2);
			sliderValue = Math.max(0, Math.min(1, sliderValue));
		} else {
			grabbing = false;
			if(lockedRightNow && current == this) {
				current = null;
				lockedRightNow = false;
			}
		}

		if(isHovering) {
			texture = hoveredTexture;
		} else {
			texture = normalTexture;
		}
		
		drawTexture(backgroundTexture, x, y, width, height);
		drawTexture(texture, x2, y, width2, height);
		Color c = isHovering ? Color.yellow : Color.white;
		drawShadowStringCentered(c, x, y, text);
	}

	public void updateComponent() {
		command.update();
		this.x = command.getX();
		this.y = command.getY();
		
		sliderValue = Math.max(0, Math.min(1, sliderValue));
		float handleHalfWidth = width2 / 2;
		float calculatedWidth = width - width2;
		x2 = (sliderValue * calculatedWidth) + (x - width / 2) + handleHalfWidth;
		x2 = Math.max(x - (width / 2) + handleHalfWidth, Math.min(x + (width / 2) - handleHalfWidth, x2));
	}
	
	public void setSliderValue(float current, float min, float max) {
	    current = Math.max(min, Math.min(max, current));

	    sliderValue = (current - min) / (max - min);

	    float handleHalfWidth = width2 / 2;
	    float calculatedWidth = width - width2;
	    x2 = (sliderValue * calculatedWidth) + (x - width / 2) + handleHalfWidth;
	    x2 = Math.max(x - (width / 2) + handleHalfWidth, Math.min(x + (width / 2) - handleHalfWidth, x2));
	}

	@Override
	public void onCleanUp() {
		components.remove(this);
	}

	public boolean isHovering() {
		return isHovering;
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void setGuiCommand(GuiCommand command) {
		this.command = command;
	}

	public Texture getTexture() {
		return texture;
	}

	public String getText() {
		return text;
	}


}