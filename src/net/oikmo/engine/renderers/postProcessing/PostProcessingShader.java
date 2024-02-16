package net.oikmo.engine.renderers.postProcessing;

import net.oikmo.engine.shaders.ShaderProgram;

public class PostProcessingShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/net/oikmo/engine/renderers/postProcessing/postProcessingVertex.glsl";
	private static final String FRAGMENT_FILE = "/net/oikmo/engine/renderers/postProcessing/postProcessingFragment.glsl";
	
	public PostProcessingShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {	
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
