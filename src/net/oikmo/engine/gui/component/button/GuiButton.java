package net.oikmo.engine.gui.component.button;

import org.lwjgl.util.vector.Vector2f;

import net.oikmo.engine.gui.component.GuiText;
import net.oikmo.engine.gui.font.fontRendering.TextMaster;
import net.oikmo.main.Main;

public class GuiButton extends AbstractButton {

	public int controlID;
	private Vector2f scale;
    private Vector2f position;
    GuiText text;
	
	public GuiButton(int controlID, Vector2f position, Vector2f scale, String text) {
		super(position, scale);
		this.controlID = controlID;
		this.position = position;
		this.scale = scale;
		float x = Math.abs((1+position.x)/2);
		float y = Math.abs((1-position.y)/2);
		this.text = new GuiText(text, 1.3f, Main.font, new Vector2f(x,y), 1, false, true);
		this.text.setColour(1, 1, 1);
		show();
	} 
	
	public void setText(String text) {
		if(this.text.getTextString().contentEquals(text)) { return; }
		float x = Math.abs((1+position.x)/2);
		float y = Math.abs((1-position.y)/2);
		this.text.setPosition(x, y);
		this.text.setTextString(text);
	}
	
	public Vector2f getScale() {
		return scale;
	}

	public void setScale(Vector2f scale) {
		this.scale = scale;
	}

	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}
	
	public int getControlID() {
		return controlID;
	}
	
	public String getDisplayString() {
		return text.getTextString();
	}
	
	@Override
	public void onStartHover(IButton button) {}
	
	@Override
	public void onStopHover(IButton button) {}
	
	@Override
	public void whileHovering(IButton button) {}
	
	@Override
	public void cleanUp() {
		TextMaster.removeText(text);
	}
	
	@Override
	public void restore() {
		float x = Math.abs((position.x+1)/2);
		float y = Math.abs((position.y-1)/2);
		text.setPosition(x,y);
		TextMaster.loadText(text);
		
	}

}
