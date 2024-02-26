package net.oikmo.engine.scene;

import java.util.ArrayList;
import java.util.List;

import net.oikmo.engine.Entity;
import net.oikmo.engine.Part;
import net.oikmo.engine.renderers.water.WaterTile;
import net.oikmo.main.entity.Camera;
import net.oikmo.main.entity.Light;

/**
 * Abstract class for scene management.
 * Stores positions of lights, entities, parts and water tiles.
 * 
 * @author Oikmo
 */
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
	
	/**
	 * Activates and initialises scene.
	 */
	public abstract void init();
	
	/**
	 * Handles updates and rendering (by sending its data to {@link net.oikmo.engine.renderers.MasterRenderer} with camera.
	 * @param camera
	 */
	public abstract void update(Camera camera);
	
	/**
	 * Used when Scene has finished loading from {@link #init()}
	 */
	public void setLoaded() {
		loaded = true;
	}
	
	/**
	 * Allows the scene to not be able to render.
	 * @param load (boolean)
	 */
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
	
	/**
	 * Clears all entities, lights, parts and water tiles.
	 */
	public void cleanUp() {
		lights.clear();
		entities.clear();
		parts.clear();
		waters.clear();
	}
}
