package net.zlysie.main;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import net.zlysie.engine.Camera;
import net.zlysie.engine.DisplayManager;
import net.zlysie.engine.Entity;
import net.zlysie.engine.Light;
import net.zlysie.engine.Loader;
import net.zlysie.engine.MasterRenderer;

public class Main {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		MasterRenderer renderer = new MasterRenderer();
		
		Entity entity = new Entity("/spartan.obj", "/spartan.png");
		Entity entity2 = new Entity("/silentcat.obj", "/cat.png");
		
		entity.setPosition(-2.5f, 0, 0);
		entity.setRotation(new Vector3f(0,270,0));
		
		entity2.setPosition(2.5f, 0, 0);
		entity2.setRotation(new Vector3f(0,270f,0));
		entity.setScale(0.1f);

		Camera camera = new Camera(new Vector3f(0,2,10), new Vector3f(0, 0, 0));
		//camera.setPosition(0, 4, 0);
		//camera.setRotation(90, 0, 0);
		Light light = new Light(new Vector3f(0,0,0), new Vector3f(1,1,1));
		
		while(!Display.isCloseRequested()) {
			//entity.increaseRotation(0, 2, 0);
			//entity2.increaseRotation(0, 2, 0);
			camera.move();
			
			//light.setPosition(camera.getPosition().x, 1, camera.getPosition().z);
			
			renderer.processEntity(entity);
			renderer.processEntity(entity2);
			renderer.render(light, camera);
			
			DisplayManager.pollDisplay();
		}

		renderer.cleanUp();

		Loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
