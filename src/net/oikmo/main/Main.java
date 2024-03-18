package net.oikmo.main;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import net.oikmo.engine.DisplayManager;
import net.oikmo.engine.Entity;
import net.oikmo.engine.audio.AudioMaster;
import net.oikmo.engine.audio.Source;
import net.oikmo.engine.gui.GuiScreen;
import net.oikmo.engine.gui.component.GuiText;
import net.oikmo.engine.gui.font.meshcreator.FontType;
import net.oikmo.engine.lua.DataModel;
import net.oikmo.engine.models.RawModel;
import net.oikmo.engine.models.TexturedModel;
import net.oikmo.engine.renderers.MasterRenderer;
import net.oikmo.engine.textures.ModelTexture;
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
import net.oikmo.toolbox.obj.OBJLoader;
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
	static List<String> maps = new ArrayList<>();
	
	/**
	 * Main method where the game starts.
	 * Handles initialisation of renderers and scenes and main game loop.
	 * 
	 * @author Oikmo
	 * @param args
	 */
	public static void main(String[] args) {
		maps.add("2005PirateShip");
		maps.add("2005StartPlace");
		maps.add("2006Crossroads");
		maps.add("2008ROBLOXHQ");
		maps.add("2008SwordFightonTheHeightsIV");
		maps.add("TESTColours");
		maps.add("TESTShapes");
		
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
		
		JComboBox<String> box = new JComboBox<>();
		for(String map : maps) {		
			box.addItem(map);
		}
		
		JPanel inputWindow = new JPanel(new GridLayout(2, 1));
		inputWindow.add(new JLabel("Choose the map you want!"));
		inputWindow.add(box);
		frame.add(inputWindow);
		frame.setVisible(true);
		box.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.removeAll();
				frame.setVisible(false);
				mapToLoad = (String) box.getSelectedItem();
				GameLoop();
			}
		});
		frame.validate();
	}
	
	public static void GameLoop() {
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
			Camera camera = new Camera(new Vector3f(), new Vector3f());
			
			String fontType = "comic-sans";
			font = new FontType(fontType);
			gameState = GameState.game;
			currentScreen = new GuiInGame();

			//player = new Player(new Vector3f(0,0,0),new Vector3f(0,0,0), 1.75f);
			
			float offsetX = 0.01f;
			float offsetY = 0.05f;
			
			GuiText version = new GuiText(gameVersion, 0.8f, font, new Vector2f(offsetX, offsetY), 1, false, false);
			offsetY += 0.0175f;
			GuiText fps = new GuiText(Integer.toString(DisplayManager.getFPSCount()), 0.8f, font, new Vector2f(offsetX, offsetY), 1, false, false);
			offsetY += 0.0175f;
			GuiText state = new GuiText(gameState.toString(), 0.8f, font, new Vector2f(offsetX, offsetY), 1, false, false);
			offsetY += 0.0175f;
			GuiText useMem = new GuiText("Used memory:", 0.8f, font, new Vector2f(offsetX, offsetY), 1, false, false);
			offsetY += 0.0175f;
			GuiText allocMem = new GuiText("Allocated memory:", 0.8f, font, new Vector2f(offsetX, offsetY), 1, false, false);
			offsetY += 0.0175f;
			GuiText partsCount = new GuiText("parts:", 0.8f, font, new Vector2f(offsetX, offsetY), 1, false, false);
			
			version.setColour(1, 1, 1);
			fps.setColour(1, 1, 1);
			state.setColour(1, 1, 1);
			useMem.setColour(1, 1, 1);
			allocMem.setColour(1, 1, 1);
			partsCount.setColour(1, 1, 1);
			
			version.setOutlineColour(0,0,0);
			version.setEdge(0.3f);
			fps.setOutlineColour(0,0,0);
			fps.setEdge(0.3f);
			state.setOutlineColour(0,0,0);
			state.setEdge(0.3f);
			useMem.setOutlineColour(0,0,0);
			useMem.setEdge(0.3f);
			allocMem.setOutlineColour(0,0,0);
			allocMem.setEdge(0.3f);
			partsCount.setOutlineColour(0,0,0);
			partsCount.setEdge(0.3f);
			
			
			DataModel dm = new DataModel();
			
			SceneManager.loadScene("roblox");
			RobloxScene scener = ((RobloxScene)SceneManager.getCurrentScene());
			scener.loadRoblox(mapToLoad);
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

				useMem.setTextString("Used memory: " + (usedMem * 100L) / maxMem +"% (" + usedMem / 1024L / 1024L + "MB) of " + maxMem / 1024L / 1024L + "MB");
				allocMem.setTextString("Allocated memory: " + (totalMem * 100L) / maxMem +"% (" + totalMem / 1024L / 1024L + "MB)");

				fps.setTextString("fps: " + Integer.toString(DisplayManager.getFPSCount()));
				state.setTextString("gameState: " + gameState.toString());
				
				switch(gameState) {
				case game:
					partsCount.setTextString("parts: " + scener.getParts().size());
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
	
	public static boolean isMapValid(String mapName) {
		boolean load = false;
		for(String map : maps) {		
			if(map.contentEquals(mapName)) {
				load = true;
			}
		}
		return load;
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
	/**
	 * Creates a frame with the error log embedded inside.
	 * 
	 * @param id (String)
	 * @param throwable (Throwable)
	 */
	public static void error(String id, Throwable throwable) {
		Main.destroyGameButNoClose();
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
		
	}
}