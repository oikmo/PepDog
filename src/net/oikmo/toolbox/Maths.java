package net.oikmo.toolbox;

public class Maths {
	public static javax.vecmath.Vector3f lwjglToVM(org.lwjgl.util.vector.Vector3f vector) {
		return new javax.vecmath.Vector3f(vector.x,vector.y,vector.z);
	}
}
