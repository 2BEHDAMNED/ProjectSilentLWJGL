package net.zlysie.engine.renderers.normal;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import net.zlysie.engine.Camera;
import net.zlysie.engine.Entity;
import net.zlysie.engine.Light;
import net.zlysie.engine.models.RawModel;
import net.zlysie.engine.models.TexturedModel;
import net.zlysie.engine.utils.VectorMaths;

public class StaticRenderer {
	
	private StaticShader shader;
	
	public StaticRenderer() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		shader = new StaticShader();
	
	}
	
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public void render(Map<TexturedModel, List<Entity>> entities, Matrix4f projectionMatrix, Light light, Camera camera) {
		shader.start();
		shader.loadGoraud(true);
		shader.loadLight(light);
		shader.loadViewMatrix(camera);
		
		shader.loadProjectionMatrix(projectionMatrix);
		
		for(TexturedModel model : entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			
			for(Entity e : batch) {
				prepareInstance(light, camera, e);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			
			unbindTexturedModel();
		}
		shader.stop();
	}
	
	private void prepareTexturedModel(TexturedModel texturedModel) {
		RawModel model = texturedModel.getRawModel();
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getID());
	}
	
	private void unbindTexturedModel() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void prepareInstance(Light light, Camera camera, Entity entity) {		
		shader.loadTransformationMatrix(VectorMaths.createTransformationMatrix(entity.getPosition(), entity.getRotation(), entity.getScale()));
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}
	
}
