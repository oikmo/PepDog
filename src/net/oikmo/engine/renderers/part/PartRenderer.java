package net.oikmo.engine.renderers.part;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import net.oikmo.engine.Loader;
import net.oikmo.engine.Part;
import net.oikmo.engine.models.RawModel;
import net.oikmo.engine.models.TexturedModel;
import net.oikmo.engine.renderers.MasterRenderer;
import net.oikmo.engine.textures.ModelTexture;
import net.oikmo.toolbox.Toolbox;
import net.oikmo.toolbox.obj.OBJLoader;

public class PartRenderer {	
	private PartShader shader;
	
	public static RawModel cylinder = OBJLoader.loadOBJ("cylinder");
	public static RawModel block =  OBJLoader.loadOBJ("cube");
	public static RawModel sphere = OBJLoader.loadOBJ("sphere");
	
	public static int texture;
	
	/**
	 * EntityRenderer Constructor.
	 * @param shader [StaticShader]
	 */
	public PartRenderer(PartShader shader, Matrix4f projectionMatrix){
		this.shader = shader;
		texture = Loader.loadGameTexture("models/base");
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
				part.update();
				prepareInstance(part);
				if(part.getTransparency() != 1f) {
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				}
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			
			unbindTextureModel();
		}
	}
	
	/**
	 * Does exactly as it says.<br>
	 * Loads VAO into GPU and assigns texture.
	 * @param model
	 */
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
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
	}
	
	/**
	 * Guess.
	 */
	private void unbindTextureModel() {
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * Creates the actual visuals to be displayed on screen.
	 * @param part {@link Part}
	 */
	private void prepareInstance(Part part) {
		Matrix4f transformationMatrix = Toolbox.createTransformationMatrix(part.getPosition(), part.getRotation(), part.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		shader.loadPartColour(part.getColour());
		shader.loadTransparency(part.getTransparency());
		
	}
}
