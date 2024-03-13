package net.oikmo.toolbox;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
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

	/**
	 * Returns Window size as OpenGL coordinates.
	 * @param position
	 * @param scale
	 * @return {@link Vector3f}
	 */
	public static Vector2f getNormalizedDeviceCoords(Vector2f position, Vector2f scale) {
		float x =  (((2f * position.x + scale.x) / Display.getWidth()) - 1f);
		float y = ((((2f * position.y +  scale.y) /Display.getHeight()) - 1f) * -1f);
		return new Vector2f(x, y);
	}

	/**
	 * Converts string to long via taking each character of the string and converting it into a number. Then that number is added to string to be parsed to {@link Long#valueOf(String)}
	 * @param name
	 * @return {@link Long}
	 */
	public static long getSeedFromName(String name) {
		String finalString = "";
		for(int i = 0; i < name.length(); i++) {
			char ch = (char) name.getBytes()[i];
			int pos = Math.abs(ch - 'a' + 1);
			finalString += pos;
		}

		return Long.valueOf(finalString);
	}
	
	/**
	 * Clamps given value to given range.
	 * @param value
	 * @param min
	 * @param max
	 * @return {@link Double}
	 */
	public static double clamp(double value, double min, double max) {
		return Math.max(min, Math.min(max, value));
	}   

	/**
	 * fast sqaure root
	 * @param d
	 * @return {@link Double}
	 */
	public static double sqrt(double d) {
		double sqrt = Double.longBitsToDouble( ( ( Double.doubleToLongBits( d )-(1l<<52) )>>1 ) + ( 1l<<61 ) );
		double better = (sqrt + d/sqrt)/2.0;
		double evenbetter = (better + d/better)/2.0;
		return evenbetter;
	}
}
