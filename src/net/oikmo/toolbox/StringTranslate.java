package net.oikmo.toolbox;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import net.oikmo.main.Main;

public class StringTranslate {
	private static StringTranslate instance = new StringTranslate();
	private Properties translateTable = new Properties();

	private StringTranslate() {
		try {
			//this.translateTable.load(StringTranslate.class.getResourceAsStream("/lang/en_US.lang"));
			//System.out.println(new File(UNDECIDED.getUNDECIDEDDir().getAbsolutePath()+"/options.save"));
			this.translateTable.load(new FileInputStream(Main.getUNDECIDEDDir().getAbsolutePath()+"/options.save"));
			
		} catch (IOException var2) {
			var2.printStackTrace();
		}

	}

	public static StringTranslate getInstance() {
		return instance;
	}
	
	public void insertKey(String name, String value) {
		this.translateTable.setProperty(name, value);
		try {
			this.translateTable.store(new FileOutputStream(Main.getUNDECIDEDDir().getAbsolutePath()+"/options.save"), null);
		} catch (IOException e) {
			Main.error("failed to store key!", e);
		}
	}
	
	public String translateKey(String key) {
		return this.translateTable.getProperty(key, key);
	}
	
	public String getAudioSFXKey(String key) {
		return this.translateTable.getProperty("und-j:sfx."+key, key);
	}
	
	public String getAudioMusKey(String key) {
		return this.translateTable.getProperty("und-j:music."+key, key);
	}
	
	
	public String getAudioKey(String key) {
		return this.translateTable.getProperty("und-j:"+key, key);
	}
	
	public String translateKeyFormat(String key, Object toFormat) {
		String var3 = this.translateTable.getProperty(key, key);
		return String.format(var3, toFormat);
	}

	public String translateNamedKey(String key) {
		return this.translateTable.getProperty(key + ".name", "");
	}
}
