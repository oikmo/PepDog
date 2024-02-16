package net.oikmo.engine.audio;

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
	
	public Source() {
		sourceID = AL10.alGenSources();
	}
	
	/** AL_EXPONENT_DISTANCE_CLAMPED	*
	 * AL_INVERSE_DISTANCE_CLAMPED	*
	 * AL_LINEAR_DISTANCE_CLAMPED   */
	public Source(float rolloff, float reference, float maxDist, int model) {
		sourceID = AL10.alGenSources();
		AL10.alSourcef(sourceID, AL10.AL_ROLLOFF_FACTOR, rolloff);
		AL10.alSourcef(sourceID, AL10.AL_REFERENCE_DISTANCE, reference);
		AL10.alSourcef(sourceID, AL10.AL_MAX_DISTANCE, maxDist);
		AL10.alDistanceModel(model);
	}
	
	public float getTime() {
		return AL10.alGetSourcef(sourceID, AL11.AL_SEC_OFFSET);
	}
	
	public void play(int buffer) {
		stop();
		bufferID = buffer;
		AL10.alSourcei(sourceID, AL10.AL_BUFFER, buffer);
		continuePlaying();
	}
	
	public float getEndTime() {
		int sizeInBytes = AL10.alGetBufferi(bufferID, AL10.AL_SIZE);
		int channels = AL10.alGetBufferi(bufferID, AL10.AL_CHANNELS);
		int bits = AL10.alGetBufferi(bufferID, AL10.AL_BITS);
		int frequency = AL10.alGetBufferi(bufferID, AL10.AL_FREQUENCY);
		float lengthInSamples = sizeInBytes * 8 / (channels * bits);
		return (float)lengthInSamples / (float)frequency;
	}
	
	public boolean isPlaying() {
		return AL10.alGetSourcei(sourceID, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}
	
	public void setLooping(boolean loop) {
		AL10.alSourcei(sourceID, AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
	}
	
	public void pause() {
		AL10.alSourcePause(sourceID);
	}
	
	public void continuePlaying() {
		AL10.alSourcePlay(sourceID);
	}
	
	public void stop() {
		AL10.alSourceStop(sourceID);
	}
	
	public void delete() {
		stop();
		AL10.alDeleteSources(sourceID);
	}
	
	public void setVelocity(float x, float y, float z) {
		AL10.alSource3f(sourceID, AL10.AL_POSITION, x, y, z);
	}
	
	public void setVolume(float volume) {
		volume %= 1.0001f;
		//System.out.println(volume + " source");
		if(this.volume != volume) {
			this.volume = volume;
			AL10.alSourcef(sourceID, AL10.AL_GAIN, this.volume);
		} else if(volume <= 0) { 
			this.volume = volume;
			AL10.alSourcef(sourceID, AL10.AL_GAIN, this.volume);
		}
	}
	
	public float getVolume() {
		return this.volume;
	}
	
	public void setPitch(float pitch) {
		AL10.alSourcef(sourceID, AL10.AL_PITCH, pitch);
	}
	public void setPosition(float x, float y, float z) {
		AL10.alSource3f(sourceID, AL10.AL_POSITION, x, y, z);
	}
}
