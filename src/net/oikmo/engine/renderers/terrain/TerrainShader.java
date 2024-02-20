package net.oikmo.engine.renderers.terrain;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import net.oikmo.engine.shaders.ShaderProgram;
import net.oikmo.main.entity.Camera;
import net.oikmo.main.entity.Light;
import net.oikmo.toolbox.Maths;

/**
 * Static Shader class which loads graphics e.g textureCoords and light normals on entities.
 * 
 * @author <i>Oikmo</i>
 */
public class TerrainShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "/net/oikmo/engine/renderers/terrain/terrainVertex.glsl";
	private static final String FRAGMENT_FILE = "/net/oikmo/engine/renderers/terrain/terrainFragment.glsl";

	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition;
	private int location_lightColour;
	private int location_attenuation;
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_skyColour;
	private int location_backgroundTexture;
	private int location_rTexture;
	private int location_gTexture;
	private int location_bTexture;
	private int location_blendMap;
	private int location_plane;
	private int location_toShadowMapSpace;
	private int location_shadowMap;
	
	/**
	 * TerrainShader Constructor
	 * @author <i>Oikmo</i>
	 */
	public TerrainShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	/**
	 * Binds attributes from the shaders.
	 * @author <i>Oikmo</i>
	 */
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}
	
	/**
	 * Assigns all uniform locations from the shaders
	 * @author <i>Oikmo</i>
	 */
	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_skyColour = super.getUniformLocation("skyColour");
		location_backgroundTexture = super.getUniformLocation("backgroundTexture");
		location_rTexture = super.getUniformLocation("rTexture");
		location_gTexture = super.getUniformLocation("gTexture");
		location_bTexture = super.getUniformLocation("bTexture");
		location_blendMap = super.getUniformLocation("blendMap");
		location_plane = super.getUniformLocation("plane");
		location_toShadowMapSpace = super.getUniformLocation("toShadowMapSpace");
		location_shadowMap = super.getUniformLocation("shadowMap");
		
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColour = super.getUniformLocation("lightColour");
		location_attenuation = super.getUniformLocation("attenuation");
	}
	
	public void loadClipPlane(Vector4f plane) {
		super.load4DVector(location_plane, plane);
	}
	public void connectTextureUnits() {
		super.loadInt(location_backgroundTexture, 0);
		super.loadInt(location_rTexture, 1);
		super.loadInt(location_gTexture, 2);
		super.loadInt(location_bTexture, 3);
		super.loadInt(location_blendMap, 4);
		super.loadInt(location_shadowMap, 5);
	}
	
	public void loadSkyColour(float r, float g, float b) {
		super.load3DVector(location_skyColour, new Vector3f(r,g,b));
	}
	
	/**
	 * Loads shine variables either make something reflective as hell or nah
	 * 
	 * @param damper [float]
	 * @param reflectivity [float]
	 * @author <i>Oikmo</i>
	 */
	public void loadShineVariables(float damper, float reflectivity) {
		this.loadFloat(location_shineDamper, damper);
		this.loadFloat(location_reflectivity, reflectivity);
	}
	
	/**
	 * Guess what this loads. That's right the transformation matrix.
	 * 
	 * @param matrix [Matrix4f]
	 * @author <i>Oikmo</i>
	 */
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	/**
	 * Guess what this loads. That's right the projection matrix.
	 * 
	 * @param projection [Matrix4f]
	 * @author <i>Oikmo</i>
	 */
	public void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
	/**
	 * Guess what this loads. That's right the view matrix.
	 * 
	 * @param camera [Camera]
	 * @author <i>Oikmo</i>
	 */
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	/**
	 * Loads light from entity to light position and colour [aka strength too]
	 * @param light [Light]
	 * @author <i>Oikmo</i>
	 */
	public void loadLight(List<Light> lights) {
		super.load3DVector(location_lightPosition, lights.get(0).getPosition());
		super.load3DVector(location_lightColour, lights.get(0).getColour());
		super.load3DVector(location_attenuation, lights.get(0).getAttenuation());
	}
	
	public void loadToShadowMapSpace(Matrix4f matrix) {
		super.loadMatrix(location_toShadowMapSpace, matrix);
	}
}