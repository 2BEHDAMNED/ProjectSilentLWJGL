package net.zlysie.engine.utils;

import javax.vecmath.Quat4f;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import net.zlysie.engine.Camera;

public class VectorMaths {
	
	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, float scale) {
		return createTransformationMatrix(translation, rotation, new Vector3f(scale,scale,scale));
	}	
	
	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, Vector3f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate(toRadians(rotation.x), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.rotate(toRadians(rotation.y%360), new Vector3f(0,1,0), matrix, matrix);
		//System.out.println(rotation.y);
		Matrix4f.rotate(toRadians(rotation.z), new Vector3f(0,0,1), matrix, matrix);
		Matrix4f.scale(scale, matrix, matrix);
		return matrix;
	}
	
	private static float toRadians(float rotation) {
		return (float) Math.toRadians(rotation);
	}
	

	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.rotate(toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix,
				viewMatrix);
		Matrix4f.rotate(toRadians(camera.getYaw()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		return viewMatrix;
	}

	public static Quat4f toQuaternion(Vector3f v) // roll (x), pitch (y), yaw (z), angles are in radians
	{
	    // Abbreviations for the various angular functions
		
		double roll = v.x;
		double pitch = v.y;
		double yaw = v.z;
		
	    double cr = Math.cos(roll * 0.5);
	    double sr = Math.sin(roll * 0.5);
	    double cp = Math.cos(pitch * 0.5);
	    double sp = Math.sin(pitch * 0.5);
	    double cy = Math.cos(yaw * 0.5);
	    double sy = Math.sin(yaw * 0.5);

	    Quat4f q = new Quat4f();
	    q.w = (float) (cr * cp * cy + sr * sp * sy);
	    q.x = (float) (sr * cp * cy - cr * sp * sy);
	    q.y = (float) (cr * sp * cy + sr * cp * sy);
	    q.z = (float) (cr * cp * sy - sr * sp * cy);

	    return q;
	}
	
	public static Vector3f toEulerAngles(Quat4f q) {
	    // roll (x-axis rotation)
	    double sinr_cosp = 2 * (q.w * q.x + q.y * q.z);
	    double cosr_cosp = 1 - 2 * (q.x * q.x + q.y * q.y);
	    double roll = Math.atan2(sinr_cosp, cosr_cosp);

	    // pitch (y-axis rotation)
	    double sinp = Math.sqrt(1 + 2 * (q.w * q.y - q.x * q.z));
	    double cosp = Math.sqrt(1 - 2 * (q.w * q.y - q.x * q.z));
	    double pitch = 2 * Math.atan2(sinp, cosp) - Math.PI / 2;

	    // yaw (z-axis rotation)
	    double siny_cosp = 2 * (q.w * q.z + q.x * q.y);
	    double cosy_cosp = 1 - 2 * (q.y * q.y + q.z * q.z);
	    double yaw = Math.atan2(siny_cosp, cosy_cosp);
	    
	    return new Vector3f((float)pitch, (float)yaw, (float)roll);
	}

}
