package net.zlysie.main;

import java.awt.Color;

import javax.vecmath.Quat4f;

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
			//entity.increaseRotation(0, 2, 0);
			world.clientMoveAndDisplay(camera);
			world.ghostObject.getWorldTransform(transform);
			camera.move(transform);
			
			Quat4f q = transform.getRotation(new Quat4f());
			Vector3f eulers = VectorMaths.toEulerAngles(q);
			
			//world.character.setUpAxis((int)-camera.getYaw());
			
			//model.update();
			
			model.setPosition(transform.origin.x, transform.origin.y, transform.origin.z);
			model.setRotation(eulers.x, -camera.getYaw(), eulers.z);
			//model.setRotation(eulers.x,eulers.y, eulers.z);
			
			//light.setPosition(camera.getPosition().x, 0, camera.getPosition().z);
			
			renderer.processEntity(model);
			renderer.processEntity(entity);
			
			renderer.render(light, camera);
			//System.out.println(DisplayManager.getFPS());
			DisplayManager.pollDisplay();
			
			
		}

		renderer.cleanUp();

		Loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
