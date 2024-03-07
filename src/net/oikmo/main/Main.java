package net.oikmo.main;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JTextField;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import net.oikmo.engine.DisplayManager;
import net.oikmo.engine.Loader;
import net.oikmo.engine.audio.AudioMaster;
import net.oikmo.engine.gui.GuiScreen;
import net.oikmo.engine.gui.component.GuiText;
import net.oikmo.engine.gui.font.meshcreator.FontType;
import net.oikmo.engine.renderers.MasterRenderer;
import net.oikmo.main.entity.Camera;
import net.oikmo.main.entity.Player;
import net.oikmo.main.gui.GuiInGame;
import net.oikmo.main.gui.GuiPauseMenu;
import net.oikmo.main.scene.RobloxScene;
import net.oikmo.main.scene.SceneManager;
import net.oikmo.toolbox.Logger;
import net.oikmo.toolbox.Logger.LogLevel;
import net.oikmo.toolbox.error.PanelCrashReport;
import net.oikmo.toolbox.error.UnexpectedThrowable;
import net.oikmo.toolbox.os.EnumOS;
import net.oikmo.toolbox.os.EnumOSMappingHelper;

/**
 * Main class. Starts the engine. Plays the game.
 * 
 * @author Oikmo
 */
public class Main {
	public static int WIDTH = 800; //854
	public static int HEIGHT = 600; //480

	public static FontType font;
	
	public static Player player;
	public static GuiScreen currentScreen;

	public static String gameName = "PEPDOG-BLOX";
	public static String version = "a0.0.2";
	public static String gameVersion = gameName + " " + version;
	
	static Frame frame;
	
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

	/**
	 * Main method where the game starts.
	 * Handles initialisation of renderers and scenes and main game loop.
	 * 
	 * @author Oikmo
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String mapToLoad = "2008SwordFightonTheHeightsIV";
			Logger.log(LogLevel.INFO, "Selected: " + mapToLoad);
			
			DisplayManager.createDisplay();
			removeHSPIDERR();
			try {
				GameSettings.loadValues();
			} catch (IOException e) {
				Main.error("Could not load values from options", e);
			}
			AudioMaster.init();
			
			Loader loader = Loader.getInstance();		
			@SuppressWarnings("unused")
			MasterRenderer renderer = MasterRenderer.getInstance();
			
			SceneManager.init();
			SceneManager.loadScene("empty");
			
			PhysicsSystem.init();
			
			String fontType = "comic-sans";
			font = new FontType(loader.loadFontTexture(fontType),fontType);
			gameState = GameState.game;
			currentScreen = new GuiInGame();

			player = new Player(new Vector3f(0,0,0),new Vector3f(0,0,0), 1.75f);
			

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
			
			JFrame inputWindow = new JFrame();
			inputWindow.setLocation(0, 105);
			inputWindow.setSize(200, 100);
			JTextField input = new JTextField();
			inputWindow.add(input);
			
			input.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					RobloxScene scene = ((RobloxScene)SceneManager.getCurrentScene());
					scene.loadRoblox(e.getActionCommand());
					inputWindow.setVisible(false);
				}
			});
			
			SceneManager.loadScene("roblox");
			RobloxScene scener = ((RobloxScene)SceneManager.getCurrentScene());
			scener.loadRoblox(mapToLoad);
			Camera camera = new Camera(scener.getRandomSpawn(), player.getRotation());
			
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
				
				switch(gameState) {
				case game:
					
					if(Keyboard.isKeyDown(Keyboard.KEY_J)) {
						inputWindow.setVisible(true);
					}

					AudioMaster.setListenerData(camera.getPosition().x,camera.getPosition().y,camera.getPosition().z, 0, 0, 0);
					if(camera != player.getCamera()) {
						camera.flyCam();
					} else {
						player.update();
					}
					PhysicsSystem.update();
					break;
				case pausemenu:
					Mouse.setGrabbed(false);
					player.pause();
					break;
				}
				SceneManager.getCurrentScene().update(camera);

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

	private static long lastClick = 150;
	private static long coolDownTime = 150;
	/**
	 * Switches GUI based on the current {@code Main.GameState} state 
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
	 * @return {@code EnumOS}
	 */
	private static EnumOS getOS() {
		String rawOS = System.getProperty("os.name").toLowerCase();
		return rawOS.contains("win") ? EnumOS.windows : (rawOS.contains("mac") ? EnumOS.linux : (rawOS.contains("solaris") ? EnumOS.solaris : (rawOS.contains("sunos") ? EnumOS.unknown : (rawOS.contains("linux") ? EnumOS.linux : (rawOS.contains("unix") ? EnumOS.linux : EnumOS.unknown)))));
	}
	static PanelCrashReport report;
	
	/**
	 * Creates a frame with the error log embedded inside.
	 * 
	 * @param id (String)
	 * @param throwable (Throwable)
	 */
	public static void error(String id, Throwable throwable) {
		//Main.destroyGameButNoClose();
		if(frame == null) {
			frame = new JFrame();
		}
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
		
	}
}