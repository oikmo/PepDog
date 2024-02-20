package net.oikmo.engine.collision;

import org.lwjgl.util.vector.Vector3f;

/**
 * Used to store collision info to resolve collision.
 * @author Oikmo
 *
 */
public class CollisionPacket {
	
	public Vector3f distance;
	public boolean intersecting;
	
	public CollisionPacket(Vector3f distance, boolean intersecting) {
		this.distance = distance;
		this.intersecting = intersecting;
	}
	 @Override
	public String toString() {
		 return "Collision [dist(" + distance.x + ", " + distance.y + ", " + distance.z + ") intersecting:" + Boolean.toString(intersecting) + "]";
	 }
}
