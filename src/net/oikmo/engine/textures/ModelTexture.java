package net.oikmo.engine.textures;

public class ModelTexture {
	
	private int textureID;
	private int normalMapID;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	private boolean hasTransparency = false;
	private boolean useFakeLighting = false;
	
	private int numberOfRows = 1;
	
	public ModelTexture(int textureID) {
		this.textureID = textureID;
	}
	
	public int getID() {
		return textureID;
	}
	
	public void setID(int textureID) {
		this.textureID = textureID;
	}
	
	public int getNormalID() {
		return normalMapID;
	}

	public void setNormalID(int normalMapID) {
		this.normalMapID = normalMapID;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public boolean isHasTransparency() {
		return hasTransparency;
	}

	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}
	
	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}

	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
	
	public void setPlantLighting() {
		this.hasTransparency = true;
		this.useFakeLighting = true;
	}
}
