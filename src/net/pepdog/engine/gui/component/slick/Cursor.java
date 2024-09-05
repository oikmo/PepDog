package net.pepdog.engine.gui.component.slick;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import net.pepdog.engine.gui.Gui;

public class Cursor extends Gui implements GuiComponent {
	
	private float x, y;
	
	@Override
	public void tick() {
		x = Mouse.getX() + 10;
		y= Math.abs(Display.getHeight()-Mouse.getY());
		drawSquare(x, y, 10, 10);
	}
	
	@Override
	public void updateComponent() {}
	
	@Override
	public void onCleanUp() {}
	
	public List<String> splitEqually(String text, int size) {
	    // Give the list the right capacity to start with. You could use an array
	    // instead if you wanted.
	    List<String> ret = new ArrayList<String>((text.length() + size - 1) / size);

	    for (int start = 0; start < text.length(); start += size) {
	        ret.add(text.substring(start, Math.min(text.length(), start + size)));
	    }
	    return ret;
	}
}
