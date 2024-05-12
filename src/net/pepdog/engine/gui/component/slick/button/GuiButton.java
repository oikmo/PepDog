package net.pepdog.engine.gui.component.slick.button;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.opengl.Texture;

import net.pepdog.engine.gui.Gui;
import net.pepdog.engine.gui.component.slick.GuiCommand;
import net.pepdog.engine.gui.component.slick.GuiComponent;

public class GuiButton extends Gui implements GuiComponent {
	private Texture normal, hover, pressed;
	private Texture texture;

	private String text;
	private GuiCommand command;

	private float x, y, width, height;
	private boolean lockButton = false;

	private boolean isHovering = false;

	public GuiButton(float x, float y, float width, float height, String text, GuiCommand command) {
		this.text = text;
		this.command = command;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		components.add(this);
	}

	public GuiButton(float x, float y, float width, float height, String text) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		components.add(this);
	}
	
	public void setImages(Texture normal, Texture hover, Texture pressed) {
		this.normal = normal;
		this.hover = hover;
		this.pressed = pressed;
	}

	long thing = System.currentTimeMillis()+500L;
	@Override
	public void tick() {
		float mouseX = Mouse.getX();
		float mouseY = Math.abs(Display.getHeight()-Mouse.getY());

		if(y + height/2 > mouseY && y-height/2 < mouseY && x + width/2 > mouseX && x-width/2 < mouseX) {
			if(System.currentTimeMillis() - thing >= 500) {
				texture = hover;	
			}
			isHovering = true;
			if(Mouse.isButtonDown(0)) {
				if(command != null) {
					if(!lockButton) {
						if(!lockedRightNow && current != this) {
							lockedRightNow = true;
							current = this;
							thing = System.currentTimeMillis();
							texture = pressed;
							//SoundMaster.playSFX("ui.button.click");
							command.invoke();
						}
					}
				}
			} else {
				if(lockedRightNow && current == this) {
					lockedRightNow = false;
					current = null;

				}
				lockButton = false;
			}
		} else {
			thing = System.currentTimeMillis();
			isHovering = false;
			texture = normal;
			if(lockedRightNow && current == this) {
				lockedRightNow = false;
				current = null;

			}
		}
		
		if(texture != null) {
			
			drawTexture(texture, x, y, width, height);
		}
		
		Color c = isHovering ? new Color(0.9f,0.9f,0.1f,1f) : Color.white;
		drawShadowStringCentered(c, x, y, text);
	}

	public void tickNoTextures() {
		float mouseX = Mouse.getX();
		float mouseY = Math.abs(Display.getHeight()-Mouse.getY());

		if(y + height/2 > mouseY && y-height/2 < mouseY && x + width/2 > mouseX && x-width/2 < mouseX) {
			isHovering = true;
			if(Mouse.isButtonDown(0)) {
				if(command != null) {
					if(!lockButton) {
						if(!lockedRightNow && current != this) {
							lockedRightNow = true;
							current = this;
							//SoundMaster.playSFX("ui.button.click");
							command.invoke();
						}
					}
				}
			} else {
				
				lockButton = false;
			}
		} else {
			isHovering = false;
			if(lockedRightNow && current == this) {
				lockedRightNow = false;
				current = null;

			}
		}
		
		Color c = isHovering ? new Color(0.9f,0.9f,0.1f,1f) : Color.white;
		drawShadowStringCentered(c, x, y, text);
	}

	
	public void setText(String text) {
		this.text = text;
	}

	public void updateComponent() {
		command.update();
		this.x = command.getX();
		this.y = command.getY();
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