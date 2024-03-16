package net.oikmo.main;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;

import net.oikmo.engine.DisplayManager;

/**
 * Handles physics (bullet)
 * @author Oikmo
 *
 */
public class PhysicsSystem {
	
	private static DiscreteDynamicsWorld world;
	
	/**
	 * Creates the physics system such as Collision Configuration, Constraint Resolvers etc
	 */
	public static void init() {
		CollisionConfiguration collisionConfig = new DefaultCollisionConfiguration();
		CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfig);
		Vector3f worldAabbMin = new Vector3f(-10000, -10000, -10000);
		Vector3f worldAabbMax = new Vector3f(10000, 10000, 10000);
		int maxProxies = 1024*10;
		AxisSweep3 overlappingPairCache = new AxisSweep3(worldAabbMin, worldAabbMax, maxProxies);
		SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();
		world = new DiscreteDynamicsWorld(dispatcher, overlappingPairCache, solver, collisionConfig);
		world.setGravity(new Vector3f(0, -50, 0));
		
		/**CollisionShape groundShape = new BoxShape(new Vector3f(10000.f, 10.f, 10000.f));
		ObjectArrayList<CollisionShape> shapes = new ObjectArrayList<>();
		shapes.add(groundShape);
		Transform groundTransform = new Transform();
		groundTransform.setIdentity();
		groundTransform.origin.set(new Vector3f(0.f, 0f, 0.f));
		CollisionObject obj = new CollisionObject();
		obj.setCollisionShape(groundShape);
		obj.setWorldTransform(groundTransform);
		world.addCollisionObject(obj);*/
	}
	
	/**
	 * Returns the physics world.
	 * @return DiscreteDynamicsWorld
	 */
	public static DiscreteDynamicsWorld getWorld() {
		return world;
	}
	
	/**
	 * Steps the world simulation using deltatime from {@link DisplayManager#getFrameTimeSeconds()}
	 */
	public static void update() {
		world.stepSimulation(DisplayManager.getFrameTimeSeconds());
	}
}
