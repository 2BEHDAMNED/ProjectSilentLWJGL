package net.zlysie.engine.models;

import net.zlysie.engine.utils.data.MeshData;

public class RawModel {
	
	private int vaoID;
	private MeshData mesh;
	private int vertexCount;

	public RawModel(int vaoID, int vertexCount, MeshData mesh) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		this.mesh = mesh;
	}
	
	public RawModel(int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}
	
	public MeshData getMeshData() {
		return mesh;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
}
