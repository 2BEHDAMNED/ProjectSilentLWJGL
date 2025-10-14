package net.zlysie.main;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import net.zlysie.engine.Camera;
import net.zlysie.engine.DisplayManager;
import net.zlysie.engine.Entity;
import net.zlysie.engine.Light;
import net.zlysie.engine.Loader;
import net.zlysie.engine.animation.Animation;
import net.zlysie.engine.animation.loaders.AnimatedModelLoader;
import net.zlysie.engine.animation.loaders.AnimationLoader;
import net.zlysie.engine.models.animated.AnimatedModel;
import net.zlysie.engine.renderers.MasterRenderer;

public class Main {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		MasterRenderer renderer = new MasterRenderer();
		
		AnimatedModel model = AnimatedModelLoader.loadEntity("/models/cat/model.dae", "/models/cat/texture.png");
		Animation anim = AnimationLoader.loadAnimation("/models/cat/model.dae");
		model.doAnimation(anim);
		
		Entity entity = new Entity("/models/spartan/model.obj", "/models/spartan/texture.png");
		
		entity.setPosition(-2.5f, 0, 0);
		entity.setRotation(new Vector3f(0,270,0));
		entity.setScale(0.1f);
		
		model.setPosition(2.5f, 0, 0);
		model.setRotation(0,90f,0);
		

		Camera camera = new Camera(new Vector3f(0,2,10), new Vector3f(0, 0, 0));
		//camera.setPosition(0, 4, 0);
		//camera.setRotation(90, 0, 0);
		Light light = new Light(new Vector3f(0,0,0), new Vector3f(1,1,1));
		
		while(!Display.isCloseRequested()) {
			//entity.increaseRotation(0, 2, 0);
			camera.move();
			model.update();
			
			//light.setPosition(camera.getPosition().x, 1, camera.getPosition().z);
			
			renderer.processEntity(entity);
			renderer.processEntity(model);
			renderer.render(light, camera);
			
			DisplayManager.pollDisplay();
		}

		renderer.cleanUp();

		Loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
