package net.oikmo.engine.audio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

import net.oikmo.main.Main;
import net.oikmo.toolbox.Toolbox;

/**
 * Loads sounds, sets listener position. Anything audio.
 * 
 * @author Oikmo
 */
public class AudioMaster {
	
	private static List<Integer> buffers = new ArrayList<>();
	
	private static Map<String, Integer> audio = new HashMap<>();
	
	/**
	 * Start the god damn thing
	 */
	public static void init() {
		try {
			AL.create();
		} catch(LWJGLException e) {
			Main.error("Could not initalize AudioMaster!", e);
		}
		
		audioRefresh("assets/audio");
		
		setListenerData();
	}
	
	/**
	 * If you really need to, set the audio path to something else.<br>
	 * This searches the given path and loads it in a HashMap to be used in AudioMaster.getSound().
	 * @param path
	 */
	public static void audioRefresh(String path) {
		List<String> audioFiles = null;
		audio = new HashMap<>(); // not letting you keep data! >:D
		try {
			audioFiles = Toolbox.getResourceFiles(path, ".wav");
		} catch (IOException e) {
			Main.error("Failed to load audio assets", e);
		}
		
		for(String audio : audioFiles) {
			AudioMaster.audio.put(audio, AudioMaster.loadSound(audio));
		}
	}
	
	public static int getSound(String name) {
		return audio.get(name);
	}
	
	public static void setListenerData() {
		AL10.alListener3f(AL10.AL_POSITION, 0, 0, 0);
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
	}
	
	public static void setListenerData(float posX, float posY, float posZ, float velX, float velY, float velZ) {
		AL10.alListener3f(AL10.AL_POSITION, posX, posY, posZ);
		AL10.alListener3f(AL10.AL_VELOCITY, velX, velY, velZ);
	}
	
	/**
	 * 
	 * Loads wav audio file and returns int for loading.
	 * 
	 * @param file
	 * @return Audio (int)
	 */
	private static int loadSound(String file) {
		int buffer = AL10.alGenBuffers();
		buffers.add(buffer);
		ClassLoader load = AudioMaster.class.getClassLoader();
		String path = "assets/audio/" + file + ".wav";
		byte[] stream = null;
		try {
			stream = IOUtils.toByteArray(load.getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		WaveData waveFile = WaveData.create(stream);
		AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
		waveFile.dispose();
		return buffer;
	}
	
	/**
	 * Cleans up any memory used for audio.
	 */
	public static void cleanUp() {
		AL.destroy();
	}
	
}
