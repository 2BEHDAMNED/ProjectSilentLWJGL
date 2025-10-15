package net.zlysie.engine;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;

import org.lwjgl.util.vector.Vector3f;

import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.shapes.BvhTriangleMeshShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.ConvexHullShape;
import com.bulletphysics.collision.shapes.IndexedMesh;
import com.bulletphysics.collision.shapes.TriangleIndexVertexArray;
import com.bulletphysics.linearmath.Transform;

import net.zlysie.engine.models.RawModel;
import net.zlysie.engine.models.TexturedModel;
import net.zlysie.engine.utils.data.MeshData;
import net.zlysie.engine.utils.obj.ModelLoader;

public class Entity {

	private TexturedModel model;
	private Vector3f position = new Vector3f();
	private Vector3f rotation = new Vector3f();
	private float scale = 1.0f;

	public Entity(String path, String texture) {
		this.model = new TexturedModel(ModelLoader.loadOBJ(path), new ModelTexture(Loader.loadTexture(texture)));
	}

	public Entity(String path, BufferedImage texture) {
		this.model = new TexturedModel(ModelLoader.loadOBJ(path), new ModelTexture(Loader.loadTexture(texture)));
	}

	public Entity(String path, int texture) {
		this.model = new TexturedModel(ModelLoader.loadOBJ(path), new ModelTexture(texture));
	}

	public Entity(RawModel model, BufferedImage texture, boolean generateCollisions) {
		this.model = new TexturedModel(model, new ModelTexture(Loader.loadTexture(texture)));
	}

	/**
	 * https://stackoverflow.com/questions/40855945/lwjgl-mesh-to-jbullet-collider
	 * @return
	 */
	public CollisionObject generateMesh(){
		MeshData mesh = this.model.getRawModel().getMeshData();
		if(mesh == null) {
			System.err.println("Literally no mesh to use!");
			return null;
		}
		float[] coords = mesh.getVertices();
		int[] indices = mesh.getIndices();

		if (indices.length > 0) { 

			IndexedMesh indexedMesh = new IndexedMesh();
			indexedMesh.numTriangles = indices.length / 3;
			indexedMesh.triangleIndexBase = ByteBuffer.allocateDirect(indices.length*Integer.BYTES).order(ByteOrder.nativeOrder());
			indexedMesh.triangleIndexBase.rewind();
			indexedMesh.triangleIndexBase.asIntBuffer().put(indices);
			indexedMesh.triangleIndexStride = 3 * Integer.BYTES;
			indexedMesh.numVertices = coords.length / 3;
			indexedMesh.vertexBase = ByteBuffer.allocateDirect(coords.length*Float.BYTES).order(ByteOrder.nativeOrder());
			indexedMesh.vertexBase.rewind();
			indexedMesh.vertexBase.asFloatBuffer().put(coords);
			indexedMesh.vertexStride = 3 * Float.BYTES;

			TriangleIndexVertexArray vertArray = new TriangleIndexVertexArray();
			vertArray.addIndexedMesh(indexedMesh);

			boolean useQuantizedAabbCompression = false;
			BvhTriangleMeshShape meshShape = new BvhTriangleMeshShape(vertArray, useQuantizedAabbCompression);
			//meshShape.setLocalScaling(new Vector3f(0.5f, 0.5f, 0.5f));

			CollisionShape collisionShape = meshShape;

			CollisionObject colObject = new CollisionObject();
			colObject.setCollisionShape(collisionShape);
			colObject.setWorldTransform(new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new javax.vecmath.Vector3f(position.x, position.y, position.z), 1f)));


			return colObject;
		} else {
			System.err.println("Failed to extract geometry from model. ");
		}

		return null;

	}

	public Entity(RawModel model, int texture, Vector3f position, Vector3f rotation, float scale) {
		this.model = new TexturedModel(model, new ModelTexture(texture));
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}

	public Entity(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}

	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	public void increaseRotation(float dx, float dy, float dz) {
		this.rotation.x = (this.rotation.x + dx) % 360;
		this.rotation.y = (this.rotation.y + dy) % 360;
		this.rotation.z = (this.rotation.z + dz) % 360;
	}

	public void setPosition(float dx, float dy, float dz) {
		this.position.x = dx;
		this.position.y = dy;
		this.position.z = dz;
	}

	public TexturedModel getModel() {
		return model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
}
