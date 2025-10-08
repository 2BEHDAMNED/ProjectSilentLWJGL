package net.zlysie.engine;

import org.lwjgl.util.vector.Vector3f;

public class Light {

	private Vector3f position;
	private Vector3f colour;
	
	public Light(Vector3f position, Vector3f colour) {
		this.position = position;
		this.colour = colour;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public Vector3f getColour() {
		return colour;
	}

	public void setPosition(float dx, float dy, float dz) {
		this.position.x = dx;
		this.position.y = dy;
		this.position.z = dz;
	}
	
}
