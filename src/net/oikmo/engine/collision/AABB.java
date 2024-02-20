package net.oikmo.engine.collision;

import java.text.DecimalFormat;

import org.lwjgl.util.vector.Vector3f;

import net.oikmo.toolbox.obj.PartialAABB;

/**
 * Axis Aligned Bounding Box.<br>
 * Collision boxes with no rotation :P
 * 
 * @author Oikmo
 */
public class AABB {
	
	private Vector3f center, half_extent;

	public AABB(Vector3f center, PartialAABB partial) {
		this.center = new Vector3f(center);
		this.half_extent = partial.getFull();
	}
	
	public AABB(Vector3f center, Vector3f halfExtents) {
		this.center = new Vector3f(center);
		this.half_extent = new Vector3f(halfExtents);
	}
	
	public CollisionPacket intersects(AABB other) {
		Vector3f dist = Vector3f.sub(other.center, center, new Vector3f());
		dist.x = (float) Math.abs(dist.x);
		dist.y = (float) Math.abs(dist.y);
		dist.z = (float) Math.abs(dist.z);
		
		Vector3f temp = new Vector3f();
        temp = Vector3f.add(half_extent, other.half_extent, temp);
        //Logger.log(LogLevel.INFO, temp.toString());
        dist = Vector3f.sub(dist, temp, dist);
		
        return new CollisionPacket(dist, dist.x < 0 && dist.y < 0 && dist.z < 0);
	}
	
	public void correctPosition(AABB other, CollisionPacket data) {
		Vector3f correction = Vector3f.sub(other.center, center, new Vector3f());
		
		if (data.distance.x > data.distance.y && data.distance.x > data.distance.z) {
			if (correction.x > 0) {
				center.translate(data.distance.x, 0, 0);
			} else {
				center.translate(-data.distance.x, 0, 0);
			}
		} else if (data.distance.y > data.distance.z) {
			if (correction.y > 0) {
				center.translate(0, data.distance.y, 0);
			} else {
				center.translate(0, -data.distance.y, 0);
			}
		} else {
			if (correction.z > 0) {
				center.translate(0, 0, data.distance.z);
			} else {
				center.translate(0, 0, -data.distance.z);
			}
		}
	}
	
	public boolean isPointInside(Vector3f point) {
		
		Vector3f min = Vector3f.sub(this.getCenter(), this.getHalfExtent(), null);
		Vector3f max = Vector3f.add(this.getCenter(), this.getHalfExtent(), null);
		
        if(point.x <= min.x || point.x >= max.x) {
            return false;
        }
        if(point.y <= min.y || point.y >= max.y) {
            return false;
        }
        return point.z > min.z && point.z < max.z;
    }
	
	DecimalFormat format = new DecimalFormat("#.#");
	public String getPrintPosition() {
		return "AABB[X:"+format.format(getCenter().x)+", Y:"+format.format(getCenter().y)+", Z:"+format.format(getCenter().z) + "  X:" + format.format(getHalfExtent().x)+", Y:"+format.format(getHalfExtent().y)+", Z:"+format.format(getHalfExtent().z)+"]";
	}
	
	public Vector3f getCenter() { return center; }
	public void setHalfExtent(float x, float y, float z) {
		this.center.x = x;
		this.center.y = y;
		this.center.z = z;
	}
	public Vector3f getHalfExtent() { return half_extent; }
	
}
