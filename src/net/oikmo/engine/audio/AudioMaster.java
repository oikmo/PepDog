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
		
		setListenerData();
	}
	
	/**
	 * Basically, loads a sound if it isn't in the audio table. if so then return the audio from table instead of reloading it.
	 * @param name
	 */
	public static int getSound(String name) {
		if(audio.get(name) == null) {
			audio.put(name, loadSound(name));
		}
		return audio.get(name);
	}
	
	/**
	 * Sets the data of listener to nil.
	 * <br> The data that is set are Position of listener and Velocity of listener.
	 */
	public static void setListenerData() {
		AL10.alListener3f(AL10.AL_POSITION, 0, 0, 0);
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
	}
	/**
	 * Sets the data of listener.
	 * <br> The data that is set are Position of listener and Velocity of listener.
	 * 
	 * @param posX (float)
	 * @param posY (float)
	 * @param posZ (float)
	 * @param velX (float)
	 * @param velY (float)
	 * @param velZ (float)
	 */
	public static void setListenerData(float posX, float posY, float posZ, float velX, float velY, float velZ) {
		AL10.alListener3f(AL10.AL_POSITION, posX, posY, posZ);
		AL10.alListener3f(AL10.AL_VELOCITY, velX, velY, velZ);
	}
	
	/**
	 * Loads wav audio file and returns int for loading.
	 * 
	 * @param audioName (String)
	 * @return soundbyte (int)
	 */
	private static int loadSound(String audioName) {
		int buffer = AL10.alGenBuffers();
		buffers.add(buffer);
		ClassLoader load = AudioMaster.class.getClassLoader();
		String path = "assets/audio/" + audioName + ".wav";
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
