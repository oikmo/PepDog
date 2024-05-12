package net.pepdog.engine.gui.component.slick;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import net.pepdog.engine.gui.Gui;

public class Cursor extends Gui implements GuiComponent {
	
	@SuppressWarnings("unused")
	private float x, y;
	
	@Override
	public void tick() {
		x = Mouse.getX() + 10;
		y = Math.abs(Display.getHeight()-Mouse.getY());
	}
	
	@Override
	public void updateComponent() {}
	
	@Override
	public void onCleanUp() {
		
	}
}
