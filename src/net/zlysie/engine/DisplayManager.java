package net.zlysie.engine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

/**
 * Handles the window and also polling but WHO GIVE A FUCK
 * @author grace
 */
public class DisplayManager {
	
	public static void createDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(1280, 720));
			Display.setTitle("Learning LWJGL... Again?");
			Display.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}
	
	public static void pollDisplay() {
		Display.sync(60);
		Display.update();
	}
	
	public static void closeDisplay() {
		Display.destroy();
	}
	
}
