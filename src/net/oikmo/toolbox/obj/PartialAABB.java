package net.oikmo.toolbox.obj;

import org.lwjgl.util.vector.Vector3f;

public class PartialAABB {
	
	private Vector3f full;
	
	public PartialAABB(Vector3f min, Vector3f max, String obj) {

		min.x = Math.abs(min.x);
		if(obj.contentEquals("spartan")) {
			min.y = Math.abs(min.y) - 2;
		} else {
			min.y = Math.abs(min.y);
		}
		min.z = Math.abs(min.z);
		
		max.x = Math.abs(max.x);
		if(obj.contentEquals("spartan")) {
			max.y = Math.abs(max.y) - 2;
		} else {
			max.y = Math.abs(max.y);
		}
		max.z = Math.abs(max.z);
		
		this.full = Vector3f.add(min, max, new Vector3f());
	}
	
	public Vector3f getFull() {
		return full;
	}
}
