package net.pepdog.engine.audio;

import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

/**
 * A sound player, acts as speakers essentially
 * 
 * @author Oikmo
 */
public class Source {
	
	private int sourceID;
	private float volume;
	private int bufferID;
	
	/**
	 * Creates source.
	 */
	public Source() {
		sourceID = AL10.alGenSources();
	}
	
	/** 
	 * <b>Models</b>
	 * <ul><li>AL_EXPONENT_DISTANCE_CLAMPED</li>
	 * <li>AL_INVERSE_DISTANCE_CLAMPED</li>
	 * <li>AL_LINEAR_DISTANCE_CLAMPED</li>
	 * </ul>
	 * 
	 * Constructor for Source
	 * <br>
	 * @param rolloff (float) - The fade of the audio if the audio is heard past its max distance.
	 * @param reference (float) - **To actually know**
	 * @param maxDist (float) - How far the audio can be heard from it's position.
	 * @param model (AL10, int) - What model to use.
	 */
	public Source(float rolloff, float reference, float maxDist, int model) {
		sourceID = AL10.alGenSources();
		AL10.alSourcef(sourceID, AL10.AL_ROLLOFF_FACTOR, rolloff);
		AL10.alSourcef(sourceID, AL10.AL_REFERENCE_DISTANCE, reference);
		AL10.alSourcef(sourceID, AL10.AL_MAX_DISTANCE, maxDist);
		AL10.alDistanceModel(model);
	}
	
	/**
	 * Returns where the source is currently at inside the audio.<br>
	 * Calls {@link AL10#alGetSource()} with {@link AL11#AL_SEC_OFFSET}
	 * @return time (float)
	 */
	public float getTime() {
		return AL10.alGetSourcef(sourceID, AL11.AL_SEC_OFFSET);
	}
	
	/**
	 * Intialises the buffer and plays the given.
	 * 
	 * @param buffer (int) - sound byte (use {@link AudioMaster#getSound(String)} to retrieve it)
	 */
	public void play(int buffer) {
		stop();
		bufferID = buffer;
		AL10.alSourcei(sourceID, AL10.AL_BUFFER, buffer);
		continuePlaying();
	}
	
	/**
	 * Returns the length of the buffer by retrieving the size (in bytes),
	 * the amount of channels, bits and frequency. This is all factorised into
	 * {@code lengthInSamples / frequency}
	 * giving us the length (end time) of the buffer.
	 * 
	 * @return length (float)
	 */
	public float getEndTime() {
		int sizeInBytes = AL10.alGetBufferi(bufferID, AL10.AL_SIZE);
		int channels = AL10.alGetBufferi(bufferID, AL10.AL_CHANNELS);
		int bits = AL10.alGetBufferi(bufferID, AL10.AL_BITS);
		int frequency = AL10.alGetBufferi(bufferID, AL10.AL_FREQUENCY);
		float lengthInSamples = sizeInBytes * 8 / (channels * bits);
		return (float)lengthInSamples / (float)frequency;
	}
	/**
	 * Returns if the source is playing.
	 * @return playing (boolean)
	 */
	public boolean isPlaying() {
		return AL10.alGetSourcei(sourceID, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}
	/**
	 * Set looping audio or not.
	 * @param loop (boolean)
	 */
	public void setLooping(boolean loop) {
		AL10.alSourcei(sourceID, AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
	}
	/** Pauses the buffer and can be resumed.
	 */
	public void pause() {
		AL10.alSourcePause(sourceID);
	}
	/**
	 * Resume function e.g if the source is paused.
	 */
	public void continuePlaying() {
		AL10.alSourcePlay(sourceID);
	}
	/**
	 * Stops the buffer.
	 */
	public void stop() {
		AL10.alSourceStop(sourceID);
	}
	/**
	 * Basically a clean up function. Stops the source and removes it from memory.
	 */
	public void delete() {
		stop();
		AL10.alDeleteSources(sourceID);
	}
	
	/**
	 * Sets the speed of travel of sound. <br>
	 * Can replicate the doppler effect.
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setVelocity(float x, float y, float z) {
		AL10.alSource3f(sourceID, AL10.AL_POSITION, x, y, z);
	}
	
	/**
	 * Sets the gain (volume) of the source.
	 * <br><br>
	 * The volume is <b>capped to 100%.</b>
	 * 
	 * @param volume (float)
	 */
	public void setVolume(float volume) {
		volume %= 1.0001f;
		if(this.volume != volume) {
			this.volume = volume;
			AL10.alSourcef(sourceID, AL10.AL_GAIN, this.volume);
		} else if(volume <= 0) { 
			this.volume = volume;
			AL10.alSourcef(sourceID, AL10.AL_GAIN, this.volume);
		}
	}
	/**
	 * Returns current volume of source.
	 * @return volume (float)
	 */
	public float getVolume() {
		return this.volume;
	}
	/**
	 * Changes the pitch of the source.
	 * @param pitch (float)
	 */
	public void setPitch(float pitch) {
		AL10.alSourcef(sourceID, AL10.AL_PITCH, pitch);
	}
	/**
	 * Sets position of the source in the world space using Cartesian Coordinates (XYZ)
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setPosition(float x, float y, float z) {
		AL10.alSource3f(sourceID, AL10.AL_POSITION, x, y, z);
	}
}
