package net.oikmo.toolbox;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.ReadableVector3f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import net.oikmo.main.entity.Camera;

/**
 * <i><b>Maths</b> class. Performs calculations for engine.<i>
 * 
 * @author Oikmo
 */
public class Toolbox {

	static Vector3f rxTable = new Vector3f(1,0,0);
	static Vector3f ryTable = new Vector3f(0,1,0);
	static Vector3f rzTable = new Vector3f(0,0,1);

	/**
	 * Creates and returns a transformation matrix so that 3d is real.<br><br>
	 * 
	 * {@code public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale)}
	 * 
	 * @param translation - to be positioned. <i>[Vector3f]</i>
	 * @param rx - X Rotation. <i>[float]</i>
	 * @param ry - Y Rotation. <i>[float]</i>
	 * @param rz - Z Rotation. <i>[float]</i>
	 * @param scale - to be scaled. <i>[float]</i>
	 * 
	 * @return <b>matrix</b> <i>[Matrix4f]</i>
	 * 
	 * @author <i>Oikmo</i>
	 */
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry,
			float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rx), rxTable, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), ryTable, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), rzTable, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale,scale,scale), matrix, matrix);
		return matrix;
	}

	/**
	 * Creates and returns a transformation matrix so that 3d is real.<br><br>
	 * 
	 * {@code public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale)}
	 * 
	 * @param translation - to be positioned. <i>[Vector3f]</i>
	 * @param rx - X Rotation. <i>[float]</i>
	 * @param ry - Y Rotation. <i>[float]</i>
	 * @param rz - Z Rotation. <i>[float]</i>
	 * @param scale - to be scaled. <i>[Vector3f]</i>
	 * 
	 * @return <b>matrix</b> <i>[Matrix4f]</i>
	 * 
	 * @author <i>Oikmo</i>
	 */
	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rot, Vector3f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rot.x), rxTable, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rot.y), ryTable, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rot.z), rzTable, matrix, matrix);
		Matrix4f.scale(scale, matrix, matrix);
		return matrix;
	}

	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}

	/**
	 * Creates and returns a view matrix using camera for perspective.<br><br>
	 * 
	 * {@code public static Matrix4f createViewMatrix(Camera camera)}
	 * 
	 * @param camera - Main Camera <i>[Camera]</i>
	 * 
	 * @return <b>viewMatrix</b> <i>[Matrix4f]</i>
	 * 
	 * @author <i>Oikmo</i>
	 */
	public static Matrix4f createViewMatrix(Camera camera){
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0,0,1));
		viewMatrix.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1,0,0));
		viewMatrix.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0,1,0));


		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		viewMatrix.translate(negativeCameraPos);
		return viewMatrix;
	}
	/**
	 * Lerp, allows you to transition numbers<br><br>
	 * 
	 * {@code public static float lerp(float start, float end, float amount)} 
	 * 
	 * @param start - starting number to interpolate from <i>[float]</i>
	 * @param end - end number to interpolate to <i>[float]</i>
	 * @param amount - amount to interpolate to and from <i>[float]</i>
	 * 
	 * @return <b>result</b> <i>[float]</i>
	 * 
	 * @author <i>Oikmo</i>
	 */
	public static float lerp(float start, float end, float amount) {
		return start + (amount)* (end - start);
	}

	public static Vector3f lerp(Vector3f start, Vector3f end, float amount) {
		float x = start.x + amount * (end.x - start.x);
		float y = start.y + amount * (end.y - start.y);
		float z = start.z + amount * (end.z - start.z);
		return new Vector3f(x,y,z);
	}

	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}

	public static boolean cooldown(long lastClick, long coolDownTime) {
		boolean result = false;
		long timeNow = System.currentTimeMillis();
		long time = timeNow - lastClick;
		if (time < 0 || time > coolDownTime) {
			lastClick = timeNow;
			result = true;
			// Trigger associated action
		}
		return result;
	} 

	public static Vector2f getNormalizedDeviceCoords(Vector2f position, Vector2f scale)
	{
		float x =  (((2f * position.x + scale.x) / Display.getWidth()) - 1f);
		float y = ((((2f * position.y +  scale.y) /Display.getHeight()) - 1f) * -1f);
		return new Vector2f(x, y);
	}

	public static long getSeedFromName(String name) {
		String finalString = "";
		for(int i = 0; i < name.length(); i++) {
			char ch = (char) name.getBytes()[i];
			int pos = Math.abs(ch - 'a' + 1);
			finalString += pos;
		}

		return Long.valueOf(finalString);
	}

	public static double clamp(double value, double min, double max) {
		return Math.max(min, Math.min(max, value));
	}   

	public static double sqrt(double d) {
		double sqrt = Double.longBitsToDouble( ( ( Double.doubleToLongBits( d )-(1l<<52) )>>1 ) + ( 1l<<61 ) );
		double better = (sqrt + d/sqrt)/2.0;
		double evenbetter = (better + d/better)/2.0;
		return evenbetter;
	}

	public static double square(double d) {		
		return d*d;
	}

	public static ReadableVector3f max(ReadableVector3f a, ReadableVector3f b) {
		return new Vector3f(
				Math.max(a.getX(), b.getX()),
				Math.max(a.getY(), b.getY()),
				Math.max(a.getZ(), b.getZ())
				);
	}

	public static float max(ReadableVector3f vector) {
		return Math.max(vector.getX(), Math.max(vector.getY(), vector.getZ()));
	}

	public static ReadableVector3f min(ReadableVector3f a, ReadableVector3f b) {
		return new Vector3f(
				Math.min(a.getX(), b.getX()),
				Math.min(a.getY(), b.getY()),
				Math.min(a.getZ(), b.getZ())
				);
	}

	public static float min(ReadableVector3f vector) {
		return java.lang.Math.min(vector.getX(), java.lang.Math.min(vector.getY(), vector.getZ()));
	}

	public static float easeOut(float t) {
		return t * (2 - t);
	}

	public static Quaternion EulerAnglesToQuaternion(Vector3f rotation) {
		Quaternion q = new Quaternion();
		double yaw = rotation.z;
		double pitch = rotation.y;
		double roll = rotation.x;
		double qx = Math.sin(roll/2) * Math.cos(pitch/2) * Math.cos(yaw/2) - Math.cos(roll/2) * Math.sin(pitch/2) * Math.sin(yaw/2);
		double qy = Math.cos(roll/2) * Math.sin(pitch/2) * Math.cos(yaw/2) + Math.sin(roll/2) * Math.cos(pitch/2) * Math.sin(yaw/2);
		double qz = Math.cos(roll/2) * Math.cos(pitch/2) * Math.sin(yaw/2) - Math.sin(roll/2) * Math.sin(pitch/2) * Math.cos(yaw/2);
		double qw = Math.cos(roll/2) * Math.cos(pitch/2) * Math.cos(yaw/2) + Math.sin(roll/2) * Math.sin(pitch/2) * Math.sin(yaw/2);
		
		q.x = (float) qx;
		q.y = (float) qy;
		q.z = (float) qz;
		q.w = (float) qw;
		return q;
	}
	static double RADTODEG = 180.0 / Math.PI;
	public static Vector3f MatrixToEulerEngles(Matrix3f matrix) {
		
		double EPS = 1.0e-6;
		double X, Y, Z;
		
		Y = Math.asin(matrix.m02);                       // Unique angle in [-pi/2,pi/2]

		if (Math.abs(Math.abs(matrix.m02) - 1.0) < EPS) { // Yuk! Gimbal lock. Infinite choice of X and Z
			X = Math.atan2(matrix.m21, matrix.m11);          // One choice amongst many
			Z = 0.0;
		} else {                                   // Unique solutions in (-pi,pi]
			X = Math.atan2(-matrix.m12, matrix.m22);         // atan2 gives correct quadrant and unique solutions
			Z = Math.atan2(-matrix.m01, matrix.m00);
		}

		X *= RADTODEG;   
		Y *= RADTODEG;   
		Z *= RADTODEG;

		return new Vector3f((float)X,(float)Y,(float)Z);
	}
}
