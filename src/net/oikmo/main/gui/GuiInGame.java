package net.oikmo.main.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import net.oikmo.engine.DisplayManager;
import net.oikmo.engine.Loader;
import net.oikmo.engine.gui.GuiScreen;
import net.oikmo.engine.gui.component.button.GuiButton;
import net.oikmo.engine.textures.GuiTexture;

public class GuiInGame extends GuiScreen {

	boolean toggle = false;
	public List<GuiScreen> innerScreens;
	GuiButton b1;
	
	private GuiTexture headerBack;

	public GuiInGame() {
		super("In Game");
		innerScreens = new ArrayList<>();
	}

	public void initGui() {
		headerBack = new GuiTexture(Loader.loadGameTexture("ui/ui_backshots"), new Vector2f(0,0), new Vector2f(1.50f,0.1f));
		headerBack.setPosition(new Vector2f(-1+headerBack.getScale().x,1-headerBack.getScale().y));
		uiList.add(headerBack);
		b1 = new GuiButton(0, new Vector2f(-1+0.1f,1-0.05f), new Vector2f(0.3f,0.1f), "Fullscreen", 0.75f);
		buttonList.add(b1);
	}

	public void actionPerformed(GuiButton button) {
		switch(button.getControlID()) {
		case 0:
			DisplayManager.setFullscreen();
			break;
		}
	}

	public void updateScreen() {
		for(GuiScreen screen : innerScreens) {
			if(screen != null) {
				screen.update();
				screen.updateScreen();
			}
		}
	}
}
