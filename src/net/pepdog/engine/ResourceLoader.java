package net.pepdog.engine;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import net.pepdog.toolbox.Logger;
import net.pepdog.toolbox.Logger.LogLevel;

public class ResourceLoader {
	private static Map<String, Integer> textures = new HashMap<>();
	private static Map<String, Texture> uiTextures = new HashMap<>();
	
	/**
	 * Loads file into program, if the file hasn't been loaded before then it will be added to a static list. If it has then it retrieves a current instance of the file from the static list.
	 * @param name - {@link String}
	 * @return {@link Integer}
	 */
	public static int loadTexture(String name) {
		if(textures.get(name) == null) {
			textures.put(name, Loader.loadTexture("/assets/textures/"+name));
		}

		return textures.get(name);
	}
	
	/**
	 * Loads file into program, if the file hasn't been loaded before then it will be added to a static list. If it has then it retrieves a current instance of the file from the static list.
	 * @param name - {@link String}
	 * @return {@link Integer}
	 */
	public static int loadContentTexture(String name) {
		if(textures.get(name) == null) {
			textures.put(name, Loader.loadTexture("/content/textures/"+name));
		}

		return textures.get(name);
	}


	/**
	 * Loads file into program, if the file hasn't been loaded before then it will be added to a static list. If it has then it retrieves a current instance of the file from the static list.
	 * <br>
	 * This variation uses Slick to load textures as to use it for {@link Gui}.
	 * @param name - {@link String}
	 * @return {@link Integer}
	 */
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
}
