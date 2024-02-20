package net.oikmo.main.gui;

import org.lwjgl.util.vector.Vector2f;

import net.oikmo.engine.Loader;
import net.oikmo.engine.gui.GuiScreen;
import net.oikmo.engine.gui.component.GuiSlider;
import net.oikmo.engine.gui.component.GuiText;
import net.oikmo.engine.gui.component.button.GuiButton;
import net.oikmo.engine.gui.font.fontRendering.TextMaster;
import net.oikmo.main.GameSettings;
import net.oikmo.main.Main;
import net.oikmo.main.Main.GameState;

public class GuiOptions extends GuiScreen {
	
	private GuiText title;
	private GuiSlider volume;
	private GuiButton postProcess;
	private GameState state;
	
	public GuiOptions(GameState state) {
		super(Loader.getInstance().loadTexture("ui/ui_background"), "Options");
		this.setLockInput();
		this.state = state;
		Main.gameState = this.state;
	}
	
	public void initGui() {
		
		title = new GuiText("Options", 1.5f, Main.font, new Vector2f(0,0.05f), 1, true, false);
		title.setColour(1, 1, 1);
		
		volume = new GuiSlider(0, GameSettings.globalVolume, new Vector2f(-0.3f,0.3f), new Vector2f(0.65f,0.1f), "Volume");
		sliderList.add(volume);
		
		postProcess = new GuiButton(1, new Vector2f(0f,0f), new Vector2f(0.65f,0.1f), ("Post Process : " +  GameSettings.postProcess));
		buttonList.add(postProcess);
		
		buttonList.add(new GuiButton(0, new Vector2f(0f,-0.2f), new Vector2f(0.65f,0.1f), "Back"));
	}
	
	public void actionPerformed(GuiButton button) {
		int id = button.getControlID();
		if(id == 0) {
			this.prepareCleanUp();
			GameSettings.saveValues();
			switch(state) {
				case pausemenu:
					Main.currentScreen = new GuiPauseMenu();
					break;
				case mainmenu:
					Main.currentScreen = new GuiMainMenu();
					break;
				default: break;
			}
			
		}
		if(id == 1) {
			GameSettings.postProcess = !GameSettings.postProcess;
			postProcess.setText("Post Process : " +  GameSettings.postProcess);
		}
	}

	public void updateScreen() {
		volume.setText("Volume: " + (int)(volume.getValue() * 100) + "%");
		if(volume.getValue() != GameSettings.globalVolume ) {
			GameSettings.globalVolume = volume.getValue();
		}
	}
	
	public void restoreGUI() {
		TextMaster.loadText(title);
	}
	
	public void onGUIClosed() {
		TextMaster.removeText(title);
	}
	
}
