package net.pepdog.engine.renderers.postProcessing;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import net.pepdog.engine.Loader;
import net.pepdog.engine.models.RawModel;

public class PostProcessing {
	
	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };	
	private static RawModel quad;
	private static PixelatorEffect pixelEffect;

	public static void init(){
		quad = Loader.loadToVAO(POSITIONS, 2);
		pixelEffect = new PixelatorEffect();
	}
	
	public static void doPostProcessing(int colourTexture){
		start();
		pixelEffect.render(colourTexture);
		end();
	}
	
	public static void cleanUp(){
		pixelEffect.cleanUp();
	}
	
	private static void start(){
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	private static void end(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}


}
