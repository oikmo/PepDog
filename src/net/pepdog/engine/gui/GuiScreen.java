package net.pepdog.engine.gui;

public class GuiScreen extends Gui {

	protected String screenID;

	private boolean lockInput = false;
	private boolean dontUpdate = false;
	private boolean isUnableToExit = false;

	public GuiScreen(String screenID) {
		this.screenID = screenID;
		onInit();
	}

	public void update() {
		if(dontUpdate) { return; }
		onUpdate();
		cursor.tick();
	}

	public void prepareCleanUp() {
		dontUpdate = true;
		onClose();
	}

	public void onInit() {}
	public void onUpdate() {}
	public void onTick() {}
	public void onClose() {}

	public String getScreenID() {
		return screenID;
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

	public boolean isUnableToExit() {
		return isUnableToExit;
	}
	public void setUAE(boolean isUnableToExit) {
		this.isUnableToExit = isUnableToExit;
	}
}
