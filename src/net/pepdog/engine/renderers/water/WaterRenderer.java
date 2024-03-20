package net.pepdog.engine.renderers.water;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import net.pepdog.engine.DisplayManager;
import net.pepdog.engine.Loader;
import net.pepdog.engine.models.RawModel;
import net.pepdog.main.Main;
import net.pepdog.main.Main.GameState;
import net.pepdog.main.entity.Camera;
import net.pepdog.main.entity.Light;
import net.pepdog.toolbox.Toolbox;

public class WaterRenderer {

	private final String DUDV_MAP = "waterDUDV";
	private final String NORMAL_MAP = "waterNormal";
	private final float WAVE_SPEED = 0.03f;
	
	private RawModel quad;
	private WaterShader shader;
	private WaterFrameBuffers fbos;
	
	private float moveFactor = 0;
	
	private int dudvTexture;
	private int normalMapTexture;
	
	public WaterRenderer(Matrix4f projectionMatrix, WaterFrameBuffers fbos) {
		this.shader = new WaterShader();
		this.fbos = fbos;
		dudvTexture = Loader.loadGameTexture("water/"+DUDV_MAP);
		normalMapTexture = Loader.loadGameTexture("water/"+NORMAL_MAP);
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		setUpVAO();
	}

	public void render(List<WaterTile> water, Camera camera, Light sun, Matrix4f projectionMatrix) {
		prepareRender(camera, sun, projectionMatrix);	
		for (WaterTile tile : water) {
			Matrix4f modelMatrix = Toolbox.createTransformationMatrix(
					new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0,
					tile.getTileSize());
			shader.loadModelMatrix(modelMatrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
		}
		unbind();
	}
	
	private void prepareRender(Camera camera, Light sun, Matrix4f projectionMatrix){
		shader.start();
		shader.loadViewMatrix(camera);
		shader.loadProjectionMatrix(projectionMatrix);
		if(Main.gameState == GameState.game) {
			moveFactor += WAVE_SPEED * DisplayManager.getFrameTimeSeconds();
			moveFactor %= 1;
		}
		
		shader.loadLight(sun);
		shader.loadMoveFactor(moveFactor);
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, dudvTexture);
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalMapTexture);
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionDepthTexture());
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	private void unbind(){
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	private void setUpVAO() {
		// Just x and z vectex positions here, y is set to 0 in v.shader
		float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
		quad = Loader.loadToVAO(vertices, 2);
	}

}
