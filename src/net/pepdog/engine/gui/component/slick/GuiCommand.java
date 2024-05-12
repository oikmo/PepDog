package net.pepdog.engine.gui.component.slick;

public class GuiCommand {
	
	protected float x, y;
	protected float width, height;
	
	public void invoke() { return; }
	public void invoke(float sliderValue) { return; }
	public void update() { return; }
	
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public float getWidth() {
		return width;
	}
	public float getHeight() {
		return height;
	}
}

