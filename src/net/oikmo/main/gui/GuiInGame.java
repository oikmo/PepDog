package net.oikmo.main.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import net.oikmo.engine.Loader;
import net.oikmo.engine.gui.GuiScreen;
import net.oikmo.engine.gui.GuiTexture;
import net.oikmo.engine.gui.component.GuiText;
import net.oikmo.engine.gui.component.button.GuiButton;
import net.oikmo.main.Main;

public class GuiInGame extends GuiScreen {

	boolean toggle = false;
	public List<GuiScreen> innerScreens;
	GuiText text;
	GuiTexture cat;
	GuiTexture breen;

	public GuiInGame() {
		super("In Game");
		innerScreens = new ArrayList<>();
	}

	public void initGui() {
		text = new GuiText("", 2, Main.font, new Vector2f(0,0), 1, true, false);
		text.setColour(1, 0, 1);
		cat = new GuiTexture(Loader.getInstance().loadTexture("ui/cat"), new Vector2f(-0.725f,0.68f), new Vector2f(0.6f,0.4f));
		cat.setScaleP(new Vector2f(0.6f,0.4f));
		breen = new GuiTexture(Loader.getInstance().loadTexture("ui/breen"), new Vector2f(0.725f,0.68f), new Vector2f(0.6f,0.4f));
		breen.setScaleP(new Vector2f(0.6f,0.4f));
	}

	public void actionPerformed(GuiButton button) {}

	long lastClick = 150;
	long coolDownTime = 150;
	public void updateScreen() {
		
		/*if(Keyboard.isKeyDown(Keyboard.KEY_F1)) {
			long timeNow = System.currentTimeMillis();
		    long time = timeNow - lastClick;
		    if (time < 0 || time > coolDownTime) {
		    	lastClick = timeNow;
				toggle = !
		    }
		}*/
		
		toggle = Keyboard.isKeyDown(Keyboard.KEY_F1);

		if(toggle) {
			if(!uiList.contains(cat)) {
				uiList.add(cat);
			}
			if(!uiList.contains(breen)) {
				uiList.add(breen);
			}
		} else {
			if(uiList.size() != 0) {
				this.removeAllUI();
			}
		}

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
