package net.pepdog.main.gui;

import org.newdawn.slick.opengl.Texture;

import net.pepdog.engine.ResourceLoader;
import net.pepdog.engine.gui.GuiScreen;
import net.pepdog.engine.gui.component.slick.slider.GuiSlider;
import net.pepdog.main.Main;
import net.pepdog.main.Main.GameState;

public class GuiOptions extends GuiScreen {
	
	private GuiSlider volume;
	private GuiSlider sensitivity;
	private GameState state;
	
	public GuiOptions(GameState state) {
		super("Options");
		this.setLockInput();
		this.state = state;
		Main.gameState = this.state;
	}
	
	public void initGui() {
		
		/*title = new GuiText("Options", 1.5f, Main.font, new Vector2f(0,0.05f), 1, true, false);
		title.setColour(1, 1, 1);
		
		volume = new GuiSlider(0, GameSettings.globalVolume, new Vector2f(-0.3f,0.3f), new Vector2f(0.65f,0.1f), "Volume");
		sliderList.add(volume);
		
		sensitivity = new GuiSlider(1, GameSettings.sensitivity, new Vector2f(0.3f,0.3f), new Vector2f(0.65f,0.1f), "Volume");
		sliderList.add(sensitivity);
		
		buttonList.add(new GuiButton(0, new Vector2f(0f,-0.2f), new Vector2f(0.65f,0.1f), "Back"));*/
	}
	
	/*public void actionPerformed(GuiButton button) {
		int id = button.getControlID();
		if(id == 0) {
			this.prepareCleanUp();
			GameSettings.saveValues();
			switch(state) {
				case pausemenu:
					Main.currentScreen = new GuiPauseMenu();
					break;
			default:
				break;
			}
			
		}
	}*/
	
	private Texture background = ResourceLoader.loadUITexture("ui/ui_background");
	public void updateScreen() {
		this.drawBackground(background);
		/*int valueSens = (int)((sensitivity.getValue()/5f)*1500f)/2;
		float valueSenss = (valueSens*5F)/1500f;
		
		sensitivity.setText("Sensitivity: " + valueSens);
		if(GameSettings.sensitivity != valueSenss) {
			GameSettings.sensitivity = valueSenss;
		}
		
		volume.setText("Volume: " + (int)(volume.getValue() * 100) + "%");
		if(volume.getValue() != GameSettings.globalVolume) {
			GameSettings.globalVolume = volume.getValue();
		}*/
	}
}
