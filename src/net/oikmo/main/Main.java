package net.oikmo.main;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import net.oikmo.engine.DisplayManager;
import net.oikmo.engine.Loader;
import net.oikmo.engine.audio.AudioMaster;
import net.oikmo.engine.audio.Source;
import net.oikmo.engine.gui.GuiScreen;
import net.oikmo.engine.gui.component.GuiText;
import net.oikmo.engine.gui.font.fontMeshCreator.FontType;
import net.oikmo.engine.renderers.MasterRenderer;
import net.oikmo.engine.terrain.Terrain;
import net.oikmo.main.entity.Camera;
import net.oikmo.main.entity.Player;
import net.oikmo.main.gui.GuiInGame;
import net.oikmo.main.gui.GuiMainMenu;
import net.oikmo.main.gui.GuiPauseMenu;
import net.oikmo.main.scene.SceneManager;
import net.oikmo.toolbox.Logger;
import net.oikmo.toolbox.error.PanelCrashReport;
import net.oikmo.toolbox.error.UnexpectedThrowable;
import net.oikmo.toolbox.os.EnumOS;
import net.oikmo.toolbox.os.EnumOSMappingHelper;

/**
 * <b><i>Main class. Starts the engine. Plays the game.</i></b>
 * 
 * @author <i>Oikmo</i>
 */
public class Main {
	static boolean doLoadingScreens = false;
	
	public static int WIDTH = 854;
	public static int HEIGHT = 480;
	
	public static Map<String, Terrain> terrainMap = new HashMap<>();
	
	public static FontType font;
	
	public static Player player;
	public static GuiScreen currentScreen;
	
	public static String gameName = "RBXL LOADER";
	public static String version = "a0.0.1";
	public static String gameVersion = gameName + " " + version;
	static Frame frame;
	
	public static int textureSponge;
	
	public enum GameState {
		mainmenu,
		game,
		pausemenu
	}
	public static GameState gameState;
	
	/**
	 * <b><i>Main method where the game starts</i></b>
	 * 
	 * @author <i>Oikmo</i>
	 * @param args
	 */
	public static void main(String[] args) throws IOException {		
		removeHSPIDERR();
		DisplayManager.createDisplay();
		
		GameSettings.loadValues();
		AudioMaster.init();
		textureSponge = Loader.getInstance().loadTexture("models/base");
		
		Loader loader = Loader.getInstance();		
		MasterRenderer renderer = MasterRenderer.getInstance();
		
		SceneManager.init();
		SceneManager.loadScene("empty");
		
		String fontType = "vcr";
		font = new FontType(loader.loadFontTexture(fontType),fontType);
		gameState = GameState.mainmenu;
		currentScreen = new GuiMainMenu();
		
		Camera camera = new Camera(new Vector3f(0,25,0), new Vector3f(0,45,0));
		
		Source music = new Source();
		int musicUNDECIDED= AudioMaster.loadSound("undecided");
		music.setLooping(true);
		music.setVolume(GameSettings.globalVolume);
		
		//Duration end = new Duration(music.getEndTime());
		//System.out.println(end.getMinutes() + ":" + end.getSeconds());
		
		float vol = GameSettings.globalVolume-0.2f;
		
		GuiText version = new GuiText(gameVersion, 0.8f, font, new Vector2f(0,0), 1, false, false);
		GuiText fps = new GuiText(Integer.toString(DisplayManager.getFPSCount()), 0.8f, font, new Vector2f(0,0.0175f), 1, false, false);
		GuiText state = new GuiText(gameState.toString(), 0.8f, font, new Vector2f(0,0.035f), 1, false, false);
		GuiText scene = new GuiText(SceneManager.getCurrentScene().getClass().getSimpleName().replace("Scene",""), 0.8f, font, new Vector2f(0,0.0525f), 1, false, false);
		GuiText useMem = new GuiText("Used memory:", 0.8f, font, new Vector2f(0,0.07f), 1, false, false);
		GuiText allocMem = new GuiText("Allocated memory:", 0.8f, font, new Vector2f(0,0.0875f), 1, false, false);
		
		version.setColour(1, 1, 1);
		fps.setColour(1, 1, 1);
		state.setColour(1, 1, 1);
		scene.setColour(1, 1, 1);
		useMem.setColour(1, 1, 1);
		allocMem.setColour(1, 1, 1);
		
		boolean isMusicMain = false;
		
		while(!Display.isCloseRequested()) {
			handleGUI();
			
			long maxMem = Runtime.getRuntime().maxMemory();
			long totalMem = Runtime.getRuntime().totalMemory();
			long freeMem = Runtime.getRuntime().freeMemory();
			long usedMem = totalMem - freeMem;
			
			useMem.setTextString("Used memory: " + (usedMem * 100L) / maxMem +"% (" + usedMem / 1024L / 1024L + "MB) of " + maxMem / 1024L / 1024L + "MB");
			allocMem.setTextString("Allocated memory: " + (totalMem * 100L) / maxMem +"% (" + totalMem / 1024L / 1024L + "MB)");
			
			fps.setTextString("fps: " + Integer.toString(DisplayManager.getFPSCount()));
			state.setTextString("gameState: " + gameState.toString());
			scene.setTextString("scene: " + SceneManager.getCurrentScene().getClass().getSimpleName().replace("Scene","").toLowerCase());
			
			vol = GameSettings.globalVolume-0.2f;
			
			if(vol < 0) { vol = 0; }
			if(music.getVolume() != vol) {
				music.setVolume(vol);
			}
			
			/* ****** TESTING ****** */
			if(Keyboard.isKeyDown(Keyboard.KEY_R)) {
				SceneManager.loadScene("test");
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_T)) {
				SceneManager.loadScene("main");
			}
			/* ********************* */
			
			switch(gameState) {
	    	case mainmenu:
	    		if(player != null) {
	    			SceneManager.getCurrentScene().getEntities().remove(player);
	    			player = null;
	    			camera = null;
	    			camera = new Camera(new Vector3f(0, 25, 0), new Vector3f(0,45,0));
	    			renderer.createShadowMap(camera);
	    			Mouse.setGrabbed(false);
	    			SceneManager.loadScene("empty");
	    		}
	    		if(!isMusicMain) {
	    			renderer.createShadowMap(camera);
	    			music.stop();
	    			music.play(musicUNDECIDED);
	    			isMusicMain = true;
	    		}
	    		
	    		camera.increaseRotation(0, (15f*DisplayManager.getFrameTimeSeconds()), 0);
				break;
	    	case game:
	    		if(player == null) {
	    			player = new Player("spartan", new Vector3f(0,0,0),new Vector3f(0,0,0), 0.15f);
	    			camera = null;
	    			camera = player.getCamera();
	    			renderer.createShadowMap(camera);
	    			SceneManager.getCurrentScene().addEntity(player);	    			
	    			SceneManager.loadScene("roblox");
	    		}
	    		if(isMusicMain) {
	    			renderer.createShadowMap(camera);
	    			music.stop();
	    			isMusicMain = false;
	    		}
	    		
	    		AudioMaster.setListenerData(camera.getPosition().x,camera.getPosition().y,camera.getPosition().z, 0, 0, 0);
				player.update(getTerrainFromPosition(player.getCoords()));
	    		break;
			case pausemenu:
				Mouse.setGrabbed(false);
				player.pause();
				break;
			}
			SceneManager.getCurrentScene().update(camera);
			
			DisplayManager.updateDisplay();
		}
		music.delete();
		destroyGame();
	}
	
	public static void destroyGame() {
		GameSettings.saveValues();
		AudioMaster.cleanUp();
		MasterRenderer.getInstance().cleanUp();
		Logger.saveLog();
		DisplayManager.closeDisplay();
		frame = null;
		
		System.exit(0);
		
	}
	private static void removeHSPIDERR() {
		File path = new File(".");
		String[] files = path.list();
		for(int i = 0; i < files.length; i++) {
			if(files[i].contains("hs_err_pid")) {
				File file = new File(path.getAbsoluteFile() + "\\" + files[i]);
				file.delete();
			}
		}
	}
	
	public static Terrain getTerrainFromPosition(Vector2f position) { 
		int gridX = (int) (position.x / Terrain.SIZE);
		int gridZ = (int) (position.y / Terrain.SIZE);
		if(position.x < 0) {
			gridX = (int) (position.x / Terrain.SIZE) - 1;
		}
		if(position.y < 0) {
			gridZ =(int) (position.y/ Terrain.SIZE) - 1;
		}
		return terrainMap.get(gridX + " " + gridZ);
	}	
	public static Terrain getTerrainFromPosition(float x, float z) { 
		int gridX = (int) (x / Terrain.SIZE);
		int gridZ = (int) (z / Terrain.SIZE);
		if(x < 0) {
			gridX = (int) (x / Terrain.SIZE) - 1;
		}
		if(z < 0) {
			gridZ =(int) (z/ Terrain.SIZE) - 1;
		}
		return terrainMap.get(gridX + " " + gridZ);
	}
	public static float getHeightFromPosition(float worldX, float worldZ) {
		Terrain terrain = getTerrainFromPosition(worldX, worldZ);
		if(terrain == null) {
			return 0;
		}
		return terrain.getHeightOfTerrain(worldX, worldZ);
	}

	private static long lastClick = 150;
	private static long coolDownTime = 150;
	/**
	 * Switches from one GUI to another.
	 */
	private static void handleGUI() {
		if(currentScreen != null) { 
			currentScreen.update();
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				long timeNow = System.currentTimeMillis();
			    long time = timeNow - lastClick;
			    if (time < 0 || time > coolDownTime) {
			    	lastClick = timeNow;
			    	
			    	if(gameState == GameState.game) {
			    		currentScreen.prepareCleanUp();
		    			Main.currentScreen = null;
		    			gameState = GameState.pausemenu;
		    		} else if(gameState == GameState.pausemenu){
		    			if(!currentScreen.isUnableToExit()) {
		    				currentScreen.prepareCleanUp();
		    				gameState = GameState.game;
		    			}
		    			
		    		}
			    	switch(gameState) {
			    	case game:
			    		currentScreen = new GuiInGame();
			    		break;
					case pausemenu:
						currentScreen = new GuiPauseMenu();
						break;
					default:
						break;
			    	}
			    }
			}
		} else {
			
			
		}
		
	}
	
	public static void sleep(long time) {
		try { Thread.sleep(time); } catch (InterruptedException e) { Main.error("Couldn't sleep thread!", e); }
	}

	public static boolean isMoving() {
		return Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_D);
	}
	
	public static File getUNDECIDEDDir() {
		File notFoundDir = getAppDir("undecided");
		return notFoundDir;
	}
	public static File getAppDir(String name) {
		String userDir = System.getProperty("user.home", ".");
		File folder;
		switch(EnumOSMappingHelper.os[getOs().ordinal()]) {
		case 1:
		case 2:
			folder = new File(userDir, '.' + name + '/');
			break;
		case 3:
			String appdataLocation = System.getenv("APPDATA");
			if(appdataLocation != null) {
				folder = new File(appdataLocation, "." + name + '/');
			} else {
				folder = new File(userDir, '.' + name + '/');
			}
			break;
		case 4:
			folder = new File(userDir, "Library/Application Support/" + name);
			break;
		default:
			folder = new File(userDir, name + '/');
		}

		if(!folder.exists() && !folder.mkdirs()) {
			throw new RuntimeException("The working directory could not be created: " + folder);
		} else {
			return folder;
		}
	}
	private static EnumOS getOs() {
		String rawOS = System.getProperty("os.name").toLowerCase();
		return rawOS.contains("win") ? EnumOS.windows : (rawOS.contains("mac") ? EnumOS.linux : (rawOS.contains("solaris") ? EnumOS.solaris : (rawOS.contains("sunos") ? EnumOS.unknown : (rawOS.contains("linux") ? EnumOS.linux : (rawOS.contains("unix") ? EnumOS.linux : EnumOS.unknown)))));
	}
	
	public static void error(String id, Throwable throwable) {
		frame.setVisible(true);
		UnexpectedThrowable unexpectedThrowable = new UnexpectedThrowable(id, throwable);
		frame.removeAll();
		frame.add(new PanelCrashReport(unexpectedThrowable), "Center");
		frame.validate();
	}
}