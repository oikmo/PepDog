package net.pepdog.engine.gui.component.slick.button;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import net.pepdog.engine.gui.Gui;
import net.pepdog.engine.gui.component.slick.GuiCommand;
import net.pepdog.engine.gui.component.slick.GuiComponent;
//import net.pepdog.engine.sound.SoundMaster;

public class GuiButton extends Gui implements GuiComponent {

	private Image normalTexture;
	private Image hoveredTexture;
	
	private Image texture = normalTexture;

	private String text;
	private GuiCommand command;

	private float x, y, width, height;
	private boolean lockButton = false;

	private boolean isHovering = false;

	private void onInit() {
		try {
			if(normalTexture == null) {
				normalTexture = Gui.guiAtlas.getSubImage(0, 66, 200, 20);
				normalTexture.clampTexture();
			}
			if(hoveredTexture == null) {
				hoveredTexture = Gui.guiAtlas.getSubImage(0, 86, 200, 20);
				normalTexture.clampTexture();
			}
		} catch(Exception e) {
			
		}
		
	}

	public GuiButton(float x, float y, float width, float height, String text, GuiCommand command) {
		onInit();
		this.text = text;
		this.command = command;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		components.add(this);
	}

	public GuiButton(float x, float y, float width, float height, String text) {
		onInit();
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		components.add(this);
	}
	
	public GuiButton(Image normalTexture, Image hoveredTexture, float x, float y, float width, float height, String text) {
		this.normalTexture = normalTexture;
		this.hoveredTexture = hoveredTexture;
		this.normalTexture.setFilter(Image.FILTER_NEAREST);
		this.hoveredTexture.setFilter(Image.FILTER_NEAREST);
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		components.add(this);
	}


	@Override
	public void tick() {
		float mouseX = Mouse.getX();
		float mouseY = Math.abs(Display.getHeight()-Mouse.getY());

		if(y + height/2 > mouseY && y-height/2 < mouseY && x + width/2 > mouseX && x-width/2 < mouseX) {
			texture = hoveredTexture;
			isHovering = true;
			if(Mouse.isButtonDown(0)) {
				if(!lockButton) {
					if(!lockedRightNow && current != this) {
						lockedRightNow = true;
						current = this;
						//SoundMaster.playSFX("ui.button.click");
						command.invoke();
					}
				} else {
					lockedRightNow = false;
					current = null;
				}
			} else {
				lockedRightNow = false;
				current = null;
				lockButton = false;
			}
		} else {
			lockButton = false;
			isHovering = false;
			texture = normalTexture;
			if(lockedRightNow && current == this) {
				lockedRightNow = false;
				current = null;

			}
		}
		
		
		drawImage(texture, x, y, width, height);
		
		Color c = isHovering ? new Color(0.9f,0.9f,0.1f,1f) : Color.white;
		drawShadowStringCentered(c, x, y, text);
	}

	public void tick(boolean shouldClicky) {
		float mouseX = Mouse.getX();
		float mouseY = Math.abs(Display.getHeight()-Mouse.getY());

		if(y + height/2 > mouseY && y-height/2 < mouseY && x + width/2 > mouseX && x-width/2 < mouseX) {
			isHovering = true;
			texture = hoveredTexture;
			if(Mouse.isButtonDown(0) && shouldClicky) {
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
			texture = normalTexture;
			if(lockedRightNow && current == this) {
				lockedRightNow = false;
				current = null;
			}
		}
		
		drawImage(texture, x, y, width, height);
		
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

	public Image getTexture() {
		return texture;
	}

	public String getText() {
		return text;
	}


}