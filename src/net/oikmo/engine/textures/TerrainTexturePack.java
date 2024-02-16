package net.oikmo.engine.textures;

import net.oikmo.engine.Loader;

public class TerrainTexturePack {
	
	private TerrainTexture backgroundTexture;
	private TerrainTexture rTexture;
	private TerrainTexture gTexture;
	private TerrainTexture bTexture;
	
	public TerrainTexturePack(TerrainTexture backgroundTexture, TerrainTexture rTexture, TerrainTexture gTexture, TerrainTexture bTexture) {
		this.backgroundTexture = backgroundTexture;
		this.rTexture = rTexture;
		this.gTexture = gTexture;
		this.bTexture = bTexture;
	}
	
	public TerrainTexturePack(String backgroundTexture, String rTexture, String gTexture, String bTexture) {
		this.backgroundTexture = new TerrainTexture(Loader.getInstance().loadTexture("terrain/"+backgroundTexture));
		this.rTexture = new TerrainTexture(Loader.getInstance().loadTexture("terrain/"+rTexture));
		this.gTexture = new TerrainTexture(Loader.getInstance().loadTexture("terrain/"+gTexture));
		this.bTexture = new TerrainTexture(Loader.getInstance().loadTexture("terrain/"+bTexture));
	}

	public TerrainTexture getBackgroundTexture() {
		return backgroundTexture;
	}

	public TerrainTexture getrTexture() {
		return rTexture;
	}

	public TerrainTexture getgTexture() {
		return gTexture;
	}

	public TerrainTexture getbTexture() {
		return bTexture;
	}
}
