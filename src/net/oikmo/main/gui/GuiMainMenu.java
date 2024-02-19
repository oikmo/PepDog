package net.oikmo.main.gui;

import org.lwjgl.util.vector.Vector2f;

import net.oikmo.engine.Loader;
import net.oikmo.engine.gui.GuiScreen;
import net.oikmo.engine.gui.component.GuiText;
import net.oikmo.engine.gui.component.button.GuiButton;
import net.oikmo.engine.gui.font.fontRendering.TextMaster;
import net.oikmo.main.Main;
import net.oikmo.main.Main.GameState;

public class GuiMainMenu extends GuiScreen {
	
	private GuiYesNo yOrN;
	private GuiText title;
	
	public GuiMainMenu() {
		super(Loader.getInstance().loadTexture("ui/ui_background"),"Main Menu");
		Main.gameState = GameState.mainmenu;
	}
	
	public void initGui() {
		float x = (1-0.8f)/2;
		title = new GuiText("RBXL LOADER", 3f, Main.font, new Vector2f(x,0.2f), 1, false, false);
		title.setColour(1, 1, 1);
		buttonList.add(new GuiButton(0, new Vector2f(-0.55f, 0.2f), new Vector2f(0.65f,0.1f), "Play game"));
        buttonList.add(new GuiButton(1, new Vector2f(-0.55f, 0f), new Vector2f(0.65f,0.1f), "Options"));
        buttonList.add(new GuiButton(2, new Vector2f(-0.55f, -0.2f), new Vector2f(0.65f,0.1f), "Quit"));
	}

	public void actionPerformed(GuiButton button) {
		this.prepareCleanUp();
		if(button.getControlID() == 0) {
			Main.gameState = GameState.game;
			
			Main.currentScreen = new GuiInGame();
		}
		if(button.getControlID() == 1) {
			Main.currentScreen = new GuiOptions(GameState.mainmenu);
		}
		if(button.getControlID() == 2) {
			tryClose();
		}
	}
	
	public void updateScreen() {
		if(yOrN != null)	 {
			if(!this.isUnableToExit()) {
				this.setUAE(true);
			}
			yOrN.update();
			
			if(yOrN.isSelected() != 0) {
				if(yOrN.isSelected() == 1) {
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Main.destroyGame();
				} else if(yOrN.isSelected() == 2){
					
					yOrN.prepareCleanUp();
					this.restore();
					yOrN = null;
				}
			}
		} else {
			if(this.isUnableToExit()) {
				this.setUAE(false);
			}
		}
	}
	
	public void tryClose() {
		if(yOrN == null) {
			yOrN = new GuiYesNo("Are you sure you want to close the game?", GameState.mainmenu);
		}
	}
	
	public void onGUIClosed() {
		TextMaster.removeText(title);
	}

}
