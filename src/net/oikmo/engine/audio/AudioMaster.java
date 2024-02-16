package net.oikmo.engine.audio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

import net.oikmo.main.Main;

/**
 * Loads sounds, sets listener position
 * 
 * @author Oikmo
 */
public class AudioMaster {
	
	private static List<Integer> buffers = new ArrayList<>();
	
	public static void init() {
		try {
			AL.create();
		} catch(LWJGLException e) {
			Main.error("Could not initalize AudioMaster!", e);
		}
		setListenerData();
	}
	
	public static void setListenerData() {
		AL10.alListener3f(AL10.AL_POSITION, 0, 0, 0);
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
	}
	
	public static void setListenerData(float posX, float posY, float posZ, float velX, float velY, float velZ) {
		AL10.alListener3f(AL10.AL_POSITION, posX, posY, posZ);
		AL10.alListener3f(AL10.AL_VELOCITY, velX, velY, velZ);
	}
	
	
	public static int loadSound(String file) {
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
	
	public static Source loadSoundS(String file) {
		Source source = new Source();
		source.play(loadSound(file));
		source.stop();
		return source;
	}
	
	public static void cleanUp() {
		AL.destroy();
	}
	
}
