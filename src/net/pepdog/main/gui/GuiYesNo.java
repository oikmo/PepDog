package net.pepdog.main.gui;

import org.lwjgl.util.vector.Vector2f;

import net.pepdog.engine.Loader;
import net.pepdog.engine.gui.GuiScreenOld;
import net.pepdog.engine.gui.component.old.GuiText;
import net.pepdog.engine.gui.component.old.button.GuiButton;
import net.pepdog.engine.gui.font.renderer.TextMaster;
import net.pepdog.main.Main;
import net.pepdog.main.Main.GameState;

public class GuiYesNo extends GuiScreenOld {
	
	private int selected = 0;
	
	GuiText title;
	String titleText;
	
	public GuiYesNo(String text, GameState state) {
		super(Loader.loadGameTexture("ui/ui_background2"),"Yes No");
		title = new GuiText(text, 2, Main.font, new Vector2f(0,0.05f), 1, true, false);
		title.setColour(1, 1, 1);
		Main.gameState = state;
	}

	public void initGui() {
		buttonList.add(new GuiButton(0, new Vector2f(-0.3f,0f), new Vector2f(0.65f,0.1f), "Yes"));
        buttonList.add(new GuiButton(1, new Vector2f(0.3f,0f), new Vector2f(0.65f,0.1f), "No"));
	}

	public void actionPerformed(GuiButton button) {
		if(button.getControlID() == 0) {
			selected = 1;
		}
		if(button.getControlID() == 1) {
			selected = 2;
		}
	}
	
	public void onGUIClosed() {
		TextMaster.removeText(title);
	}
	
	public int isSelected() {
		return selected;
	}
	
}
