package net.zlysie.engine.renderers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import net.zlysie.engine.Camera;
import net.zlysie.engine.Entity;
import net.zlysie.engine.Light;
import net.zlysie.engine.models.TexturedModel;
import net.zlysie.engine.models.animated.AnimatedModel;
import net.zlysie.engine.renderers.animated.AnimatedModelRenderer;
import net.zlysie.engine.renderers.normal.StaticRenderer;

public class MasterRenderer {

	private static final float FOV = 70f;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000f;
	
	private Matrix4f projectionMatrix;
	
	public MasterRenderer() {
		this.createProjectionMatrix();
	}
	
	private StaticRenderer renderer = new StaticRenderer();
	private AnimatedModelRenderer animatedRenderer = new AnimatedModelRenderer();
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
	private List<AnimatedModel> animatedEntities = new ArrayList<>();
	
	public void render(Light sun, Camera camera) {
		GL11.glClearColor(0.4140625f,0.390625f,0.4375f,1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		
		renderer.prepare();
		renderer.render(entities, projectionMatrix, sun, camera);
		animatedRenderer.render(animatedEntities, camera, projectionMatrix, sun);
		
		entities.clear();
		animatedEntities.clear();
	}
	
	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if(batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	public void processEntity(AnimatedModel entity) {
		if(animatedEntities.contains(entity)) {
			return;
		}
		animatedEntities.add(entity);
	}
	
	public void createProjectionMatrix(){
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;


		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}
	
	public void cleanUp() {
		renderer.cleanUp();
		animatedRenderer.cleanUp();
	}
}
