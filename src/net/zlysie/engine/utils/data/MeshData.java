package net.zlysie.engine.utils.data;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 * This object contains all the mesh data for an animated model that is to be loaded into the VAO.
 * 
 * @author Karl
 *
 */
public class MeshData {

	private static final int DIMENSIONS = 3;

	private float[] vertices;
	private float[] textureCoords;
	private float[] normals;
	private int[] indices;
	
	private boolean loadAnimated = true;
	
	private int[] jointIds;
	private float[] vertexWeights;

	public MeshData(float[] vertices, float[] textureCoords, float[] normals, int[] indices,
			int[] jointIds, float[] vertexWeights) {
		this.vertices = vertices;
		this.textureCoords = textureCoords;
		this.normals = normals;
		this.indices = indices;
		this.jointIds = jointIds;
		this.vertexWeights = vertexWeights;
	}
	
	public MeshData(float[] vertices, float[] textureCoords, float[] normals, int[] indices) {
		this.vertices = vertices;
		this.textureCoords = textureCoords;
		this.normals = normals;
		this.indices = indices;
		this.loadAnimated = false;
	}
	
	public boolean isAnimated() {
		return loadAnimated;
	}

	public int[] getJointIds() {
		return jointIds;
	}
	
	public float[] getVertexWeights(){
		return vertexWeights;
	}

	public List<Vertex> getVerticesClasses() {
		List<Vertex> vertices = new ArrayList<>();
		
		
		for (int i = 0; i < this.vertices.length/3; i++) {
			float x = (this.vertices[i * 3]);
			float y = (this.vertices[i * 3 + 1]);
			float z = (this.vertices[i * 3 + 2]);
			Vector4f position = new Vector4f(x, y, z, 1);
			vertices.add(new Vertex(vertices.size(), new Vector3f(position.x, position.y, position.z), null));
			
		}
		
		return vertices;
	}
	
	public float[] getVertices() {
		return vertices;
	}

	public float[] getTextureCoords() {
		return textureCoords;
	}

	public float[] getNormals() {
		return normals;
	}

	public int[] getIndices() {
		return indices;
	}

	public int getVertexCount() {
		return vertices.length / DIMENSIONS;
	}

}
