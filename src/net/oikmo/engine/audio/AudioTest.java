package net.oikmo.engine.audio;

import java.io.IOException;

import org.lwjgl.openal.AL11;

/**
 * A little test area to test the sound engine
 * 
 * @author Oikmo
 */
public class AudioTest {
	
	public static void main(String[] args) throws IOException, InterruptedException {
		AudioMaster.init();
		AudioMaster.setListenerData(0, 0, 0, 0, 0, 0);
		int buffer = AudioMaster.loadSound("clickfast");
		/* AL_EXPONENT_DISTANCE_CLAMPED	*
		 * AL_INVERSE_DISTANCE_CLAMPED	*
		 * AL_LINEAR_DISTANCE_CLAMPED   */
		Source source = new Source(6,6,15,AL11.AL_LINEAR_DISTANCE_CLAMPED);
		source.setLooping(true);
		source.play(buffer);
		
		float xPos = 5;
		source.setPosition(xPos, 0, 2);
		
		int i = 0;
		while(i != 1) {
			xPos -= 0.03f;
			source.setPosition(xPos, 0, 2);
			Thread.sleep(50);
		}
		
		AudioMaster.cleanUp();
	}
}
