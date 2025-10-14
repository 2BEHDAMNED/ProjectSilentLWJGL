package net.zlysie.engine.models.animated;

import net.zlysie.engine.animation.data.JointData;

public class SkeletonData {
	
	public final int jointCount;
	public final JointData headJoint;
	
	public SkeletonData(int jointCount, JointData headJoint){
		this.jointCount = jointCount;
		this.headJoint = headJoint;
	}

}
