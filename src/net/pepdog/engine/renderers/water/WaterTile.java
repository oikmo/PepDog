package net.pepdog.engine.renderers.water;

public class WaterTile {
	
	private float TILE_SIZE = 60;
	
	private float height;
	private float x,z;
	
	public WaterTile(float centerX, float centerZ, float height){
		this.x = centerX;
		this.z = centerZ;
		this.height = height;
	}
	
	public WaterTile(float centerX, float centerZ, float height, float tileSize){
		this.x = centerX;
		this.z = centerZ;
		this.height = height;
		this.TILE_SIZE = tileSize;
	}
	
	public float getHeight() {
		return height;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public float getTileSize() {
		return TILE_SIZE;
	}
}
