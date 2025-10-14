package net.zlysie.engine.renderers.animated;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import net.zlysie.engine.Camera;
import net.zlysie.engine.Light;
import net.zlysie.engine.models.animated.AnimatedModel;
import net.zlysie.engine.utils.VectorMaths;

/**
 * 
 * This class deals with rendering an animated entity. Nothing particularly new
 * here. The only exciting part is that the joint transforms get loaded up to
 * the shader in a uniform array.
 * 
 * @author Karl
 *
 */
public class AnimatedModelRenderer {

	private AnimatedModelShader shader;

	/**
	 * Initializes the shader program used for rendering animated models.
	 */
	public AnimatedModelRenderer() {
		this.shader = new AnimatedModelShader();
	}
	
	public void render(List<AnimatedModel> entities, Camera camera, Matrix4f projectionMatrix, Light light) {
		prepare(camera, projectionMatrix, light);
		
		for(AnimatedModel entity: entities) {
			GL30.glBindVertexArray(entity.getModel().getVaoID());
			loadAttribs(5);
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getTexture().getID());
			Matrix4f transformationMatrix = VectorMaths.createTransformationMatrix(entity.getPosition(), entity.getRotation(), 1f);
			
			shader.loadJointTransforms(entity.getJointTransforms());
			shader.loadTransformationMatrix(transformationMatrix);
			GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			
		}
		
		finish();
	}
	
	private void prepare(Camera camera, Matrix4f projectionMatrix, Light light) {
		shader.start();
		shader.loadViewMatrix(camera);
		shader.loadProjectionMatrix(projectionMatrix);
		shader.loadLight(light);
		shader.loadGoraud(true);
	}
	
	private void loadAttribs(int size) {
		for(int i = 0; i < size; i++) {
			GL20.glEnableVertexAttribArray(i);
		}
	}
	
	private void disableAttribs(int size) {
		for(int i = 0; i < size; i++) {
			GL20.glDisableVertexAttribArray(i);
		}
	}
	
	private void finish() {
		disableAttribs(5);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}

	

}
