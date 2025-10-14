package net.zlysie.main;

import java.awt.Color;

import javax.vecmath.Quat4f;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.bulletphysics.linearmath.Transform;

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
import net.zlysie.engine.utils.VectorMaths;

public class Main {

	public static void main(String[] args) throws Exception {
		DisplayManager.createDisplay();
		
		MasterRenderer renderer = new MasterRenderer();
		
		AnimatedModel model = AnimatedModelLoader.loadEntity("/models/cat/model.dae", "/models/cat/texture.png");
		Animation anim = AnimationLoader.loadAnimation("/models/cat/animation.dae");
		//model.doAnimation(anim);
		
		Entity entity = new Entity("/models/spartan/model.obj", "/models/spartan/texture.png");
		
		entity.setPosition(-2.5f, 0, 0);
		entity.setRotation(new Vector3f(0,270,0));
		entity.setScale(0.1f);
		
		model.setPosition(2.5f, 0, 0);

		Camera camera = new Camera(new Vector3f(0,10,10), new Vector3f());
		//camera.setPosition(0, 4, 0);
		//camera.setRotation(90, 0, 0);
		Light light = new Light(new Vector3f(0,1005,0), new Vector3f(0,0,0));
		
		PhysicsWorldShit world = new PhysicsWorldShit();
		world.initPhysics();
		
		Transform transform = new Transform();
		
		while(!Display.isCloseRequested()) {
			
			world.clientMoveAndDisplay(camera);
			world.ghostObject.getWorldTransform(transform);
			camera.move(transform);
			model.setPosition(transform.origin);
			if(!world.idle) {
				model.setRotation(0, -camera.getYaw()+world.calculateAngleFromDirection(), 0);
			}
			//System.out.println(-camera.getYaw());
			//light.setPosition(camera.getPosition().x, 0, camera.getPosition().z);
			
			renderer.processEntity(model);
			renderer.processEntity(entity);
			
			renderer.render(light, camera);
			DisplayManager.pollDisplay();
		}

		renderer.cleanUp();

		Loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
