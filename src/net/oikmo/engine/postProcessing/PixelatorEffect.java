package net.oikmo.engine.postProcessing;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import net.oikmo.engine.renderers.postProcessing.ImageRenderer;
import net.oikmo.engine.renderers.postProcessing.PostProcessingShader;

public class PixelatorEffect {
	private ImageRenderer renderer;
	private PostProcessingShader shader;
	
	public PixelatorEffect() {
		shader = new PostProcessingShader();
		renderer = new ImageRenderer();
	}
	
	public void render(int texture) {
		shader.start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		renderer.renderQuad();
		shader.stop();
	}
	
	public void cleanUp() {
		shader.cleanUp();
		renderer.cleanUp();
	}
}
