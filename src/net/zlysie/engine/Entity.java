package net.zlysie.engine;

import org.lwjgl.util.vector.Vector3f;

import net.zlysie.engine.models.RawModel;
import net.zlysie.engine.models.TexturedModel;
import net.zlysie.engine.utils.obj.ModelLoader;

public class Entity {

	private TexturedModel model;
	private Vector3f position = new Vector3f();
	private Vector3f rotation = new Vector3f();
	private float scale = 1.0f;
	
	public Entity(String path, String texture) {
		this.model = new TexturedModel(ModelLoader.loadOBJ(loader, path), new ModelTexture(texture));
	}
	
	public Entity(String path, int texture) {
		this.model = new TexturedModel(ModelLoader.loadOBJ(loader, path), new ModelTexture(texture));
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
