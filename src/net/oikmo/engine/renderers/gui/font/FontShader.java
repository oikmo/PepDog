package net.oikmo.engine.renderers.gui.font;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import net.oikmo.engine.renderers.ShaderProgram;


public class FontShader extends ShaderProgram{

	private static final String VERTEX_FILE = "/net/oikmo/engine/renderers/gui/font/fontVertex.glsl";
	private static final String FRAGMENT_FILE = "/net/oikmo/engine/renderers/gui/font/fontFragment.glsl";
	
	
	private int location_colour;
	private int location_translation;
	private int location_width;
	private int location_edge;
	private int location_borderWidth;
	private int location_borderEdge;
	private int location_outlineColour;
	private int location_offset;
	
	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_colour = super.getUniformLocation("colour");
		location_translation = super.getUniformLocation("translation");
		location_width = super.getUniformLocation("width");
		location_edge = super.getUniformLocation("edge");
		location_borderWidth = super.getUniformLocation("borderWidth");
		location_borderEdge = super.getUniformLocation("borderEdge");
		location_outlineColour = super.getUniformLocation("outlineColour");
		location_offset = super.getUniformLocation("offset");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}
	
	public void loadEdges(float width, float edge) {
		super.loadFloat(location_width, width);
		super.loadFloat(location_edge, edge);
	}
	
	public void loadBorderEdges(float width, float edge) {
		super.loadFloat(location_borderWidth, width);
		super.loadFloat(location_borderEdge, edge);
	}
	
	public void loadOffset(Vector2f offset) {
		super.load2DVector(location_offset, offset);
	}
	
	public void loadColour(Vector3f colour){
		super.load3DVector(location_colour, colour);
	}
	
	public void loadOutlineColour(Vector3f colour){
		super.load3DVector(location_outlineColour, colour);
	}
	
	public void loadTranslation(Vector2f translation){
		super.load2DVector(location_translation, translation);
	}


}
