package net.oikmo.main.gui;

import org.lwjgl.util.vector.Vector2f;

import net.oikmo.engine.Loader;
import net.oikmo.engine.gui.GuiScreen;
import net.oikmo.engine.gui.component.GuiText;
import net.oikmo.engine.gui.component.button.GuiButton;
import net.oikmo.engine.gui.font.fontRendering.TextMaster;
import net.oikmo.main.Main;
import net.oikmo.main.Main.GameState;

public class GuiYesNo extends GuiScreen {
	
	private int selected = 0;
	
	GuiText title;
	String titleText;
	
	public GuiYesNo(String text, GameState state) {
		super(Loader.getInstance().loadTexture("ui/ui_background2"),"Yes No");
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
