package net.pepdog.main;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import javax.swing.ImageIcon;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import net.pepdog.engine.DisplayManager;
import net.pepdog.engine.Entity;
import net.pepdog.engine.PhysicsSystem;
import net.pepdog.engine.audio.AudioMaster;
import net.pepdog.engine.audio.Source;
import net.pepdog.engine.gui.GuiScreen;
import net.pepdog.engine.lua.DataModel;
import net.pepdog.engine.models.RawModel;
import net.pepdog.engine.models.TexturedModel;
import net.pepdog.engine.renderers.MasterRenderer;
import net.pepdog.engine.textures.ModelTexture;
import net.pepdog.main.entity.Camera;
import net.pepdog.main.entity.Player;
import net.pepdog.main.gui.GuiInGame;
import net.pepdog.main.gui.GuiPauseMenu;
import net.pepdog.main.scene.RobloxScene;
import net.pepdog.main.scene.SceneManager;
import net.pepdog.toolbox.Logger;
import net.pepdog.toolbox.Logger.LogLevel;
import net.pepdog.toolbox.error.PanelCrashReport;
import net.pepdog.toolbox.error.UnexpectedThrowable;
import net.pepdog.toolbox.obj.OBJLoader;
import net.pepdog.toolbox.os.EnumOS;
import net.pepdog.toolbox.os.EnumOSMappingHelper;

/**
 * Main class. Starts the engine. Plays the game.
 * 
 * @author Oikmo
 */
public class Main {
	public static int WIDTH = 800; //854
	public static int HEIGHT = 600; //480
	
	public static Player player;
	public static Camera camera;
	public static GuiScreen currentScreen;

	public static String gameName = "PEPDOG";
	public static String version = "a0.0.3";
	public static String gameVersion = gameName + " " + version;
	
	public static Frame frame;
	
	/**
	 * Game state as to allow switching between paused or not paused
	 * 
	 * @author Oikmo
	 */
	public enum GameState {
		game,
		pausemenu
	}
	public static GameState gameState;
	static String mapToLoad = "2008ROBLOXHQ";
	
	/**
	 * Main method where the game starts.
	 * Handles initialisation of renderers and scenes and main game loop.
	 * 
	 * @author Oikmo
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		frame = new Frame();
		frame.setSize(200, 100);
		frame.setLocationRelativeTo(null);
		frame.addWindowListener(new WindowAdapter(){  
            public void windowClosing(WindowEvent e) {  
            	frame.dispose(); 
                System.exit(0);
            }  
        });
		URL iconURL = Main.class.getResource("/assets/icon.png");
		ImageIcon icon = new ImageIcon(iconURL);
		frame.setIconImage(icon.getImage());
		frame.setName("PepDog Map Loader");
		frame.setTitle("PepDog Map loader");
		
		System.setProperty("http.agent", "Chrome");
		String content = null;
		URLConnection connection = null;
		try {
			connection =  new URL("http://localhost/places/2005StartPlace.rbxl").openConnection();
			connection.addRequestProperty("User-Agent", "Mozilla");
			
			if(connection != null) {
				//System.out.println(connection.getRequestProperty("User-Agent"));
				Scanner scanner = new Scanner(connection.getInputStream());
				scanner.useDelimiter("\\Z");
				content = scanner.next();
				scanner.close();
			}
			
		} catch ( Exception ex ) {
			ex.printStackTrace();
		}
		
		GameLoop(content);
	}
	
	public static void GameLoop(String rbxl) {
		try {	
			Logger.log(LogLevel.INFO, "Selected: " + mapToLoad);
			
			DisplayManager.createDisplay();
			removeHSPIDERR();
			try {
				GameSettings.loadValues();
			} catch (IOException e) {
				Main.error("Could not load values from options", e);
			}
			AudioMaster.init();
			
			MasterRenderer.getInstance();
			
			SceneManager.init();
			SceneManager.loadScene("empty");
			
			PhysicsSystem.init();
			camera = new Camera(new Vector3f(), new Vector3f());
			
			String fontType = "comic-sans";
			
			gameState = GameState.game;
			currentScreen = new GuiInGame();
			
			
			DataModel dm = new DataModel();
			
			SceneManager.loadScene("roblox");
			RobloxScene scener = ((RobloxScene)SceneManager.getCurrentScene());
			scener.loadRobloxFromContent(rbxl);
			camera.setPosition(scener.getRandomSpawn());
			
			RawModel model = OBJLoader.loadOBJ("cube");
			TexturedModel texturedModel = new TexturedModel(model, new ModelTexture(dm.load("rbxassetid://textures/face.png")));
			Entity entity = new Entity(texturedModel, new Vector3f(0,10,0), new Vector3f(), 1f);
			scener.addEntity(entity);
			
			Source source = new Source();
			
			source.play(dm.load("rbxassetid://sounds/bass.wav"));
			
			while(!Display.isCloseRequested()) {
				handleGUI();
				
				long maxMem = Runtime.getRuntime().maxMemory();
				long totalMem = Runtime.getRuntime().totalMemory();
				long freeMem = Runtime.getRuntime().freeMemory();
				long usedMem = totalMem - freeMem;

				/*useMem.setTextString("Used memory: " + (usedMem * 100L) / maxMem +"% (" + usedMem / 1024L / 1024L + "MB) of " + maxMem / 1024L / 1024L + "MB");
				allocMem.setTextString("Allocated memory: " + (totalMem * 100L) / maxMem +"% (" + totalMem / 1024L / 1024L + "MB)");

				fps.setTextString("fps: " + Integer.toString(DisplayManager.getFPSCount()));
				state.setTextString("gameState: " + gameState.toString());*/
				
				if(currentScreen != null) {
					Mouse.setGrabbed(currentScreen.isLockInput());
				}
				
				switch(gameState) {
				case game:
					//partsCount.setTextString("parts: " + scener.getParts().size());
					AudioMaster.setListenerData(camera.getPosition().x,camera.getPosition().y,camera.getPosition().z, 0, 0, 0);
					camera.flyCam();
					/*if(camera != player.getCamera()) {
						
					} else {
						player.update();
					}*/
					if(PhysicsSystem.getWorld() != null) {
						PhysicsSystem.update();
					}
					
					break;
				case pausemenu:
					Mouse.setGrabbed(false);
					//player.pause();
					break;
				}
				SceneManager.update(camera);

				DisplayManager.updateDisplay();
			}
			destroyGame();
		} catch (RuntimeException e) {
			Main.error("Runtime Error", e);
		}
	}
	
	/**
	 * Cleans up and saves config before closing display and finally on {@code System.exit(0)}
	 */
	public static void destroyGame() {
		GameSettings.saveValues();
		AudioMaster.cleanUp();
		MasterRenderer.getInstance().cleanUp();
		Logger.saveLog();
		DisplayManager.closeDisplay();
		System.exit(0);
	}
	
	/**
	 * Same as destroyGame() but instead doesn't close on {@code System.exit(0)}
	 * @see Main#destroyGame()
	 */
	public static void destroyGameButNoClose() {
		GameSettings.saveValues();
		AudioMaster.cleanUp();
		Logger.saveLog();
		MasterRenderer.getInstance().cleanUp();
		DisplayManager.closeDisplay();
	}
	
	/**
	 * Searches the directory where the program is being ran 
	 * then removes any file following "hs_err_pid"
	 */
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

	private static boolean lockInGui = false;
	/**
	 * Switches GUI based on the current {@code Main.GameState} state 
	 */
	private static void handleGUI() {
		if(currentScreen != null) { 
			currentScreen.update();
			if (!lockInGui) {
				
				if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
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
					lockInGui = true;
				}
			} else {
				if(!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
					lockInGui = false;
				}
			}
		} 
	}
	
	/**
	 * Returns true if one of the WASD keys is pressed.
	 * @return boolean
	 */
	public static boolean isMoving() {
		return Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_D);
	}
	
	/**
	 * Retrieves data directory of .pepdog/ using {@code Main.getAppDir(String)}
	 * @return Directory (File)
	 */
	public static File getPEPDOGDir() {
		File pepdogDir = getAppDir("pepdog");
		return pepdogDir;
	}
	
	/**
	 * Uses {@code Main.getOS} to locate an APPDATA directory in the system.
	 * Then it creates a new directory based on the given name e.g <b>.name/</b>
	 * 
	 * @param name (String)
	 * @return Directory (File)
	 */
	public static File getAppDir(String name) {
		String userDir = System.getProperty("user.home", ".");
		File folder;
		switch(EnumOSMappingHelper.os[getOS().ordinal()]) {
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
	
	/**
	 * Gets the os type of the user using the java property "os.name".
	 * <br>
	 * OS' it tries to find.
	 * <ul>
	 * <li>Windows (win,  {@code {@link EnumOS#windows})</li>
	 * <li>Linux (linux, {@code {@link EnumOS#linux})</li>
	 * <li>Unix (unix, {@code {@link EnumOS#linux})</li>
	 * <li>Mac (mac, {@code {@link EnumOS#macos})</li>
	 * <li>Solaris (solaris, {@code {@link EnumOS#solaris})</li>
	 * <li>Sunos (sunos, {@code {@link EnumOS#unknown})</li>
	 * <li>If it can find one ({@code {@link EnumOS#unknown})</li>
	 * </ul>
	 * @return {@link EnumOS}
	 */
	private static EnumOS getOS() {
		String rawOS = System.getProperty("os.name").toLowerCase();
		return rawOS.contains("win") ? EnumOS.windows : (rawOS.contains("mac") ? EnumOS.linux : (rawOS.contains("solaris") ? EnumOS.solaris : (rawOS.contains("sunos") ? EnumOS.unknown : (rawOS.contains("linux") ? EnumOS.linux : (rawOS.contains("unix") ? EnumOS.linux : EnumOS.unknown)))));
	}
	
	static PanelCrashReport report;
	static boolean balls = false;
	/**
	 * Creates a frame with the error log embedded inside.
	 * 
	 * @param id (String)
	 * @param throwable (Throwable)
	 */
	public static void error(String id, Throwable throwable) {
		if(!balls) {
			frame.removeAll();
			frame.setSize(Main.WIDTH, HEIGHT);
			frame.setVisible(true);
			UnexpectedThrowable unexpectedThrowable = new UnexpectedThrowable(id, throwable);
			if(report == null) {
				report = new PanelCrashReport(unexpectedThrowable);
			} else {
				report.set(unexpectedThrowable);
			}
			frame.add(report, "Center");
			frame.validate();
			balls = true;
		}
		
		
	}
}