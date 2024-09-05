package net.pepdog.engine.gui.component.old.button;

public interface IButton {
	void onStartHover(IButton button);
	
	void onStopHover(IButton button);
	
	void whileHovering(IButton button);
	
	void show();
	
	void hide();
	
	void playHoverAnimation(float scaleFactor);
	
	void resetScale();
	
	void update();

	void cleanUp();
	
	void restore();
}
