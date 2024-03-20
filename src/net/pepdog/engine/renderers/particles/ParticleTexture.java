package net.pepdog.engine.renderers.particles;

public class ParticleTexture {
	
	private boolean glowing = false;
	
	private int textureID;
	private int numberOfRows;
	
	public ParticleTexture(int textureID, int numberOfRows) {
		this.textureID = textureID;
		this.numberOfRows = numberOfRows;
	}
	
	public ParticleTexture(boolean glowing, int textureID, int numberOfRows) {
		this.glowing = glowing;
		this.textureID = textureID;
		this.numberOfRows = numberOfRows;
	}
	
	public int getTextureID() {
		return textureID;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public boolean isGlowing() {
		return glowing;
	}
}
