package net.oikmo.engine.renderers.entity;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;

import net.oikmo.engine.Entity;
import net.oikmo.engine.models.TexturedModel;
import net.oikmo.engine.renderers.MasterRenderer;
import net.oikmo.engine.textures.ModelTexture;
import net.oikmo.toolbox.Toolbox;

public class EntityRenderer {	
	private EntityShader shader;
	
	/**
	 * EntityRenderer Constructor.
	 * @param shader [StaticShader]
	 */
	public EntityRenderer(EntityShader shader, Matrix4f projectionMatrix){
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
	 * @param entities
	 */
	public void render(Map<TexturedModel,List<Entity>> entities) {
		if(entities.size() == 0) { return; }
		for(TexturedModel model : entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for(Entity entity : batch) {
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTextureModel();
		}
	}
	
	private void prepareTexturedModel(TexturedModel model) {
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		GL30.glBindVertexArray(model.getRawModel().getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		ModelTexture texture = model.getTexture();
		shader.loadNumberOfRows(texture.getNumberOfRows());
		shader.loadShineVariables(texture.getShineDamper(),texture.getReflectivity());
		shader.loadFakeLighting(texture.isUseFakeLighting());
		
		if(texture.isHasTransparency()) {
			MasterRenderer.disableCulling();
		}
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
	
	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = Toolbox.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
	}
}
