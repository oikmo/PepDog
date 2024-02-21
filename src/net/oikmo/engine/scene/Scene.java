package net.oikmo.engine.scene;

import java.util.ArrayList;
import java.util.List;

import net.oikmo.engine.Entity;
import net.oikmo.engine.Part;
import net.oikmo.engine.renderers.water.WaterTile;
import net.oikmo.main.entity.Camera;
import net.oikmo.main.entity.Light;

public abstract class Scene {
	
	private List<Light> lights;
	private List<Entity> entities;
	private List<Part> parts;
	private List<WaterTile> waters;
	
	private boolean loaded = false;
	
	public Scene() {
		lights = new ArrayList<>();
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
	
	public void cleanUp() {
		lights.clear();
		entities.clear();
		parts.clear();
		waters.clear();
		
	}
}
