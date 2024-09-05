package net.pepdog.engine;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import net.pepdog.main.Main;
import net.pepdog.toolbox.Logger;
import net.pepdog.toolbox.Logger.LogLevel;

public class ResourceLoader {
	private static Map<String, Integer> textures = new HashMap<>();
	private static Map<String, Texture> uiTextures = new HashMap<>();
	private static Map<String, URL> audioFiles = new HashMap<>();

	/**
	 * Loads file into program, if the file hasn't been loaded before then it will be added to a static list. If it has then it retrieves a current instance of the file from the static list.
	 * @param name - {@link String}
	 * @return {@link Integer}
	 */
	public static int loadTexture(String name) {
		if(textures.get(name) == null) {
			textures.put(name, Loader.loadTexture(name));
		}

		return textures.get(name);
	}

	/**
	 * Loads file into program, if the file hasn't been loaded before then it will be added to a static list. If it has then it retrieves a current instance of the file from the static list.
	 * @param file - {@link String}
	 * @return {@link URL}
	 */
	public static URL loadAudioFile(String file) {

		if(audioFiles.get(file) == null) {
			try {
				String path = Paths.get(Main.getResources() + file).toString();
				URL url = new File(path).toURI().toURL();
				audioFiles.put(file, url);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return audioFiles.get(file);
	}

	/**
	 * Uses the custom resources at .blockcraft
	 * @param file - {@link String}
	 * @return {@link URL}
	 */
	public static URL loadCustomAudioFile(String file) {
		if(audioFiles.get(file) == null) {
			audioFiles.put(file, Main.class.getResource(Main.getResources()+"/custom/music/" + file));
		}

		return audioFiles.get(file);
	}


	public static Texture loadUITexture(String name) {
		if(uiTextures.get(name) == null) {
			try {
				uiTextures.put(name, TextureLoader.getTexture("PNG",ResourceLoader.class.getResourceAsStream("/assets/textures/" + name + ".png")));
			} catch (IOException e) {
				Logger.log(LogLevel.WARN, "Texture (Slick) could not be loaded! (" + name + ")");
			}
		}
		
		return uiTextures.get(name);
	}
	
	public static void cleanUp() {
		for(Texture t : uiTextures.values()) {
			t.release();
		}
		uiTextures.clear();
		for(int id : textures.values()) {
			GL11.glDeleteTextures(id);
		}
		textures.clear();
		audioFiles.clear();
	}
}
