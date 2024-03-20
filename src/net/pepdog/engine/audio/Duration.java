package net.pepdog.engine.audio;

public class Duration {
	
	private int totalTime;
	private float totalTimeFloat;
	private int seconds;
	private int minutes;
	
	public Duration(float totalTimeFloat) {
		this.totalTimeFloat = totalTimeFloat;
		this.totalTime = (int)this.totalTimeFloat;
		this.minutes = (totalTime / 60);
		this.seconds = (totalTime % 60);
	}

	public Duration(int minutes,  int seconds) {
		this.minutes = minutes;
		this.seconds = seconds;
	}

	public int getSeconds() {
		return seconds;
	}
	
	public int getMinutes() {
		return minutes;
	}
	
	public float getTotalTime() {
		return this.totalTimeFloat;
	}

	public int getTotalTimei() {
		return this.totalTime;
	}
	
	public void update(float totalTime) {
		this.totalTimeFloat = totalTime;
		this.totalTime = (int)this.totalTimeFloat;
		this.minutes = (this.totalTime / 60);
		this.seconds = (this.totalTime % 60);
	}
}
