package net.oikmo.engine.renderers.part;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import net.oikmo.engine.Part;
import net.oikmo.engine.models.TexturedModel;
import net.oikmo.engine.renderers.MasterRenderer;
import net.oikmo.engine.textures.ModelTexture;
import net.oikmo.toolbox.Maths;

public class PartRenderer {	
	private PartShader shader;
	
	/**
	 * EntityRenderer Constructor.
	 * @param shader [StaticShader]
	 */
	public PartRenderer(PartShader shader, Matrix4f projectionMatrix){
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void updateProjectionMatrix(Matrix4f projectionMatrix) {
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	/**
	 * Renders entities by sorting their texture then the amount of entities that has the texture
	 * @param parts
	 */
	public void render(Map<TexturedModel, List<Part>> parts) {
		if(parts.size() == 0) { return; }
		for(TexturedModel model : parts.keySet()) {
			prepareTexturedModel(model);
			List<Part> batch = parts.get(model);
			for(Part part : batch) {
				prepareInstance(part);
				//System.out.println(model.getRawModel() + " " + model.getRawModel().getVertexCount());
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTextureModel();
		}
	}
	
	private void prepareTexturedModel(TexturedModel model) {
		//GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		GL30.glBindVertexArray(model.getRawModel().getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		ModelTexture texture = model.getTexture();
		shader.loadNumberOfRows(texture.getNumberOfRows());
		shader.loadShineVariables(texture.getShineDamper(),texture.getReflectivity());
		shader.loadFakeLighting(texture.isUseFakeLighting());
		
		MasterRenderer.disableCulling();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
	}
	
	private void unbindTextureModel() {
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void prepareInstance(Part part) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(part.getPosition(), part.getRotation(), part.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		shader.loadOffset(part.getTextureXOffset(), part.getTextureYOffset());
	}
}
