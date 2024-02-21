package net.oikmo.main.gui;

import org.lwjgl.util.vector.Vector2f;

import net.oikmo.engine.Loader;
import net.oikmo.engine.gui.GuiScreen;
import net.oikmo.engine.gui.component.GuiText;
import net.oikmo.engine.gui.component.button.GuiButton;
import net.oikmo.engine.gui.font.fontRendering.TextMaster;
import net.oikmo.main.Main;
import net.oikmo.main.Main.GameState;

public class GuiPauseMenu extends GuiScreen {

	private GuiText title;

	public GuiPauseMenu() {
		super(Loader.getInstance().loadTexture("ui/ui_background"), "Pause Menu");
		this.setLockInput();
	}
	
	
	public void initGui() {
		title = new GuiText("Paused...", 1.5f, Main.font, new Vector2f(0,0.05f), 1, true, false);
		title.setColour(1, 1, 1);
		buttonList.add(new GuiButton(0, new Vector2f(0f,0.2f), new Vector2f(0.65f,0.1f), "Back to game"));
		buttonList.add(new GuiButton(1, new Vector2f(0f,0f), new Vector2f(0.65f,0.1f), "Options"));
		buttonList.add(new GuiButton(2, new Vector2f(0f,-0.2f), new Vector2f(0.65f,0.1f), "Quit game"));
	}

	

	public void actionPerformed(GuiButton button) {
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

	}

	public void updateScreen() {
		
	}
	
	public void restoreGUI() {
		TextMaster.loadText(title);
	}
	
	public void onGUIClosed() {
		TextMaster.removeText(title);
	}

	
}
