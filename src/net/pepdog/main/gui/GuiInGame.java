package net.pepdog.main.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import net.pepdog.engine.Loader;
import net.pepdog.engine.gui.GuiScreen;
import net.pepdog.engine.gui.component.slick.GuiCommand;
import net.pepdog.engine.gui.component.slick.button.GuiButton;
import net.pepdog.engine.textures.GuiTexture;

public class GuiInGame extends GuiScreen {

	boolean toggle = false;
	public List<GuiScreen> innerScreens;
	
	private GuiButton button;

	public GuiInGame() {
		super("In Game");
		innerScreens = new ArrayList<>();
	}

	public void onInit() {
		button = new GuiButton(new Color(194,194,194, 100), new Color(178,178,178, 100), 100, 50, 100, 20, "x Tools", new GuiCommand() {
			
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
