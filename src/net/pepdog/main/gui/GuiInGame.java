package net.pepdog.main.gui;

import org.newdawn.slick.opengl.Texture;

import net.pepdog.engine.ResourceLoader;
import net.pepdog.engine.gui.GuiScreen;
import net.pepdog.engine.gui.component.slick.GuiCommand;
import net.pepdog.engine.gui.component.slick.button.GuiButton;

public class GuiInGame extends GuiScreen {
	
	private Texture headerBack = ResourceLoader.loadUITexture("ui/ui_backshots");

	private GuiButton file, edit, view, insert, format;
	
	public GuiInGame() {
		super("In Game");
		this.setLockInput();
	}

	public void onInit() {
		file = new GuiButton(font.getWidth("File")/2, fontSize/2, font.getWidth("File"), font.getHeight("File")+20, "File");
		file.setImages(null, ResourceLoader.loadUITexture("ui/ui_backshots"), null);
		file.setGuiCommand(new GuiCommand() {
			public void invoke() {
				
			}
		});
		/*headerBack = new GuiTexture(Loader.loadGameTexture("ui/ui_backshots"), new Vector2f(0,0), new Vector2f(1.50f,0.1f));
		headerBack.setPosition(new Vector2f(-1+headerBack.getScale().x,1-headerBack.getScale().y));
		uiList.add(headerBack);
		b1 = new GuiButton(0, new Vector2f(-1+0.1f,1-0.05f), new Vector2f(0.3f,0.1f), "Fullscreen", 0.75f);
		buttonList.add(b1);*/
	}

	/*public void actionPerformed(GuiButton button) {
		switch(button.getControlID()) {
		case 0:
			DisplayManager.setFullscreen();
			break;
		}
	}*/

	public void onUpdate() {
		//this.drawTexture(headerBack, 125, 10, 250, 20);
		file.tick();
	}
}
