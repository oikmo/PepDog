package net.oikmo.main.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import net.oikmo.engine.Loader;
import net.oikmo.engine.gui.GuiScreen;
import net.oikmo.engine.gui.component.GuiText;
import net.oikmo.engine.gui.component.button.GuiButton;
import net.oikmo.engine.textures.GuiTexture;
import net.oikmo.main.Main;

public class GuiInGame extends GuiScreen {

	boolean toggle = false;
	public List<GuiScreen> innerScreens;
	GuiText text;
	
	private GuiTexture headerBack;

	public GuiInGame() {
		super("In Game");
		innerScreens = new ArrayList<>();
	}

	public void initGui() {
		text = new GuiText("", 2, Main.font, new Vector2f(0,0), 1, true, false);
		text.setColour(1, 0, 1);
		headerBack = new GuiTexture(Loader.getInstance().loadTexture("ui/ui_backshots"), new Vector2f(0,0), new Vector2f(1.50f,0.05f));
		headerBack.setPosition(new Vector2f(-1+headerBack.getScale().x,1-headerBack.getScale().y));
		uiList.add(headerBack);
	}

	public void actionPerformed(GuiButton button) {}

	long lastClick = 150;
	long coolDownTime = 150;
	public void updateScreen() {
		for(GuiScreen screen : innerScreens) {
			if(screen != null) {
				screen.update();
				screen.updateScreen();
			}
			
		}
		
		
	}

	public void onGUIClosed() {
		text.remove();
	}
}
