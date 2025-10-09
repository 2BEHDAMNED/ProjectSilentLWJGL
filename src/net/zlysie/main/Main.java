package net.zlysie.main;

import java.awt.Color;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import net.zlysie.engine.Camera;
import net.zlysie.engine.DisplayManager;
import net.zlysie.engine.Entity;
import net.zlysie.engine.Light;
import net.zlysie.engine.Loader;
import net.zlysie.engine.MasterRenderer;
import net.zlysie.engine.models.RawModel;
import net.zlysie.engine.utils.obj.ModelLoader;

public class Main {

	public static void main(String[] args) {
		DisplayManager.createDisplay();

		Loader loader = new Loader();
		MasterRenderer renderer = new MasterRenderer();
		
		RawModel model = ModelLoader.loadOBJ(loader, "/lwjgl-player.obj");
		Entity entity = new Entity(model, loader.loadTexture(loader.generateTexture(Color.gray, 64, 64)));

		entity.setPosition(0, 0, 0);
		entity.setRotation(new Vector3f(0,180f,0));
		entity.setScale(1f);

		Camera camera = new Camera(new Vector3f(0,10,0), new Vector3f(90, 0, 0));
		//camera.setPosition(0, 4, 0);
		//camera.setRotation(90, 0, 0);
		Light light = new Light(new Vector3f(0,10,5), new Vector3f(3,3,3));
		
		while(!Display.isCloseRequested()) {
			//entity.increaseRotation(0, 1, 0);
			camera.move();
			
		//	light.setPosition(camera.getPosition().x, 1, camera.getPosition().z);
			
			renderer.processEntity(entity);
			renderer.render(light, camera);
			
			DisplayManager.pollDisplay();

		}

		renderer.cleanUp();

		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
