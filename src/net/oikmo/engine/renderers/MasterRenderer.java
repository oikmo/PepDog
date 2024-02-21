package net.oikmo.engine.renderers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import net.oikmo.engine.Entity;
import net.oikmo.engine.Loader;
import net.oikmo.engine.Part;
import net.oikmo.engine.audio.AudioMaster;
import net.oikmo.engine.gui.GuiTexture;
import net.oikmo.engine.gui.font.fontMeshCreator.FontType;
import net.oikmo.engine.gui.font.fontRendering.TextMaster;
import net.oikmo.engine.models.TexturedModel;
import net.oikmo.engine.particles.ParticleMaster;
import net.oikmo.engine.postProcessing.Fbo;
import net.oikmo.engine.renderers.entity.EntityRenderer;
import net.oikmo.engine.renderers.entity.EntityShader;
import net.oikmo.engine.renderers.gui.GuiRenderer;
import net.oikmo.engine.renderers.gui.GuiShader;
import net.oikmo.engine.renderers.part.PartRenderer;
import net.oikmo.engine.renderers.part.PartShader;
import net.oikmo.engine.renderers.postProcessing.PostProcessing;
import net.oikmo.engine.renderers.shadows.ShadowMapMasterRenderer;
import net.oikmo.engine.renderers.skybox.SkyBoxRenderer;
import net.oikmo.engine.renderers.terrain.TerrainRenderer;
import net.oikmo.engine.renderers.terrain.TerrainShader;
import net.oikmo.engine.renderers.water.WaterRenderer;
import net.oikmo.engine.scene.Scene;
import net.oikmo.engine.terrain.Terrain;
import net.oikmo.engine.water.WaterFrameBuffers;
import net.oikmo.engine.water.WaterTile;
import net.oikmo.main.entity.Camera;
import net.oikmo.main.entity.Light;
import net.oikmo.main.scene.SceneManager;

public class MasterRenderer {
	
	private Matrix4f projectionMatrix;
	
	private EntityRenderer entityRenderer;
	private EntityShader entityShader = new EntityShader();
	
	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();
	
	private GuiRenderer guiRenderer;
	private GuiShader guiShader = new GuiShader();
	
	private SkyBoxRenderer skyboxRenderer;
	
	private WaterRenderer waterRenderer;
	
	private PartRenderer partsRenderer;
	private PartShader partShader = new PartShader();
	
	private ShadowMapMasterRenderer shadowMapRenderer;
	
	public static FontType font;
	public int ui_nuhuh;
	public int ui_button;
	public int ui_smallbutton;
	public int ui_hover;
	public int ui_smallhover;
	
	public int ui_enter;
	public int ui_select;
	public int ui_deselect;
	
	//sky values
	public static float DAYRED = 0.4f, DAYGREEN = 0.7f, DAYBLUE = 1.0f;
	public static float NIGHTRED = 0.031f, NIGHTGREEN = 0.043f, NIGHTBLUE = 0.102f;
	public static float RED = DAYRED, GREEN = DAYGREEN, BLUE = DAYBLUE;
	
	//cam values
	public static float FOV = 60;

	public static final float NEAR_PLANE = 0.1f;
	public static final float FAR_PLANE = 1000f;
	
	private float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private Map<TexturedModel, List<Part>> parts = new HashMap<TexturedModel, List<Part>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	private List<GuiTexture> guis = new ArrayList<GuiTexture>();
	
	public static int WIDTH = 854;
	public static int HEIGHT = 480;
	
	private Fbo fbo;
	private WaterFrameBuffers waterFBO;
	
	
	private static MasterRenderer instance;
	
	public static MasterRenderer getInstance() {
		if(instance ==  null) {
			instance = new MasterRenderer();
		}
		return instance;
	}
	
	public MasterRenderer() {
		enableCulling();
		createProjectionMatrix();
		entityRenderer = new EntityRenderer(entityShader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		guiRenderer = new GuiRenderer(guiShader);
		skyboxRenderer = new SkyBoxRenderer("plainsky", "shiverfrost", projectionMatrix);
		partsRenderer = new PartRenderer(partShader, projectionMatrix);
		
		ui_enter = AudioMaster.loadSound("menu-enter");
		ui_select = AudioMaster.loadSound("menu-select");
		ui_deselect = AudioMaster.loadSound("menu-deselect");
		
		Loader loader = Loader.getInstance();
		ui_nuhuh = loader.loadTexture("ui/ui_nuhuh");
		ui_button = loader.loadTexture("ui/normal/ui_button");
		ui_hover = loader.loadTexture("ui/normal/ui_button_hover");

		ui_smallbutton = loader.loadTexture("ui/small/ui_button");
		ui_smallhover = loader.loadTexture("ui/small/ui_button_hover");
		PostProcessing.init();
		fbo = new Fbo(WIDTH, HEIGHT, Fbo.DEPTH_RENDER_BUFFER);
		waterFBO = new WaterFrameBuffers();
		
		ParticleMaster.init(projectionMatrix);
		TextMaster.init();
	}
	
	public void createShadowMap(Camera camera) {
		shadowMapRenderer = new ShadowMapMasterRenderer(camera);
	}
	
	public void setColours(float r, float g, float b) {
		RED = r;
		GREEN = g;
		BLUE = b;
	}
	
	public void setNightColours() {
		RED = NIGHTRED;
		GREEN = NIGHTGREEN;
		BLUE = NIGHTBLUE;
	}
	
	public void setDayColours() {
		RED = DAYRED;
		GREEN = DAYGREEN;
		BLUE = DAYBLUE;
	}
	
	/**
	 * Runs OpenGL calls before output.
	 * 
	 * Runs depth test and culling along with setting the sky.
	 */
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClearColor(RED, GREEN, BLUE, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		//GL13.glActiveTexture(GL13.GL_TEXTURE5);
		//GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.getShadowMapTexture());
	}
	
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public void renderScene(List<Light> lights, Camera camera, List<Terrain> terrains, List<Entity> entities, List<Part> parts, Vector4f clipPlane) {
	
		for(Terrain terrain : terrains) {
			processTerrain(terrain);
		}
		for(Entity entity : entities) {
			processEntity(entity);
		}
		for(Part part : parts) {
			processParts(part);
		}
		render(lights,camera, clipPlane);
	}
	
	public void renderWater(List<Light> lights, Camera camera, List<Terrain> terrains, List<Entity> entities,List<Part> parts,  List<WaterTile> waters,WaterFrameBuffers fbos, WaterTile water) {
		if(waterRenderer == null) {
			waterRenderer = new WaterRenderer(projectionMatrix, fbos);
		}
		GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
		fbos.bindReflectionFrameBuffer();
		
		float distance = 2 * (camera.getPosition().y - water.getHeight());
		camera.getPosition().y -= distance;
		camera.invertPitch();
		renderScene(lights, camera, terrains, entities, parts, new Vector4f(0,1,0, -water.getHeight()));
		camera.getPosition().y += distance;
		camera.invertPitch();
		
		fbos.bindRefractionFrameBuffer();
		renderScene(lights, camera, terrains, entities, parts, new Vector4f(0,-1,0, water.getHeight()));
		
		fbos.unbindCurrentFrameBuffer();
		GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
		renderScene(lights, camera, terrains, entities, parts, new Vector4f(0, 1,0, 15));
		waterRenderer.render(waters, camera, lights.get(0), projectionMatrix);
	}
	
	public void renderSceneWater(boolean postProcess, Camera camera) {
		Scene scene = SceneManager.getCurrentScene();
		if(postProcess) {
			fbo.bindFrameBuffer();
		}
		renderScene(scene.getLights(), camera, scene.getTerrain(), scene.getEntities(), scene.getParts(), new Vector4f(0,0,0,0));
		//this.renderShadowMap(scene.getEntities(), scene.getLights().get(0));
		renderWater(scene.getLights(), camera, scene.getTerrain(), scene.getEntities(), scene.getParts(), scene.getWaters(), waterFBO, scene.getWaters().get(0));
		ParticleMaster.update(camera);
		ParticleMaster.renderParticles(camera);
		if(postProcess) {
			fbo.unbindFrameBuffer();
			PostProcessing.doPostProcessing(fbo.getColourTexture());
		}
		guiRenderer.render(guis);
		TextMaster.render();
	}
	
	public void renderScene( Camera camera) {
		Scene scene = SceneManager.getCurrentScene();
		if(!scene.isLoaded()) { return; }
		renderScene(scene.getLights(), camera, scene.getTerrain(), scene.getEntities(), scene.getParts(), new Vector4f(0,0,0,0));
		//this.renderShadowMap(scene.getEntities(), scene.getLights().get(0));
		ParticleMaster.update(camera);
		ParticleMaster.renderParticles(camera);
		guiRenderer.render(guis);
		TextMaster.render();
	}
	
	public void render(List<Light> lights, Camera camera, Vector4f clipPlane) {
		aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		
		prepare();
		
		entityShader.start();
		entityShader.loadClipPlane(clipPlane);
		entityShader.loadLights(lights);
		entityShader.loadViewMatrix(camera);
		entityShader.loadSkyColour(RED, GREEN, BLUE);
		entityRenderer.render(entities);
		entityShader.stop();
		
		partShader.start();
		partShader.loadClipPlane(clipPlane);
		partShader.loadLights(lights);
		partShader.loadViewMatrix(camera);
		partShader.loadSkyColour(RED, GREEN, BLUE);
		partsRenderer.render(parts);
		partShader.stop();
		
		skyboxRenderer.render(camera, projectionMatrix, RED, GREEN, BLUE);
		terrains.clear();
		entities.clear();
		parts.clear();
	}
	
	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	
	public void processEntity(Entity entity) {
		TexturedModel model = entity.getModel();
		List<Entity> batch = entities.get(model);
		if(batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(model, newBatch);
		}
	}
	
	public void processParts(Part part) {
		TexturedModel model = part.getModel();
		List<Part> batch = parts.get(model);
		if(batch != null) {
			batch.add(part);
		} else {
			List<Part> newBatch = new ArrayList<Part>();
			newBatch.add(part);
			parts.put(model, newBatch);
		}
	}
	
	public void processGUI(GuiTexture texture) {
		guis.add(texture);
	}
	
	public void renderShadowMap(List<Entity> entityList, Light sun) {
		for(Entity entity : entityList) {
			processEntity(entity);
		}
		shadowMapRenderer.render(entities, sun);
		entities.clear();
	}
	
	public int getShadowMapTexture() {
		return shadowMapRenderer.getShadowMap();
	}
	
	public void cleanUp() {
		entityShader.cleanUp();
		terrainShader.cleanUp();
		PostProcessing.cleanUp();
		Loader.getInstance().cleanUp();
		ParticleMaster.cleanUp();
		TextMaster.cleanUp();
	}
	
	private void createProjectionMatrix() {
		aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}
	
	public void updateProjectionMatrix(float fov) {
		if(FOV == fov) { return; }
		MasterRenderer.FOV = fov;
		
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
		float x_scale = y_scale / aspectRatio;

		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / (FAR_PLANE - NEAR_PLANE));
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / (FAR_PLANE - NEAR_PLANE));
		projectionMatrix.m33 = 0;
		
		entityRenderer.updateProjectionMatrix(projectionMatrix);
		terrainRenderer.updateProjectionMatrix(projectionMatrix);
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
	public List<GuiTexture> getGUIList() {
		return guis;
	}
	public void addToGUIs(GuiTexture gui) {
		guis.add(gui);
	}
	public void removeFromGUIs(GuiTexture gui) {
		guis.remove(gui);
	}
}
