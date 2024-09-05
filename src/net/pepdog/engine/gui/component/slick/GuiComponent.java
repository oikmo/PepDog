package net.pepdog.engine.gui.component.slick;

import java.util.ArrayList;

public interface GuiComponent {
	public static ArrayList<GuiComponent> components = new ArrayList<>();
	
	public abstract void onCleanUp();
	public abstract void updateComponent();
	
	public abstract void tick();
}
