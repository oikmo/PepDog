package net.oikmo.main.scene;

import java.util.HashMap;
import java.util.Map;

import net.oikmo.engine.Loader;
import net.oikmo.engine.scene.Scene;
import net.oikmo.engine.terrain.Terrain;
import net.oikmo.engine.textures.TerrainTexture;
import net.oikmo.engine.textures.TerrainTexturePack;
import net.oikmo.main.Main;
import net.oikmo.main.Main.GameState;

public class SceneManager {
	
	private static enum SceneName {
		Main,
		Roblox,
		Empty
	}
	
	private static Map<String, SceneName> scenes;
	
	private static Scene currentScene;
	
	private static TerrainTexturePack texturePack;
	private static TerrainTexture blendMap;
	
	private static String seed = "ballsack!";
	
	public static void init() {
		scenes = new HashMap<>();
		
		texturePack = new TerrainTexturePack("grass", "mud", "grassFlowers", "path");
		blendMap = new TerrainTexture(Loader.getInstance().loadTexture("terrain/blendMap"));
		
		scenes.put("main", SceneName.Main);
		scenes.put("roblox", SceneName.Roblox);
		scenes.put("empty", SceneName.Empty);
	}
	
	public static void loadScene(String input) {
		if(currentScene != null) {
			currentScene.cleanUp();
		}
		
		switch(scenes.get(input)){
		case Empty:
			currentScene = new EmptyScene();
			break;
		case Main:
			currentScene = new MainScene(seed, texturePack, blendMap);
			break;
		case Roblox:
			currentScene = new RobloxScene(seed, texturePack, blendMap);
			break;
		}
		
		currentScene.init();
		
		Main.terrainMap.clear();
		for(Terrain terrain : SceneManager.getCurrentScene().getTerrain()) {
			Main.terrainMap.put((int)terrain.getGridX() + " " + (int)terrain.getGridZ(), terrain);
		}
		
		if(Main.gameState == GameState.game) {
			if(!currentScene.getEntities().contains(Main.player)) {
				currentScene.addEntity(Main.player);
			}
		}
		
	}
	
	public static Scene getCurrentScene() {
		return currentScene;
	}
}
