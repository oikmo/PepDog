package net.oikmo.engine.audio;

import org.lwjgl.util.vector.Vector3f;

public class AudioSystem {
	
	private static Source musicSource;
	
	public static void init() {
		AudioMaster.init();
		musicSource = new Source();
		musicSource.setLooping(true);
	}
	
	public static void update(Vector3f playerPosition) {
		AudioMaster.setListenerData(playerPosition.x, playerPosition.y, playerPosition.z, 0, 0, 0);
	}
	
	public static void playSFX(String sfx, Vector3f position) {
		Source source = new Source();
		source.setPosition(position.x, position.y, position.z);
		source.play(AudioMaster.loadSound(sfx));
	}
	
	public static void playMusic(String music) {
		stopMusic();
		musicSource.play(AudioMaster.loadSound(music));
	}
	
	public static void pauseMusic() {
		musicSource.pause();
	}
	
	public static void resumeMusic() {
		musicSource.continuePlaying();
	}
	
	public static void stopMusic() {
		musicSource.stop();
	}

	public static void cleanUp() {
		musicSource.delete();
		AudioMaster.cleanUp();
		
	}
}
