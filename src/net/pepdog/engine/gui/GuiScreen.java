package net.pepdog.engine.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import net.pepdog.engine.audio.AudioMaster;
import net.pepdog.engine.audio.Source;
import net.pepdog.engine.gui.component.GuiSlider;
import net.pepdog.engine.gui.component.button.GuiButton;
import net.pepdog.engine.renderers.MasterRenderer;
import net.pepdog.engine.textures.GuiTexture;
import net.pepdog.main.Main;

public class GuiScreen {
	private boolean lockInput = false;
	private GuiTexture background;
	protected List<GuiButton> buttonList;
	protected List<GuiSlider> sliderList;
	protected List<GuiTexture> uiList;
	private int zOrder;
	private GuiButton selectedButton;
	private String screenID;

	private boolean isUnableToExit = false;
	
	public GuiScreen(String screenID) {
		this.screenID = screenID;
		this.buttonList = new ArrayList<GuiButton>();
		this.sliderList = new ArrayList<GuiSlider>();
		this.uiList = new ArrayList<>();
		initGui();
	}

	public GuiScreen(int textureID, String screenID) {
		this.screenID = screenID;
		this.background = new GuiTexture(textureID, new Vector2f(0,0), new Vector2f(1,1));
		this.background.setScale(new Vector2f(1,1));
		this.background.setTilingSize(20);
		this.buttonList = new ArrayList<GuiButton>();
		this.sliderList = new ArrayList<GuiSlider>();
		this.uiList = new ArrayList<>();
		initGui();
	}

	public GuiScreen(int textureID, String screenID, int tilingSize) {
		this.screenID = screenID;
		this.background = new GuiTexture(textureID, new Vector2f(0,0), new Vector2f(1,1));
		this.background.setScale(new Vector2f(1,1));
		this.background.setTilingSize(tilingSize);
		this.buttonList = new ArrayList<GuiButton>();
		this.sliderList = new ArrayList<GuiSlider>();
		this.uiList = new ArrayList<>();
		initGui();
	}

	public void update() {
		if(!dontUpdate) {
			for(int k = 0; k < buttonList.size(); k++) {
				GuiButton comp = (GuiButton)buttonList.get(k);
				comp.update();
				if(!MasterRenderer.getInstance().getGUIList().contains(comp.getTexture())) {
					MasterRenderer.getInstance().addToGUIs(comp.getTexture());
				}
			}
			for(int k = 0; k < sliderList.size(); k++) {
				GuiSlider comp = (GuiSlider)sliderList.get(k);
				comp.update();
				if(!MasterRenderer.getInstance().getGUIList().contains(comp.getTexture())) {
					MasterRenderer.getInstance().addToGUIs(comp.getTexture());
				}
				if(!MasterRenderer.getInstance().getGUIList().contains(comp.getBackTexture())) {
					MasterRenderer.getInstance().addToGUIs(comp.getBackTexture());
				}
			}
			
			for(int k = 0; k < uiList.size(); k++) {
				GuiTexture ui = uiList.get(k);
				if(!MasterRenderer.getInstance().getGUIList().contains(ui)) {
					MasterRenderer.getInstance().addToGUIs(ui);
				}
			}
			if(background != null) {
				if(!MasterRenderer.getInstance().getGUIList().contains(background)) {
					MasterRenderer.getInstance().addToGUIs(background);
				}
			}
			handleInput();
		}
		updateScreen();
	}
	
	public void restore() {
		restoreGUI();
		dontUpdate = false;
		
		for(int k = 0; k < sliderList.size(); k++) {
			GuiSlider comp = (GuiSlider)sliderList.get(k);
			comp.update();
			if(!MasterRenderer.getInstance().getGUIList().contains(comp.getTexture())) {
				MasterRenderer.getInstance().addToGUIs(comp.getTexture());
			}
			if(!MasterRenderer.getInstance().getGUIList().contains(comp.getBackTexture())) {
				MasterRenderer.getInstance().addToGUIs(comp.getBackTexture());
			}
		}
		
		for(int k = 0; k < buttonList.size(); k++) {
			GuiButton comp = (GuiButton)buttonList.get(k);
			comp.restore();
			if(!MasterRenderer.getInstance().getGUIList().contains(comp.getTexture())) {
				MasterRenderer.getInstance().addToGUIs(comp.getTexture());
			}
		}
		for(int k = 0; k < uiList.size(); k++) {
			GuiTexture ui = uiList.get(k);
			if(!MasterRenderer.getInstance().getGUIList().contains(ui)) {
				MasterRenderer.getInstance().addToGUIs(ui);
			}
		}
		if(background != null) {
			if(!MasterRenderer.getInstance().getGUIList().contains(background)) {
				MasterRenderer.getInstance().addToGUIs(background);
			}
		}
		
	}
	
	public void restoreGUI() {}
	
	public boolean dontUpdate = false;
	public void prepareCleanUp() {
		dontUpdate = true;
		for(int k = 0; k < buttonList.size(); k++) {
			GuiButton comp = (GuiButton)buttonList.get(k);
			if(MasterRenderer.getInstance().getGUIList().contains(comp.getTexture())) {
				comp.cleanUp();
				MasterRenderer.getInstance().removeFromGUIs(comp.getTexture());
			}
		}
		
		for(int k = 0; k < sliderList.size(); k++) {
			GuiSlider comp = (GuiSlider)sliderList.get(k);
			if(MasterRenderer.getInstance().getGUIList().contains(comp.getTexture())) {
				MasterRenderer.getInstance().removeFromGUIs(comp.getTexture());
			}
			if(MasterRenderer.getInstance().getGUIList().contains(comp.getBackTexture())) {
				MasterRenderer.getInstance().removeFromGUIs(comp.getBackTexture());
			}
			comp.cleanUp();
		}
		if(uiList.size() != 0) {
			this.removeAllUI();
		}
		if(background != null) {
			if(MasterRenderer.getInstance().getGUIList().contains(background)) {
				MasterRenderer.getInstance().removeFromGUIs(background);
			}
		}
		onGUIClosed();
		
	}

	public String getScreenID() {
		return screenID;
	}

	public void initGui() {}

	protected void actionPerformed(GuiButton guibutton) {}
	protected void actionPerformed(GuiSlider guislider) {}

	public void handleInput() {
		try {
			for(; Mouse.next();    handleMouseInput())    { }
			for(; Keyboard.next(); handleKeyboardInput()) { }
		} catch(IllegalStateException e) {
			Main.error("Input Component Failed!", e);
		}

	}

	public void handleMouseInput()
	{
		if(Mouse.getEventButtonState())
		{
			mouseClicked(Mouse.getEventButton());
		} else
		{
			mouseMovedOrUp(Mouse.getEventButton());
		}
	}

	public void handleKeyboardInput()
	{
		if(Keyboard.getEventKeyState())
		{
			if(Keyboard.getEventKey() == 87)
			{
				//mc.toggleFullscreen();
				return;
			}
			keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
		}
	}

	protected void keyTyped(char c, int i)
	{
		if(i == 1)
		{
			//displayGuiScreen(null);
			//mc.func_6259_e();
		}
	}

	protected void mouseMovedOrUp(int k) {
		if(selectedButton != null && k == 0) {
			if(!selectedButton.isHovering() && !selectedButton.isHidden()) {
				Source click = new Source();
				//click.setVolume(GameSettings.globalVolume+0.05f);
				click.play(AudioMaster.getSound("menu-deselect"));
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				selectedButton = null;
			}
		}
	}

	protected void mouseClicked(int k) {
		if(k == 0) {
			for(int i = 0; i < buttonList.size(); i++) {
				GuiButton guibutton = (GuiButton)buttonList.get(i);
				if(guibutton.isHovering()) {
					selectedButton = guibutton;
					Source click = new Source();
					//click.setVolume(GameSettings.globalVolume+0.05f);
					String button = selectedButton.getDisplayString();
					click.play(button.contains("Back")||button.contains("Quit") ? AudioMaster.getSound("menu-deselect") : AudioMaster.getSound("menu-enter"));
					actionPerformed(guibutton);
				}
			}
			
			for(int i = 0; i < sliderList.size(); i++) {
				GuiSlider guibutton = (GuiSlider)sliderList.get(i);
				if(guibutton.isHovering()) {
					Source click = new Source();
					click.play(AudioMaster.getSound("menu-select"));
				}
			}
		}
	}

	public void updateScreen() {}

	public void onGUIClosed() {}

	public void removeAllUI() {
		for(GuiTexture gui : uiList) {
			if(MasterRenderer.getInstance().getGUIList().contains(gui)) {
				MasterRenderer.getInstance().removeFromGUIs(gui);
			}
		}
		uiList.clear();
	}

	public boolean isLockInput() {
		return lockInput;
	}

	public void setLockInput() {
		this.lockInput = true;
	}

	public void setLockInput(boolean lock) {
		this.lockInput = lock;
	}

	public int getZOrder() {
		return zOrder;
	}
	
	public boolean isUnableToExit() {
		return isUnableToExit;
	}
	
	public void setUAE(boolean boool) {
		isUnableToExit = boool;
	}
}
