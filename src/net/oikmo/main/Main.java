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
 * <b><i>Main class. Starts the engine. Plays the game.</i></b>
 * 
 * @author <i>Oikmo</i>
 */
public class Main {
	static boolean doLoadingScreens = false;

	public static int WIDTH = 854;
	public static int HEIGHT = 480;

	public static FontType font;
	
	public static Player player;
	public static GuiScreen currentScreen;

	public static String gameName = "PEPDOG-BLOX";
	public static String version = "a0.0.2";
	public static String gameVersion = gameName + " " + version;
	
	static Frame frame;

	public enum GameState {
		game,
		pausemenu
	}
	public static GameState gameState;

	/**
	 * Main method where the game starts.
	 * Handles initialisation of renderers and scenes and main game loop.
	 * 
	 * @author <i>Oikmo</i>
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String mapToLoad = null;
			if(args.length > 0) {
				mapToLoad = args[0].trim();
			}
			if(mapToLoad == null) {
				mapToLoad = "2005StartPlace";
				Logger.log(LogLevel.INFO, "No map was chosen! Selecting: " + mapToLoad);
			} else {
				Logger.log(LogLevel.INFO, "Selected: " + mapToLoad);
			}
			
			DisplayManager.createDisplay();
			removeHSPIDERR();
			try {
				GameSettings.loadValues();
			} catch (IOException e) {
				Main.error("Could not load values from options", e);
			}
			AudioMaster.init();
			
			Throwable throwe = new Throwable("AAAAAAAAAAAAAAAAAAAAAAAA");
			Main.error("aaaa", throwe);
			
			Loader loader = Loader.getInstance();		
			@SuppressWarnings("unused")
			MasterRenderer renderer = MasterRenderer.getInstance();
			
			SceneManager.init();
			SceneManager.loadScene("empty");
			
			String fontType = "comic-sans";
			font = new FontType(loader.loadFontTexture(fontType),fontType);
			gameState = GameState.game;
			currentScreen = new GuiInGame();

			Camera camera = new Camera(new Vector3f(0,25,0), new Vector3f(0,45,0));

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
				}});
			
			
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
					if(player == null) {
						player = new Player("player", new Vector3f(0,0,0),new Vector3f(0,0,0), 1.75f);
						camera = null;
						camera = player.getCamera();
						SceneManager.getCurrentScene().addEntity(player);	    			
						SceneManager.loadScene("roblox");
						RobloxScene scener = ((RobloxScene)SceneManager.getCurrentScene());
						scener.loadRoblox(mapToLoad);
					}
					
					if(Keyboard.isKeyDown(Keyboard.KEY_J)) {
						inputWindow.setVisible(true);
					}

					AudioMaster.setListenerData(camera.getPosition().x,camera.getPosition().y,camera.getPosition().z, 0, 0, 0);
					player.update();
					
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

	public static void destroyGame() {
		GameSettings.saveValues();
		AudioMaster.cleanUp();
		MasterRenderer.getInstance().cleanUp();
		Logger.saveLog();
		DisplayManager.closeDisplay();
		frame = null;
		System.exit(0);
	}
	
	public static void destroyGameButNoClose() {
		GameSettings.saveValues();
		AudioMaster.cleanUp();
		Logger.saveLog();
		DisplayManager.closeDisplay();
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
		}
	}

	public static boolean isMoving() {
		return Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_D);
	}

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
	 * <li>Windows (win, {@code EnumOS.windows})</li>
	 * <li>Linux (linux, {@code EnumOS.linux})</li>
	 * <li>Unix (unix, {@code EnumOS.linux})</li>
	 * <li>Mac (mac, {@code EnumOS.linux})</li>
	 * <li>Solaris (solaris, {@code EnumOS.solaris})</li>
	 * <li>Sunos (sunos, {@code EnumOS.unknown})</li>
	 * <li>If it can find one ( {@code EnumOS.unknown})</li>
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
	 * @param id
	 * @param throwable
	 */
	public static void error(String id, Throwable throwable) {
		Main.destroyGameButNoClose();
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