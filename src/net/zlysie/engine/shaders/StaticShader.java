package net.zlysie.engine.shaders;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import net.zlysie.engine.Camera;
import net.zlysie.engine.Light;
import net.zlysie.engine.utils.VectorMaths;

public class StaticShader extends ShaderProgram{
	
	private static final String VERTEX_FILE = "main.vert";
	private static final String FRAGMENT_FILE = "main.frag";
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition;
	private int location_lightColour;
	private int location_goraudEnabled;
	private int location_targetResolution;
	
	@Override
	protected void getAllUniformLocations() {
		this.location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		this.location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		this.location_viewMatrix = super.getUniformLocation("viewMatrix");

		this.location_lightPosition = super.getUniformLocation("lightPosition");
		this.location_lightColour = super.getUniformLocation("lightColour");
		this.location_targetResolution = super.getUniformLocation("targetResolution");
	}
	
	public void loadTargetResultion() {
		super.loadVector2(location_targetResolution, new Vector2f(Display.getWidth(), Display.getHeight()));
	}
	
	public void loadGoraud(boolean enabled) {
		super.loadBoolean(location_goraudEnabled, enabled);
	}
	
	public void loadLight(Light light) {
		super.loadVector3(location_lightPosition, light.getPosition());
		super.loadVector3(location_lightColour, light.getColour());
	}
	
	public void loadTransformationMatrix(Matrix4f transformation) {
		super.loadMatrix(location_transformationMatrix, transformation);
	}
	
	public void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadViewMatrix(Camera camera) {
		super.loadMatrix(location_viewMatrix, VectorMaths.createViewMatrix(camera));
	}
}
