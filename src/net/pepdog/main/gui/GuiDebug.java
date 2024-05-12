package net.pepdog.main.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.UnicodeFont;

import net.pepdog.engine.DisplayManager;
import net.pepdog.engine.gui.Gui;
import net.pepdog.engine.gui.GuiScreen;

public class GuiDebug extends GuiScreen {

	public GuiDebug() {
		super("debug");
	}
	
	private int fontSize = (int)(Gui.fontSize/1.5f);
	private UnicodeFont font = this.calculateFont(fontSize);
	
	public void onUpdate() {
		long maxMem = Runtime.getRuntime().maxMemory();
		long totalMem = Runtime.getRuntime().totalMemory();
		long freeMem = Runtime.getRuntime().freeMemory();
		long usedMem = totalMem - freeMem;

		this.drawShadowString(font, Color.white, 0, 0, "fps: " + Integer.toString(DisplayManager.getFPSCount()));		
		this.drawShadowString(font, Color.white, 0, fontSize, "Used memory: " + (usedMem * 100L) / maxMem +"% (" + usedMem / 1024L / 1024L + "MB) of " + maxMem / 1024L / 1024L + "MB");		
		this.drawShadowString(font, Color.white, 0, fontSize*2, "Allocated memory: " + (totalMem * 100L) / maxMem +"% (" + totalMem / 1024L / 1024L + "MB)");		
	}

}
