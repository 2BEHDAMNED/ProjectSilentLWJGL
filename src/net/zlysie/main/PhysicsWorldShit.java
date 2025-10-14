package net.zlysie.main;

import static com.bulletphysics.demos.opengl.IGL.GL_COLOR_BUFFER_BIT;
import static com.bulletphysics.demos.opengl.IGL.GL_DEPTH_BUFFER_BIT;

import javax.vecmath.Vector3f;

import org.lwjgl.input.Keyboard;

import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.CollisionFilterGroups;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.dispatch.GhostPairCallback;
import com.bulletphysics.collision.dispatch.PairCachingGhostObject;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CapsuleShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.ConvexShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.character.KinematicCharacterController;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.util.ObjectArrayList;

import net.zlysie.engine.Camera;
import net.zlysie.engine.DisplayManager;

public class PhysicsWorldShit {
	
	private boolean idle; 
	private int gForward = 0;
	private int gBackward = 0;
	private int gLeft = 0;
	private int gRight = 0;
	private int gJump = 0;
	
	public KinematicCharacterController character;
	public PairCachingGhostObject ghostObject;
	
	// JAVA NOTE: the original demo scaled the bsp room, we scale up the character
	private float characterScale = 1f;
	
	// keep the collision shapes, for deletion/cleanup
	public ObjectArrayList<CollisionShape> collisionShapes = new ObjectArrayList<CollisionShape>();
	public BroadphaseInterface overlappingPairCache;
	public CollisionDispatcher dispatcher;
	public ConstraintSolver constraintSolver;
	public DefaultCollisionConfiguration collisionConfiguration;
	public DynamicsWorld dynamicsWorld;
	
	public void initPhysics() throws Exception {
		CollisionShape groundShape = new BoxShape(new Vector3f(50, 3, 50));
		collisionShapes.add(groundShape);

		collisionConfiguration = new DefaultCollisionConfiguration();
		dispatcher = new CollisionDispatcher(collisionConfiguration);
		Vector3f worldMin = new Vector3f(-1000f,-1000f,-1000f);
		Vector3f worldMax = new Vector3f(1000f,1000f,1000f);
		AxisSweep3 sweepBP = new AxisSweep3(worldMin, worldMax);
		overlappingPairCache = sweepBP;

		constraintSolver = new SequentialImpulseConstraintSolver();
		dynamicsWorld = new DiscreteDynamicsWorld(dispatcher,overlappingPairCache,constraintSolver,collisionConfiguration);

		Transform startTransform = new Transform();
		startTransform.setIdentity();
		startTransform.origin.set(0.0f, 4.0f, 0.0f);

		ghostObject = new PairCachingGhostObject();
		ghostObject.setWorldTransform(startTransform);
		sweepBP.getOverlappingPairCache().setInternalGhostPairCallback(new GhostPairCallback());
		float characterHeight = 1.75f * characterScale;
		float characterWidth = 1.75f * characterScale;
		ConvexShape capsule = new CapsuleShape(characterWidth, characterHeight);
		ghostObject.setCollisionShape(capsule);
		ghostObject.setCollisionFlags(CollisionFlags.CHARACTER_OBJECT);

		float stepHeight = 0.35f * characterScale;
		character = new KinematicCharacterController(ghostObject, capsule, stepHeight);
		
		ObjectArrayList<CollisionShape> shapes = new ObjectArrayList<>();
		shapes.add(groundShape);
		Transform groundTransform = new Transform();
		groundTransform.setIdentity();
		groundTransform.origin.set(new Vector3f(0.f, 0f, 0.f));
		CollisionObject obj = new CollisionObject();
		obj.setCollisionShape(groundShape);
		obj.setWorldTransform(groundTransform);
		dynamicsWorld.addCollisionObject(obj);
		
		dynamicsWorld.addCollisionObject(ghostObject, CollisionFilterGroups.CHARACTER_FILTER, (short)(CollisionFilterGroups.STATIC_FILTER | CollisionFilterGroups.DEFAULT_FILTER));

		dynamicsWorld.addAction(character);
	}
	
	public void input() {
		gForward = Keyboard.isKeyDown(Keyboard.KEY_W) ? 1 : 0;
		gBackward = Keyboard.isKeyDown(Keyboard.KEY_S) ? 1 : 0;
		gLeft = Keyboard.isKeyDown(Keyboard.KEY_A) ? 1 : 0;
		gRight = Keyboard.isKeyDown(Keyboard.KEY_D) ? 1 : 0;
		gJump = Keyboard.isKeyDown(Keyboard.KEY_SPACE) ? 1 : 0;
		
		idle = gForward == 0 && gBackward == 0 && gLeft == 0 && gRight == 0;
	}
	
	public void clientMoveAndDisplay(Camera camera) {
		float dt = DisplayManager.getFrameTime();
		
		input();

		if (dynamicsWorld != null) {
			// during idle mode, just run 1 simulation step maximum
			int maxSimSubSteps = idle ? 1 : 2;
			if (idle) {
				//dt = 1.0f / 420.f;
			}

			// set walkDirection for our character
			Transform xform = ghostObject.getWorldTransform(new Transform());

			Vector3f forwardDir = new Vector3f();
			xform.basis.getRow(2, forwardDir);
			//printf("forwardDir=%f,%f,%f\n",forwardDir[0],forwardDir[1],forwardDir[2]);
			Vector3f upDir = new Vector3f();
			xform.basis.getRow(1, upDir);
			Vector3f strafeDir = new Vector3f();
			xform.basis.getRow(0, strafeDir);
			forwardDir.normalize();
			upDir.normalize();
			strafeDir.normalize();

			Vector3f walkDirection = new Vector3f(0.0f, 0.0f, 0.0f);
			float walkVelocity = 1.1f * 4.0f; // 4 km/h -> 1.1 m/s
			float walkSpeed = walkVelocity * DisplayManager.getFrameTime() * 100 * characterScale;
			
			
			
			int vert = 0;
			if(gForward != 0) {
				vert = -1;
			} else if(gBackward != 0) {
				vert = 1;
			}
			
			int horz = 0;
			
			if(gLeft != 0) {
				horz = -1;
			} else if(gRight != 0) {
				horz = 1;
			}
			
			float distanceVert = vert * dt * 1000;
			float distanceHorz = horz * dt * 1000;
			
			float dVertX = (float) (distanceVert * Math.sin(Math.toRadians(-camera.getYaw())));
			float dVertZ = (float) (distanceVert * Math.cos(Math.toRadians(-camera.getYaw())));

			float dHorzX = (float) (distanceHorz * Math.sin(Math.toRadians(-camera.getYaw() + 90)));
			float dHorzZ = (float) (distanceHorz * Math.cos(Math.toRadians(-camera.getYaw() + 90)));
			
			strafeDir = new Vector3f(dHorzX+dVertX, 0, dHorzZ+dVertZ);
			
			System.out.println(strafeDir);
			
			
			walkDirection.add(strafeDir);
			
			if(gJump != 0) {
				if(character.canJump()) {
					character.jump();
				}
			}

			walkDirection.scale(walkSpeed);
			character.setWalkDirection(walkDirection);

			dynamicsWorld.stepSimulation(dt, maxSimSubSteps);

			// optional but useful: debug drawing
			dynamicsWorld.debugDrawWorld();
		}
	}
	
}
