package net.oikmo.engine.scene;

import java.util.ArrayList;
import java.util.List;

import net.oikmo.engine.Entity;
import net.oikmo.engine.Part;
import net.oikmo.engine.terrain.Terrain;
import net.oikmo.engine.textures.TerrainTexture;
import net.oikmo.engine.textures.TerrainTexturePack;
import net.oikmo.engine.water.WaterTile;
import net.oikmo.main.entity.Camera;
import net.oikmo.main.entity.Light;

public abstract class Scene {
	
	private List<Light> lights;
	private List<Terrain> terrain;
	private List<Entity> entities;
	private List<Part> parts;
	private List<WaterTile> waters;
	
	private boolean loaded = false;
	
	private String seed;
	private TerrainTexturePack texturePack; 
	private TerrainTexture blendMap;
	
	public Scene(String seed, TerrainTexturePack texturePack, TerrainTexture blendMap) {
		lights = new ArrayList<>();
		terrain = new ArrayList<>();
		entities = new ArrayList<>();
		parts = new ArrayList<>();
		waters = new ArrayList<>();
		
		this.seed = seed;
		this.texturePack = texturePack;
		this.blendMap = blendMap;
	}
	
	public Scene() {
		lights = new ArrayList<>();
		terrain = new ArrayList<>();
		entities = new ArrayList<>();
		parts = new ArrayList<>();
		waters = new ArrayList<>();
	}
	
	public abstract void init();
	
	public abstract void update(Camera camera);
	public abstract void update();
	
	public void setLoaded() {
		loaded = true;
	}
	
	public void setLoaded(boolean load) {
		loaded = load;
	}
	
	
	public boolean isLoaded() {
		return loaded;
	}
	
	public List<Light> getLights() {
		return lights;
	}
	
	public void addLight(Light l) {
		this.getLights().add(l);
	}
	
	public List<Terrain> getTerrain() {
		return terrain;
	}
	
	public void addTerrain(Terrain t) {
		this.getTerrain().add(t);
	}
	
	public List<Entity> getEntities() {
		return entities;
	}
	
	public void addEntity(Entity e) {
		this.getEntities().add(e);
	}
	
	public List<Part> getParts() {
		return parts;
	}
	
	public void addPart(Part part) {
		this.getParts().add(part);
	}
	
	public List<WaterTile> getWaters() {
		return waters;
	}
	
	public void addWater(WaterTile w) {
		this.getWaters().add(w);
	}

	public String getSeed() {
		return seed;
	}

	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}

	public TerrainTexture getBlendMap() {
		return blendMap;
	}

	public void cleanUp() {
		lights.clear();
		terrain.clear();
		entities.clear();
		parts.clear();
		waters.clear();
		
	}
}
