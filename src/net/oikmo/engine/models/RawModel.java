package net.oikmo.engine.models;

import net.oikmo.toolbox.obj.PartialAABB;

public class RawModel {
	private int vaoID;
	private int vertexCount;
	private PartialAABB aabb;
	
	public RawModel(int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public PartialAABB getAABB() {
		return aabb;
	}
	
	public void setAABB(PartialAABB aabb) {
		this.aabb = aabb;
	}
	
}
