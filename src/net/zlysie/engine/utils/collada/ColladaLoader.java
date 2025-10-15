package net.zlysie.engine.utils.collada;

import net.zlysie.engine.Loader;
import net.zlysie.engine.animation.data.AnimationData;
import net.zlysie.engine.models.RawModel;
import net.zlysie.engine.models.animated.AnimatedModelData;
import net.zlysie.engine.models.animated.SkeletonData;
import net.zlysie.engine.models.animated.SkinningData;
import net.zlysie.engine.utils.collada.xmlparser.XmlNode;
import net.zlysie.engine.utils.collada.xmlparser.XmlParser;
import net.zlysie.engine.utils.data.MeshData;

public class ColladaLoader {

	public static AnimatedModelData loadColladaModel(String colladaFile, int maxWeights) {
		XmlNode node = XmlParser.loadXmlFile(colladaFile);

		SkinLoader skinLoader = new SkinLoader(node.getChild("library_controllers"), maxWeights);
		SkinningData skinningData = skinLoader.extractSkinData();

		SkeletonLoader jointsLoader = new SkeletonLoader(node.getChild("library_visual_scenes"), skinningData.jointOrder);
		SkeletonData jointsData = jointsLoader.extractBoneData();

		GeometryLoader g = new GeometryLoader(node.getChild("library_geometries"), skinningData.verticesSkinData);
		MeshData meshData = g.extractModelData();

		return new AnimatedModelData(meshData, jointsData);
	}
	
	public static RawModel loadColladaModel(String colladaFile) {
		XmlNode node = XmlParser.loadXmlFile(colladaFile);

		GeometryLoader g = new GeometryLoader(node.getChild("library_geometries"), null);
		return Loader.loadToVAO(g.extractModelData());
	}


	public static AnimationData loadColladaAnimation(String colladaFile) {
		XmlNode node = XmlParser.loadXmlFile(colladaFile);
		XmlNode animNode = node.getChild("library_animations");
		XmlNode jointsNode = node.getChild("library_visual_scenes");
		AnimationLoader loader = new AnimationLoader(animNode, jointsNode);
		AnimationData animData = loader.extractAnimation();
		return animData;
	}

}
