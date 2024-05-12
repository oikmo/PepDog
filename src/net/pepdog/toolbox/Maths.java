package net.pepdog.toolbox;

import java.nio.FloatBuffer;

import javax.vecmath.Matrix3f;
import javax.vecmath.Quat4f;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Vectors, Maths, Rotations??<br>
 * It's in here and I am definetly not losing my mind!
 * @author Oikmo
 *
 */
public class Maths {
	
	/**
	 * Converts {@link org.lwjgl.util.vector.Vector3f} to {@link javax.vecmath.Vector3f}
	 * @param vector
	 * @return
	 */
	public static void lwjglToVM(org.lwjgl.util.vector.Vector3f vector, javax.vecmath.Vector3f v) {
		v.x = vector.x;
		v.y = vector.y;
		v.z = vector.z;
	}
	
	/**
	 * Converts {@link javax.vecmath.Vector3f} to {@link org.lwjgl.util.vector.Vector3f}
	 * @param vector
	 * @return
	 */
	public static void VMtoLWJGL(javax.vecmath.Vector3f vector, Vector3f v) {
		v.x = vector.x;
		v.y = vector.y;
		v.z = vector.z;
	}
	
	/**
	 * Converts Quaternion rotations to Euler Angles
	 * @param q
	 * @return {@link Vector3f}
	 */
	public static void QuaternionToEulerAngles(Quat4f q, Vector3f v) {

		double t0 = +2.0 * (q.w * q.x + q.y * q.z);
		double t1 = +1.0 - 2.0 * (q.x * q.x + q.y * q.y);
		v.x = (float) Math.toDegrees(FastMath.atan2((float)t0, (float)t1));

		double t2 = +2.0 * (q.w * q.y - q.z * q.x);
		t2 = t2 > 1.0f ? 1.0f : t2;
		t2 = t2 < -1.0f ? -1.0f : t2;
		v.y = (float) Math.toDegrees(FastMath.asin((float)t2));

		double t3 = +2.0 * (q.w * q.z + q.x * q.y);
		double t4 = +1.0 - 2.0 * (q.y * q.y + q.z * q.z);
		v.z = (float) Math.toDegrees(FastMath.atan2((float)t3, (float)t4));
	}

	/**
	 * 
	 * Converts Euler Angles (Vector3f) to Quaternion
	 * 
	 * @param e [Vector3f]
	 * @return Quaternion [Quat4f]
	 */
	@Deprecated
	public static void ToQuaternion(Vector3f e, Quat4f q) {
		float roll = e.x;
		float pitch = e.y;
		float yaw = e.z;
		float qx = (float) (FastMath.sin(roll/2) * FastMath.cos(pitch/2) * FastMath.cos(yaw/2) - FastMath.cos(roll/2) * FastMath.sin(pitch/2) * FastMath.sin(yaw/2));
		float qy = (float) (FastMath.cos(roll/2) * FastMath.sin(pitch/2) * FastMath.cos(yaw/2) + FastMath.sin(roll/2) * FastMath.cos(pitch/2) * FastMath.sin(yaw/2));
		float qz = (float) (FastMath.cos(roll/2) * FastMath.cos(pitch/2) * FastMath.sin(yaw/2) - FastMath.sin(roll/2) * FastMath.sin(pitch/2) * FastMath.cos(yaw/2));
		float qw = (float) (FastMath.cos(roll/2) * FastMath.cos(pitch/2) * FastMath.cos(yaw/2) + FastMath.sin(roll/2) * FastMath.sin(pitch/2) * FastMath.sin(yaw/2));
		q.x = qx;
		q.y = qy;
		q.z = qz;
		q.w = qw;
	}
	
	public static void QuaternionToEulerUsingMatrix(Quat4f q, Vector3f v) {
		Matrix3f m = new Matrix3f();
		m.set(q);
		Maths.MatrixToEulerEngles(m, v);
	}
	
	static double RADTODEG = 180.0 / Math.PI;
	/**
	 * Converts rotational Matrix ({@linkplain javax.vecmath.Matrix3f}) to Euler Angles
	 * @param matrix
	 * @return
	 */
	public static void MatrixToEulerEngles(Matrix3f matrix, Vector3f v) {

		double EPS = 1.0e-6;

		v.y = FastMath.asin(matrix.m02);                       // Unique angle in [-pi/2,pi/2]

		if (FastMath.abs((float)(FastMath.abs(matrix.m02) - 1.0)) < EPS) { // Yuk! Gimbal lock. Infinite choice of X and Z
			v.x = FastMath.atan2(matrix.m21, matrix.m11);          // One choice amongst many
			v.z = 0.0f;
		} else {                                   // Unique solutions in (-pi,pi]
			v.x = FastMath.atan2(-matrix.m12, matrix.m22);         // atan2 gives correct quadrant and unique solutions
			v.z = FastMath.atan2(-matrix.m01, matrix.m00);
		}

		v.x *= RADTODEG;   
		v.y *= RADTODEG;   
		v.z *= RADTODEG;
	}
	
	public static void matrixToBuffer(Matrix4f m, FloatBuffer dest) {
        matrixToBuffer(m, 0, dest);
    }
    public static void matrixToBuffer(Matrix4f m, int offset, FloatBuffer dest) {
        dest.put(offset, m.m00);
        dest.put(offset + 1, m.m01);
        dest.put(offset + 2, m.m02);
        dest.put(offset + 3, m.m03);
        dest.put(offset + 4, m.m10);
        dest.put(offset + 5, m.m11);
        dest.put(offset + 6, m.m12);
        dest.put(offset + 7, m.m13);
        dest.put(offset + 8, m.m20);
        dest.put(offset + 9, m.m21);
        dest.put(offset + 10, m.m22);
        dest.put(offset + 11, m.m23);
        dest.put(offset + 12, m.m30);
        dest.put(offset + 13, m.m31);
        dest.put(offset + 14, m.m32);
        dest.put(offset + 15, m.m33);
    }
}
