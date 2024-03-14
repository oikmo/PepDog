package net.oikmo.main.scene;

import java.util.HashMap;
import java.util.Map;

import net.oikmo.engine.scene.Scene;
import net.oikmo.main.Main;
import net.oikmo.main.Main.GameState;
import net.oikmo.main.entity.Camera;

/**
 * Handles scene management such as updating and loading.
 * @author Oikmo
 *
 */
public class SceneManager {
	
	private static enum SceneName {
		Roblox,
		Empty
	}
	
	private static Map<String, SceneName> scenes;
	
	private static Scene currentScene;
	
	/**
	 * Initalises map to refer back to when loading Scenes.
	 */
	public static void init() {
		scenes = new HashMap<>();
		
		scenes.put("roblox", SceneName.Roblox);
		scenes.put("empty", SceneName.Empty);
	}
	
	/**
	 * Loads scene from HashMap using string.<br>
	 * When the scene is loaded, it tries to insert the player from {@link Main}
	 * @param scene
	 */
	public static void loadScene(String scene) {
		if(currentScene != null) {
			currentScene.cleanUp();
		}
		
		switch(scenes.get(scene)){
		case Empty:
			currentScene = new EmptyScene();
			break;
		case Roblox:
			currentScene = new RobloxScene();
			break;
		}
		
		currentScene.init();
		
		if(Main.gameState == GameState.game) {
			if(!currentScene.getEntities().contains(Main.player)) {
				currentScene.addEntity(Main.player);
			}
		}
		
	}
	
	/**
	 * Makes life easier by just passing arguments to {@link Scene#update(Camera)}
	 * @param camera ({@link Camera})
	 */
	public static void update(Camera camera) {
		getCurrentScene().update(camera);
	}
	
	/**
	 * Returns currently loaded Scene.
	 * @return {@link Scene}
	 */
	public static Scene getCurrentScene() {
		return currentScene;
	}
}
