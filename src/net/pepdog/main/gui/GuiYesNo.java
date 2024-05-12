package net.pepdog.main.gui;

import org.newdawn.slick.opengl.Texture;

import net.pepdog.engine.ResourceLoader;
import net.pepdog.engine.gui.GuiScreen;
import net.pepdog.main.Main;
import net.pepdog.main.Main.GameState;

public class GuiYesNo extends GuiScreen {
	
	private int selected = 0;
	private Texture background = ResourceLoader.loadUITexture("ui/ui_background2");
	
	String titleText;
	
	public GuiYesNo(String text, GameState state) {
		super("Yes No");
		/*title = new GuiText(text, 2, Main.font, new Vector2f(0,0.05f), 1, true, false);
		title.setColour(1, 1, 1);*/
		Main.gameState = state;
	}

	public void initGui() {
		/*buttonList.add(new GuiButton(0, new Vector2f(-0.3f,0f), new Vector2f(0.65f,0.1f), "Yes"));
        buttonList.add(new GuiButton(1, new Vector2f(0.3f,0f), new Vector2f(0.65f,0.1f), "No"));*/
	}

	/*public void actionPerformed(GuiButton button) {
		if(button.getControlID() == 0) {
			selected = 1;
		}
		if(button.getControlID() == 1) {
			selected = 2;
		}
	}*/
	
	public void onUpdate() {
	this.drawBackground(background);
	}
	public int isSelected() {
		return selected;
	}
	
}
