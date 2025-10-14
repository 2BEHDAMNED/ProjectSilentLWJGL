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
    private static long lastFPS;
    private static int fps, literalfps;
	
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
		updateFPS();
		
	}
	
	public static int getFPS() {
		return literalfps;
	}

	public static float getFrameTime() {
		return delta;
	}

	private static long getCurrentTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}

	static void updateFPS() {
		if (getCurrentTime() - lastFPS > 1000)	{
			literalfps = fps;
			fps = 0; //reset the FPS counter
			lastFPS += 1000; //add one second
			//System.gc();
		}
		fps++;
	}

	public static void closeDisplay() {
		Display.destroy();
	}

}
