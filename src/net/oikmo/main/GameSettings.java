package net.oikmo.main;

import java.io.File;
import java.io.IOException;

import net.oikmo.toolbox.Logger;
import net.oikmo.toolbox.Logger.LogLevel;
import net.oikmo.toolbox.StringTranslate;

public class GameSettings {
	
	public static final float GRAVITY = -40;
	public static final float SENSITIVITY = 0.3f;
	
	public static float globalVolume = 0.5f;
	public static boolean postProcess = false;
	
	public static void loadValues() throws IOException {
		File save =  new File(Main.getUNDECIDEDDir().getPath()+"/options.save");
		if(!save.exists()) {
			save.createNewFile();
			StringTranslate st = StringTranslate.getInstance();
			st.insertKey("graphics.postProcess", Boolean.toString(postProcess));
		} else {
			StringTranslate st = StringTranslate.getInstance();
			globalVolume = Float.parseFloat(st.translateKey("audio.globalVolume"));
			postProcess = Boolean.parseBoolean(st.translateKey("graphics.postProcess"));
		}
	}
	
	public static void saveValues() {
		Logger.log(LogLevel.INFO, "honey, im saving the config!");
		File save =  new File(Main.getUNDECIDEDDir().getPath()+"/options.save");
		if(!save.exists()) {
			try {
				save.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		StringTranslate st = StringTranslate.getInstance();
		st.insertKey("audio.globalVolume", Float.toString(globalVolume));
		st.insertKey("graphics.postProcess", Boolean.toString(postProcess));
	}
	//store from player and load values from predefined file
	
}
