package net.zlysie.main;

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
	
	boolean idle; 
	public Direction direction = Direction.IDLE;
	private int gForward = 0;
	private int gBackward = 0;
	private int gLeft = 0;
	private int gRight = 0;
	
	public KinematicCharacterController character;
	public PairCachingGhostObject ghostObject;
	
	// JAVA NOTE: the original demo scaled the bsp room, we scale up the character
	private float characterScale = 0.5f;
	
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
		//dynamicsWorld.addCollisionObject(obj);
		
		dynamicsWorld.addCollisionObject(ghostObject, CollisionFilterGroups.CHARACTER_FILTER, (short)(CollisionFilterGroups.STATIC_FILTER | CollisionFilterGroups.DEFAULT_FILTER));

		dynamicsWorld.addAction(character);
	}
	
	public void input() {
		
		gForward = Keyboard.isKeyDown(Keyboard.KEY_W) ? 1 : 0;
		gBackward = Keyboard.isKeyDown(Keyboard.KEY_S) ? 1 : 0;
		gLeft = Keyboard.isKeyDown(Keyboard.KEY_A) ? 1 : 0;
		gRight = Keyboard.isKeyDown(Keyboard.KEY_D) ? 1 : 0;
		
		if(gForward == 1 && gLeft == 1) {
			direction = Direction.FORWARDSLEFT;
		} else if(gForward == 1 && gRight == 1) {
			direction = Direction.FORWARDSRIGHT;
		} else if(gBackward == 1 && gLeft == 1) {
			direction = Direction.BACKWARDSLEFT;
		} else if(gBackward == 1 && gRight == 1) {
			direction = Direction.BACKWARDSRIGHT;
		} else {
			if(gForward == 1) {
				direction = Direction.FORWARDS;
			} else if(gBackward == 1) {
				direction = Direction.BACKWARDS;
			} else if(gLeft == 1) {
				direction = Direction.LEFT;
			} else if(gRight == 1) {
				direction = Direction.RIGHT;
			} 
		}
		
		idle = gForward == 0 && gBackward == 0 && gLeft == 0 && gRight == 0;
		direction = idle ? Direction.IDLE : direction;
	}
	
	private float angle = 0;
	
	public float calculateAngleFromDirection() {
		float angle = 0;
		switch(direction) {
		case BACKWARDSLEFT:
			angle = 135;
			break;
		case BACKWARDSRIGHT:
			angle = -135;
			break;
		case FORWARDSLEFT:
			angle = 45;
			break;
		case FORWARDSRIGHT:
			angle = -45;
			break;
		case BACKWARDS:
			angle = 180;
			break;
		case LEFT:
			angle = 90;
			break;
		case RIGHT:
			angle = -90;
			break;
		case FORWARDS:
			angle = 0;
			break;
		default:
			angle = this.angle;
			break;
		}
		
		this.angle = angle;
		
		return angle;
	}
	
	public void clientMoveAndDisplay(float yaw) {
		float dt = DisplayManager.getFrameTime();
		
		input();

		if (dynamicsWorld != null) {
			Vector3f walkDirection = new Vector3f(0.0f, 0.0f, 0.0f);
			
			if(!idle) {
				float walkVelocity = 1.1f * 4.0f; // 4 km/h -> 1.1 m/s
				float walkSpeed = walkVelocity * DisplayManager.getFrameTime() * 100 * characterScale;
				
				int vert = 0;
				     if(gForward != 0) { vert = -1; } 
				else if(gBackward != 0) { vert = 1; }
				
				int horz = 0;
				     if(gLeft != 0) { horz = -1; } 
				else if(gRight != 0) { horz = 1; }
				
				float distanceVert = vert * walkSpeed;
				float distanceHorz = horz * walkSpeed;
				
				float dVertX = (float) (distanceVert * Math.sin(Math.toRadians(-yaw)));
				float dVertZ = (float) (distanceVert * Math.cos(Math.toRadians(-yaw)));

				float dHorzX = (float) (distanceHorz * Math.sin(Math.toRadians(-yaw + 90)));
				float dHorzZ = (float) (distanceHorz * Math.cos(Math.toRadians(-yaw + 90)));
				
				Vector3f strafeDir = new Vector3f(dHorzX+dVertX, 0, dHorzZ+dVertZ);
				strafeDir.normalize();
				strafeDir.scale(0.05f);
				
				if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && character.canJump()) {
					character.jump();
				}
				
				walkDirection.add(strafeDir);
				
				walkDirection.scale(walkSpeed);
				
			}
			character.setWalkDirection(walkDirection);
			dynamicsWorld.stepSimulation(dt, 2);
		}
	}
	
	public static enum Direction {
		IDLE,
		FORWARDS,
		FORWARDSLEFT,
		FORWARDSRIGHT,
		BACKWARDS,
		BACKWARDSLEFT,
		BACKWARDSRIGHT,
		LEFT,
		RIGHT
	}
	
}
