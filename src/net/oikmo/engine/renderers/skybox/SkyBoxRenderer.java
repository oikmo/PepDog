package net.oikmo.engine.renderers.skybox;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import net.oikmo.engine.Loader;
import net.oikmo.engine.models.RawModel;
import net.oikmo.main.entity.Camera;
import net.oikmo.toolbox.Logger;
import net.oikmo.toolbox.Logger.LogLevel;

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
	private static String[] NIGHT_TEXTURE_FILES = {"right","left","up","down","back","front"};
	
	private RawModel cube;
	private int dayTexture;
	private int nightTexture;
	private SkyBoxShader shader;
	
	public SkyBoxRenderer(String skyboxDayName, String skyboxNightName, Matrix4f projectionMatrix) {
		cube = Loader.getInstance().loadToVAO(VERTICES, 3);
		Logger.log(LogLevel.INFO, "==================");
		for(int i = 0; i < TEXTURE_FILES.length; i++) {
			DAY_TEXTURE_FILES[i] = skyboxDayName + "_" +TEXTURE_FILES[i];
			Logger.log(LogLevel.INFO, DAY_TEXTURE_FILES[i]);
		}
		Logger.log(LogLevel.INFO, "==================");
		dayTexture = Loader.getInstance().loadCubeMap(DAY_TEXTURE_FILES);
		for(int i = 0; i < TEXTURE_FILES.length; i++) {
			NIGHT_TEXTURE_FILES[i] = skyboxNightName + "_" + TEXTURE_FILES[i];
			Logger.log(LogLevel.INFO, NIGHT_TEXTURE_FILES[i]);
		}
		Logger.log(LogLevel.INFO, "==================");
		nightTexture = Loader.getInstance().loadCubeMap(NIGHT_TEXTURE_FILES);
		shader = new SkyBoxShader();
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
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
	long time;
	float blendFactor = 0;
	boolean shouldStore = false;
	private void bindTextures(){
		
		int texture1 = dayTexture;
		//int texture2 = nightTexture;
		
		/*if(Main.gameState == GameState.game) {
			time += DisplayManager.getFrameTimeSeconds() * 1000;
			time %= 24000;
			
			blendFactor = -((time/1000f)-13)*((time/1000f)-13) * 0.02f + 1;
			if(blendFactor < 0) blendFactor = 0;
			if(blendFactor > 0.5 ) { MasterRenderer.getInstance().setNightColours(); } else { MasterRenderer.getInstance().setDayColours(); }
			if(blendFactor < 0) blendFactor = 0;
			
			if(!shouldStore) {
				shouldStore = true;
			}
		} else if(Main.gameState == GameState.mainmenu) {
			if(shouldStore) {
				blendFactor = 0;
				shouldStore= false;
			}
		}*/
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
		//GL13.glActiveTexture(GL13.GL_TEXTURE1);
		//GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
		
		//shader.loadBlendFactor(blendFactor);
	}
}
