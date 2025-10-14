package net.zlysie.engine.renderers.animated;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import net.zlysie.engine.Camera;
import net.zlysie.engine.Light;
import net.zlysie.engine.renderers.ShaderProgram;
import net.zlysie.engine.utils.VectorMaths;

public class AnimatedModelShader extends ShaderProgram {

	private static final int MAX_JOINTS = 50;// max number of joints in a skeleton
	
	private static final String VERTEX_SHADER = "/shaders/animated.vert";
	private static final String FRAGMENT_SHADER = "/shaders/main.frag";

	protected int location_projectionMatrix;
	protected int location_viewMatrix;
	protected int location_transformationMatrix;
	protected int location_lightDirection;
	protected int[] location_jointTransforms;
	protected int location_lightPosition;
	protected int location_lightColour;
	protected int location_goraudEnabled;

	/**
	 * Creates the shader program for the {@link AnimatedModelRenderer} by
	 * loading up the vertex and fragment shader code files. It also gets the
	 * location of all the specified uniform variables, and also indicates that
	 * the diffuse texture will be sampled from texture unit 0.
	 */
	public AnimatedModelShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER);
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
		super.bindAttribute(3, "in_jointIndices");
		super.bindAttribute(4, "in_weights");
	}
	
	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_lightDirection = super.getUniformLocation("lightDirection");
		
		location_goraudEnabled = super.getUniformLocation("goraudEnabled");
		this.location_lightPosition = super.getUniformLocation("lightPosition");
		this.location_lightColour = super.getUniformLocation("lightColour");
		
		location_jointTransforms = new int[MAX_JOINTS];
		for(int i = 0; i < location_jointTransforms.length; i++) {
			location_jointTransforms[i] = super.getUniformLocation("jointTransforms["+i+"]");
		}
	}
	
	public void loadGoraud(boolean enabled) {
		super.loadBoolean(location_goraudEnabled, enabled);
	}
	
	public void loadLight(Light light) {
		super.loadVector3(location_lightPosition, light.getPosition());
		super.loadVector3(location_lightColour, light.getColour());
	}
	
	public void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}
	
	public void loadViewMatrix(Camera camera) {
		super.loadMatrix(location_viewMatrix, VectorMaths.createViewMatrix(camera));
	}
	
	public void loadTransformationMatrix(Matrix4f transformationMatrix) {
		super.loadMatrix(location_transformationMatrix, transformationMatrix);
	}

	public void loadJointTransforms(Matrix4f[] jointTransforms) {
		for(int i = 0; i < jointTransforms.length; i++) {
			if(jointTransforms[i] != null)
			super.loadMatrix(location_jointTransforms[i], jointTransforms[i]);
		}
		
	}

	public void loadLightDirection(Vector3f lightDir) {
		super.loadVector3(location_lightDirection, lightDir);
	}

	

}
