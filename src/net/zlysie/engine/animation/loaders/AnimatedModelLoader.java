package net.zlysie.engine.animation.loaders;

import net.zlysie.engine.Loader;
import net.zlysie.engine.ModelTexture;
import net.zlysie.engine.animation.Joint;
import net.zlysie.engine.animation.data.JointData;
import net.zlysie.engine.models.RawModel;
import net.zlysie.engine.models.animated.AnimatedModel;
import net.zlysie.engine.models.animated.AnimatedModelData;
import net.zlysie.engine.models.animated.SkeletonData;
import net.zlysie.engine.utils.collada.ColladaLoader;
import net.zlysie.engine.utils.data.MeshData;

public class AnimatedModelLoader {
	
	private static final int MAX_WEIGHTS = 3;

	/**
	 * Creates an AnimatedEntity from the data in an entity file. It loads up
	 * the collada model data, stores the extracted data in a VAO, sets up the
	 * joint heirarchy, and loads up the entity's texture.
	 * 
	 * @param entityFile
	 *            - the file containing the data for the entity.
	 * @return The animated entity (no animation applied though)
	 */
	public static AnimatedModel loadEntity(String modelFile, String textureFile) {
		AnimatedModelData entityData = ColladaLoader.loadColladaModel(modelFile, MAX_WEIGHTS);
		RawModel model = createVao(entityData.getMeshData());
		ModelTexture texture = new ModelTexture(textureFile);
		SkeletonData skeletonData = entityData.getJointsData();
		Joint headJoint = createJoints(skeletonData.headJoint);
		return new AnimatedModel(model, texture, headJoint, skeletonData.jointCount);
	}

	/**
	 * Constructs the joint-hierarchy skeleton from the data extracted from the
	 * collada file.
	 * 
	 * @param data
	 *            - the joints data from the collada file for the head joint.
	 * @return The created joint, with all its descendants added.
	 */
	private static Joint createJoints(JointData data) {
		Joint joint = new Joint(data.index, data.nameId, data.bindLocalTransform);
		for (JointData child : data.children) {
			joint.addChild(createJoints(child));
		}
		return joint;
	}

	/**
	 * Stores the mesh data in a VAO.
	 * 
	 * @param data
	 *            - all the data about the mesh that needs to be stored in the
	 *            VAO.
	 * @return The VAO containing all the mesh data for the model.
	 */
	private static RawModel createVao(MeshData data) {
		return Loader.loadToVAO(data);
	}

}
