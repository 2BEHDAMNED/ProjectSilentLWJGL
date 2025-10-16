package net.zlysie.main;

import java.awt.Color;

import javax.vecmath.Quat4f;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.bulletphysics.collision.dispatch.CollisionObject;
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
import net.zlysie.engine.utils.collada.ColladaLoader;

public class Main {

	public static void main(String[] args) throws Exception {
		DisplayManager.createDisplay();
		
		MasterRenderer renderer = new MasterRenderer();
		
		AnimatedModel model = AnimatedModelLoader.loadEntity("/models/cat/model.dae", "/models/cat/texture.png");
		Animation anim = AnimationLoader.loadAnimation("/models/cat/animation.dae");
		model.doAnimation(anim);
		
		Entity entity = new Entity(ColladaLoader.loadColladaModel("/models/map.dae"), Loader.generateTexture(new Color(180, 180,180), 2,2), null);
		
		entity.setPosition(-2.5f, 0, 0);
		entity.setRotation(new Vector3f(0,0,0));
		//entity.setScale(0.1f);
		
		model.setPosition(2.5f, 0, 0);

		Camera camera = new Camera(new Vector3f(0,10,10), new Vector3f());
		//camera.setPosition(0, 4, 0);
		//camera.setRotation(90, 0, 0);
		Light light = new Light(new Vector3f(0,1005,0), new Vector3f(0,0,0));
		
		PhysicsWorldShit world = new PhysicsWorldShit();
		world.initPhysics();
		
		Transform transform = new Transform();
		
		CollisionObject obj = entity.generateMesh();
		obj.getWorldTransform(transform);
		
		entity.setRotation(VectorMaths.toEulerAngles(transform.getRotation(new Quat4f())));
		entity.setPosition(transform.origin);
		
		world.dynamicsWorld.addCollisionObject(obj);
		
		transform = new Transform();
		
		while(!Display.isCloseRequested()) {
			world.clientMoveAndDisplay(camera.getYaw());
			world.ghostObject.getWorldTransform(transform);
			camera.move(model, transform);
			
			model.setPosition(transform.origin);
			
			if(!world.idle) {
				model.setRotation(0, lerp(model.getRotation().y, -camera.getYaw()+world.calculateAngleFromDirection(), 0.2f), 0);
			}
			
			model.update();
			
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
	
	public static float lerp(float point1, float point2, float fraction) {
	    return (1 - fraction) * point1 + fraction * point2;
	  }
}
