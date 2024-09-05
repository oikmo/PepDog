package net.pepdog.main.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import net.pepdog.engine.Loader;
import net.pepdog.engine.gui.GuiScreen;
import net.pepdog.engine.gui.component.slick.GuiCommand;
import net.pepdog.engine.gui.component.slick.button.GuiButton;
import net.pepdog.engine.textures.GuiTexture;

public class GuiInGame extends GuiScreen {

	boolean toggle = false;
	public List<GuiScreen> innerScreens;
	
	private GuiButton button;
	private GuiTexture headerBack;

	public GuiInGame() {
		super("In Game");
		innerScreens = new ArrayList<>();
	}

	public void onInit() {
		headerBack = new GuiTexture(Loader.loadGameTexture("ui/ui_backshots"), new Vector2f(0,0), new Vector2f(1.50f,0.1f));
		headerBack.setPosition(new Vector2f(-1+headerBack.getScale().x,1-headerBack.getScale().y));
		//uiList.add(headerBack);
		button = new GuiButton(0, 0, 50, 50, "HELP", new GuiCommand() {
			
		});
	}

	public void onUpdate() {
		button.tick();
	}
	
	public void updateScreen() {
		for(GuiScreen screen : innerScreens) {
			if(screen != null) {
				screen.update();
			}
		}
	}
}
