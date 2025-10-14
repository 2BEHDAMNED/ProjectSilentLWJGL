package net.zlysie.engine;

public class ModelTexture {

	private int textureID;

	public ModelTexture(int textureID) {
		this.textureID = textureID;
	}
	
	public ModelTexture(String texturePath) {
		this.textureID = Loader.loadTexture(texturePath);
	}

	public int getID() {
		return textureID;
	}
}
