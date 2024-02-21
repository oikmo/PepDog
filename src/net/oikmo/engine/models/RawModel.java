package net.oikmo.engine.models;

/**
 * Holds raw model data
 * @author Oikmo
 */
public class RawModel {
	private int vaoID;
	private int vertexCount;

	public RawModel(int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}

	/**
	 * Returns Vertex Array Object (the object which holds the points of triangles in a model)
	 * @return VAO (int)
	 */
	public int getVaoID() {
		return vaoID;
	}
	
	/**
	 * Returns the amount of vertexes in the model.
	 * @return
	 */
	public int getVertexCount() {
		return vertexCount;
	}
}
