package net.oikmo.engine.renderers.gui;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import net.oikmo.engine.Loader;
import net.oikmo.engine.models.RawModel;
import net.oikmo.engine.textures.GuiTexture;
import net.oikmo.toolbox.Toolbox;

public class GuiRenderer {
	
	private final RawModel quad;
	private GuiShader shader;
	
	public GuiRenderer(GuiShader shader) {
		float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 };
		quad = Loader.loadToVAO(positions, 2);
		this.shader = shader;
	}
	
	public void updateProjectionMatrix(Matrix4f projection) {
		shader.loadTransformation(projection);
	}
	
	
	public void render(List<GuiTexture> guis) {
		if(guis == null) { 
			return;
		}
		if(guis.size() == 0) {
			return; 
		}
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		for(GuiTexture gui : guis) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTextureID());
			Matrix4f matrix = Toolbox.createTransformationMatrix(gui.getPosition(), gui.getScale());
			shader.loadTransformation(matrix);
			shader.loadTilingSize(gui.getTilingSize());
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		GL11.glDisable(GL11.GL_BLEND);
		shader.stop();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}
}
