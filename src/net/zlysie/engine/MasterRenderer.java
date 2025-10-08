package net.zlysie.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zlysie.engine.models.TexturedModel;

public class MasterRenderer {

	private Renderer renderer = new Renderer();
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
	
	public void render(Light sun, Camera camera) {
		renderer.prepare();
		renderer.render(entities, sun, camera);
		entities.clear();
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
	
	public void cleanUp() {
		renderer.cleanUp();
	}
}
