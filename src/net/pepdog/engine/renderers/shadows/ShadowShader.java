package net.pepdog.engine.renderers.shadows;

import org.lwjgl.util.vector.Matrix4f;

import net.pepdog.engine.renderers.ShaderProgram;

public class ShadowShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "/net/oikmo/engine/renderers/shadows/shadowVertex.glsl";
	private static final String FRAGMENT_FILE = "/net/oikmo/engine/renderers/shadows/shadowFragment.glsl";
	
	private int location_mvpMatrix;

	protected ShadowShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "in_position");
		super.bindAttribute(1, "in_textureCoords");
	}

	@Override
	protected void getAllUniformLocations() {
		location_mvpMatrix = super.getUniformLocation("mvpMatrix");
	}
	
	protected void loadMvpMatrix(Matrix4f mvpMatrix){
		super.loadMatrix(location_mvpMatrix, mvpMatrix);
	}

	

}
