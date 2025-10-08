package net.zlysie.engine;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

/**
 * Camera class. Allows the player to see the world.
 * 
 * @author <i>Oikmo</i>
 */
public class Camera {
	
	Vector3f position;

	public float pitch = 0;
	public float yaw = 0;
	public float roll = 0;
	int maxVerticalTurn = 90; // max angle
	
	
	/**
	 * Camera constructor. Sets position and rotation.
	 * 
	 * @param position
	 * @param rotation
	 * @param scale
	 */
	public Camera(Vector3f position, Vector3f rotation) {
		this.position = position;
		this.pitch = rotation.x;
		this.roll = rotation.z;
	}
	
	
	
	/**
	 * Moves camera based on given values.
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}
	/**
	 * Rotates the camera based on given values.
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public void increaseRotation(float dx, float dy, float dz) {
		this.pitch += dx;
		this.yaw += dy;
		this.roll += dz;
	}
	/**
	 * Sets the rotation of the camera based on given values.
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public void setRotation(float dx, float dy, float dz) {
		this.pitch = dx;
		this.yaw = dy;
		this.roll = dz;
	}
	
	/**
	 * Sets position to given 3D Vector
	 * @param vector
	 */
	public void setPosition(Vector3f v) {
		this.position = v;
	}

	private float speeds = 0f;
	private float speed = 0.1f;
	private float moveAt;
	
	/**
	 * Moves the camera in which direction the player is facing (when moving).<br>
	 * Fly cam function within else, just do lock on player.
	 */
	public void update(){
		//flyCam();
		System.out.println("update");
	}
	
	boolean lockInBalls = false;
	/**
	 * Fly cam (will most likely be temporary)
	 */
	public void move() {
		calculateZoom();
		if(Mouse.isGrabbed()) {
			Mouse.setCursorPosition(Display.getWidth()/2, Display.getHeight()/2);
		}
		
		if(Mouse.isGrabbed() != Mouse.isButtonDown(1)) {
			Mouse.setGrabbed(Mouse.isButtonDown(1));
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			speeds = 8;
		} else {
			speeds = 2;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			position.y += speed * speeds;
		} else if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
			position.y -= speed * speeds;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			moveAt = -speed * speeds;
		} else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			moveAt = speed * speeds;
		} else {
			moveAt = 0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			position.x += (float) -((speed * speeds) * Math.cos(Math.toRadians(yaw)));
			position.z -= (float) ((speed * speeds) * Math.sin(Math.toRadians(yaw)));
		} else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			position.x -= (float) -((speed * speeds) * Math.cos(Math.toRadians(yaw)));
			position.z += (float) ((speed * speeds) * Math.sin(Math.toRadians(yaw)));
		}
		
		if(Mouse.isGrabbed()) {
			pitch -= Mouse.getDY() * 0.3f*2;
			if(pitch < -maxVerticalTurn){
				pitch = -maxVerticalTurn;
			}else if(pitch > maxVerticalTurn){
				pitch = maxVerticalTurn;
			}
			yaw += Mouse.getDX() * 0.3f;
		}
		
		position.x += (float) -(moveAt * Math.sin(Math.toRadians(yaw)));
		position.y += (float) (moveAt * Math.sin(Math.toRadians(pitch)));
		position.z += (float) (moveAt * Math.cos(Math.toRadians(yaw)));
	}

	
	/**
	 * Returns position of the camera.
	 * @return
	 */
	public Vector3f getPosition() {
		return position;
	}
	
	public float getPitch() {
		return pitch;
	}
	public float getYaw() {
		return yaw;
	}
	public float getRoll() {
		return roll;
	}
	
	/**
	 * looksmaxxing
	 */
	private boolean lockIn = false;
	/**
	 * Calculates the scroll wheel or I/O keys to factor in how far the camera is using {@link #distanceFromPlayer}
	 */
	private void calculateZoom(){
		float zoomLevel = Mouse.getDWheel() * 0.05f;
		
		if(!lockIn) {
			if(Keyboard.isKeyDown(Keyboard.KEY_I)){
				zoomLevel = 2;
				lockIn = true;
			} else if(Keyboard.isKeyDown(Keyboard.KEY_O)) {
				zoomLevel = -2;
				lockIn = true;
			}
		} else { 
			if(!Keyboard.isKeyDown(Keyboard.KEY_I) && !Keyboard.isKeyDown(Keyboard.KEY_O)) {
				lockIn = false;
			}
		}
	}
}