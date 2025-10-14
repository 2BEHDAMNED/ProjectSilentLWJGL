package net.zlysie.engine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

/**
 * Handles the window and also polling but WHO GIVE A FUCK
 * @author grace
 */
public class DisplayManager {

    private static long lastFrameTime;
    private static float delta;
	
	public static void createDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(640, 480));
			Display.setTitle("Project-SilentLWJGL");
			Display.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
		lastFrameTime = getCurrentTime();
	}

	public static void pollDisplay() {
		Display.sync(60);
		Display.update();
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currentFrameTime;
	}

	public static float getFrameTime() {
		return delta;
	}

	private static long getCurrentTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}


	public static void closeDisplay() {
		Display.destroy();
	}

}
