package net.pepdog.main.gui;

import org.lwjgl.opengl.Display;

import net.pepdog.engine.gui.GuiScreen;
import net.pepdog.engine.gui.component.slick.button.GuiButton;

public class GuiPauseMenu extends GuiScreen {
	
	public GuiPauseMenu() {
		super("Pause Menu");
		this.setLockInput();
	}
	
	private GuiButton backToGame;
	
	public void onInit() {
		/*title = new GuiText("Paused...", 1.5f, Main.font, new Vector2f(0,0.05f), 1, true, false);
		title.setColour(1, 1, 1);
		buttonList.add(new GuiButton(0, new Vector2f(0f, 0.2f), new Vector2f(0.65f,0.15f), "Back to game"));
		buttonList.add(new GuiButton(1, new Vector2f(0f,   0f),  new Vector2f(0.7f,0.15f),   "Options"));
		buttonList.add(new GuiButton(2, new Vector2f(0f,-0.2f),  new Vector2f(0.7f,0.15f),  "Quit game"));*/
	}

	/*public void actionPerformed(GuiButton button) {
		int id = button.getControlID();
		if(id == 0) {
			this.prepareCleanUp();
			Main.gameState = GameState.game;
			Main.currentScreen = new GuiInGame();
		}
		if(id == 1) {
			this.prepareCleanUp();
			Main.currentScreen = new GuiOptions(GameState.pausemenu);
		}
		if(id == 2) {
			this.prepareCleanUp();
			Main.destroyGame();
		}

	}*/

	public void onUpdate() {
		this.drawShadowStringCentered(Display.getWidth()/2, 0 + fontSize*2, "Paused...");
	}
}
