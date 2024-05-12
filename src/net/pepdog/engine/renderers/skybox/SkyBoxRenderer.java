package net.pepdog.engine.renderers.skybox;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import net.pepdog.engine.Loader;
import net.pepdog.engine.entity.Camera;
import net.pepdog.engine.models.RawModel;
import net.pepdog.toolbox.Logger;
import net.pepdog.toolbox.Logger.LogLevel;

public class SkyBoxRenderer {
	private static final float SIZE = 500f;
	
	private static final float[] VERTICES = {        
	    -SIZE,  SIZE, -SIZE,
	    -SIZE, -SIZE, -SIZE,
	    SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	    -SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE
	};
	
	private static String[] TEXTURE_FILES = {"right","left","up","down","back","front"};
	private static String[] DAY_TEXTURE_FILES = {"right","left","up","down","back","front"};
	
	private RawModel cube;
	private int dayTexture;
	private SkyBoxShader shader;
	
	public SkyBoxRenderer(String skyboxDayName, String skyboxNightName, Matrix4f projectionMatrix) {
		cube = Loader.loadToVAO(VERTICES, 3);
		for(int i = 0; i < TEXTURE_FILES.length; i++) {
			DAY_TEXTURE_FILES[i] = skyboxDayName + "_" +TEXTURE_FILES[i];
		}
		
		dayTexture = Loader.loadCubeMap(DAY_TEXTURE_FILES);
		shader = new SkyBoxShader();
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		Logger.log(LogLevel.INFO, "Loaded!");
	}
	
	public void updateProjectionMatrix(Matrix4f matrix) {	
		shader.loadProjectionMatrix(matrix);
	}
	public void render(Camera camera,  Matrix4f projectionMatrix, float r, float g, float b) {
		shader.start();
		shader.loadViewMatrix(camera);
		shader.loadFogColour(r, g, b);
		shader.loadProjectionMatrix(projectionMatrix);
		GL30.glBindVertexArray(cube.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		bindTextures();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	private void bindTextures(){		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, dayTexture);
	}
}
