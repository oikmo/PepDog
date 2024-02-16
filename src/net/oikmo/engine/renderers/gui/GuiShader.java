package net.oikmo.engine.renderers.gui;

import org.lwjgl.util.vector.Matrix4f;

import net.oikmo.engine.shaders.ShaderProgram;

public class GuiShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "/net/oikmo/engine/renderers/gui/guiVertex.glsl";
	private static final String FRAGMENT_FILE = "/net/oikmo/engine/renderers/gui/guiFragment.glsl";
	
	private int location_transformationMatrix;
	private int location_tilingSize;

	public GuiShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_tilingSize = super.getUniformLocation("tilingSize");
	}
	
	public void loadTilingSize(int tilingSize) {
		super.loadInt(location_tilingSize, tilingSize);
	}
	
	public void loadTransformation(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	

}
