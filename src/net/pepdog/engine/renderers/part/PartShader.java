package net.pepdog.engine.renderers.part;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import net.pepdog.engine.renderers.ShaderProgram;
import net.pepdog.main.entity.Camera;
import net.pepdog.main.entity.Light;
import net.pepdog.toolbox.Toolbox;

/**
 * Static Shader class which loads graphics e.g textureCoords and light entitys on entities.
 * 
 * @author <i>Oikmo</i>
 */
public class PartShader extends ShaderProgram {
	private static final String VERTEX_FILE = "/net/oikmo/engine/renderers/part/partVertex.glsl";
	private static final String FRAGMENT_FILE = "/net/oikmo/engine/renderers/part/partFragment.glsl";

	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition;
	private int location_lightColour;
	private int location_attenuation;
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_useFakeLighting;
	private int location_skyColour;
	private int location_numberOfRows;
	private int location_offset;
	private int location_plane;
	private int location_partColour;
	private int location_transparency;
	
	public PartShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}
	
	
	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_useFakeLighting = super.getUniformLocation("useFakeLighting");
		location_skyColour = super.getUniformLocation("skyColour");
		location_numberOfRows = super.getUniformLocation("numberOfRows");
		location_offset = super.getUniformLocation("offset");
		location_plane = super.getUniformLocation("plane");
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColour = super.getUniformLocation("lightColour");
		location_attenuation = super.getUniformLocation("attenuation");
		location_partColour = super.getUniformLocation("partColour");
		location_transparency = super.getUniformLocation("alphaValue");
	}
	
	public void loadClipPlane(Vector4f plane) {
		super.load4DVector(location_plane, plane);
	}
	
	public void loadNumberOfRows(int numberOfRows) {
		super.loadFloat(location_numberOfRows, numberOfRows);
	}
	
	public void loadOffset(float x, float y) {
		super.load2DVector(location_offset, new Vector2f(x,y));
	}
	
	public void loadSkyColour(float r, float g, float b) {
		super.load3DVector(location_skyColour, new Vector3f(r,g,b));
	}
	
	public void loadPartColour(Vector3f colour) {
		super.load3DVector(location_partColour, new Vector3f(colour.x,colour.y,colour.z));
	}
	
	public void loadFakeLighting(boolean useFake) {
		super.loadBoolean(location_useFakeLighting, useFake);
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
		Matrix4f viewMatrix = Toolbox.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	/**
	 * Loads light from entity to light position and colour [aka strength too]
	 * @param light [Light]
	 * @author <i>Oikmo</i>
	 */
	public void loadLights(List<Light> lights) {
		super.load3DVector(location_lightPosition, lights.get(0).getPosition());
		super.load3DVector(location_lightColour, lights.get(0).getColour());
		super.load3DVector(location_attenuation, lights.get(0).getAttenuation());
	}
	
	public void loadTransparency(float transparency) {
		super.loadFloat(location_transparency, transparency);
	}
}