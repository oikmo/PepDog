package net.pepdog.main.entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import net.pepdog.engine.Part;
import net.pepdog.engine.Part.ShapeType;
import net.pepdog.engine.audio.AudioMaster;
import net.pepdog.engine.audio.Source;
import net.pepdog.engine.renderers.MasterRenderer;
import net.pepdog.main.GameSettings;
import net.pepdog.main.Main;
import net.pepdog.main.Main.GameState;
import net.pepdog.main.scene.SceneManager;
import net.pepdog.toolbox.Toolbox;

/**
 * Camera class. Allows the player to see the world.
 * 
 * @author <i>Oikmo</i>
 */
public class Camera {
	
	Vector3f position;
	private float distanceFromPlayerMax = 128;
	private float distanceFromPlayerMin = 0;
	public float distanceFromPlayer = 8;
	public float angleAroundPlayer = 0;

	public float pitch = 0;
	public float yaw = 0;
	public float roll = 0;
	private final float yOffset = 4f;
	
	Player player;
	
	/**
	 * Camera constructor. Sets position and rotation.
	 * 
	 * @param position
	 * @param rotation
	 * @param scale
	 */
	public Camera(Vector3f position, Vector3f rotation) {
		this.position = position;
		this.pitch = rotation.x;
		this.roll = rotation.z;
		flyCam = true;
	}
	
	private int source;
	private Source click;
	
	/**
	 * Camera constructor. Sets player and enables click noise when {@link #calculateZoom()} is used
	 * @param player
	 */
	public Camera(Player player) {
		this.position = new Vector3f(0,0,0);
		this.player = player;
		source = AudioMaster.getSound("SWITCH3");
		click = new Source();
	}
	/**
	 * Moves camera based on given values.
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}
	/**
	 * Rotates the camera based on given values.
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public void increaseRotation(float dx, float dy, float dz) {
		this.pitch += dx;
		this.yaw += dy;
		this.roll += dz;
	}
	/**
	 * Sets the rotation of the camera based on given values.
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public void setRotation(float dx, float dy, float dz) {
		this.pitch = dx;
		this.yaw = dy;
		this.roll = dz;
	}
	
	/**
	 * Sets position to given 3D Vector
	 * @param vector
	 */
	public void setPosition(Vector3f v) {
		this.position = v;
	}

	private float speeds = 0f;
	private float speed = 0.1f;
	private float moveAt;
	private boolean flyCam = false;
	private boolean lockInCam;
	/**
	 * Moves the camera in which direction the player is facing (when moving).<br>
	 * Fly cam function within else, just do lock on player.
	 */
	public void update(){
		if(!lockInCam) {
			if(Keyboard.isKeyDown(Keyboard.KEY_Q)) {
				flyCam = !flyCam;
				lockInCam = true;
			}
		} else {
			if(!Keyboard.isKeyDown(Keyboard.KEY_Q)) {
				lockInCam = false;
			}
		}
		
		if(flyCam) {
			player.setPaused(true);
			if(!Mouse.isGrabbed()) {
				if(Main.gameState == GameState.game) {
					Mouse.setGrabbed(true);
				} 
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				speeds = 6;
			} else {
				speeds = 2;
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
				position.y += speed * speeds;
			} else if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
				position.y -= speed * speeds;
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
				moveAt = -speed * speeds;
			} else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
				moveAt = speed * speeds;
			} else {
				moveAt = 0;
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
				position.x += (float) -((speed * speeds) * Math.cos(Math.toRadians(yaw)));
				position.z -= (float) ((speed * speeds) * Math.sin(Math.toRadians(yaw)));
			} else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
				position.x -= (float) -((speed * speeds) * Math.cos(Math.toRadians(yaw)));
				position.z += (float) ((speed * speeds) * Math.sin(Math.toRadians(yaw)));
			}
			
			pitch -= Mouse.getDY() * GameSettings.sensitivity*2;
			if(pitch < -maxVerticalTurn){
				pitch = -maxVerticalTurn;
			}else if(pitch > maxVerticalTurn){
				pitch = maxVerticalTurn;
			}
			yaw += Mouse.getDX() * GameSettings.sensitivity;
			
			position.x += (float) -(moveAt * Math.sin(Math.toRadians(yaw)));
			position.y += (float) (moveAt * Math.sin(Math.toRadians(pitch)));
			position.z += (float) (moveAt * Math.cos(Math.toRadians(yaw)));
		} else {
			player.setPaused(false);
			if(Main.gameState == GameState.game) {
				calculateZoom();
				if(Mouse.isButtonDown(1) || this.isFirstPerson()) {
					calculateAngleAroundPlayer();
					calculatePitch();
					
				}
				
				if(this.isFirstPerson()) {
					Mouse.setGrabbed(true);
				} else {
					Mouse.setGrabbed(Mouse.isButtonDown(1));
				}
			} else {
				if(Mouse.isGrabbed())
					Mouse.setGrabbed(false);
			}
			
			float horizontalDistance = calculateHorizontalDistance();
			float verticalDistance = calculateVerticalDistance();
			
			calculateCameraPosition(horizontalDistance, verticalDistance);
			
		}
	}
	
	boolean lockInBalls = false;
	/**
	 * Fly cam (will most likely be temporary)
	 */
	public void flyCam() {
		if(!lockInBalls) {
			if(Keyboard.isKeyDown(Keyboard.KEY_E)) {
				Part part = new Part(new Vector3f(getPosition()), 2, ShapeType.Sphere);
				SceneManager.getCurrentScene().addPart(part);
				lockInBalls = true;
			}
		} else {
			if(!Keyboard.isKeyDown(Keyboard.KEY_E)) {
				lockInBalls = false;
			}
		}
		
		
		if(!lockInCam) {
			if(Keyboard.isKeyDown(Keyboard.KEY_Q)) {
				flyCam = !flyCam;
				lockInCam = true;
			}
		} else {
			if(!Keyboard.isKeyDown(Keyboard.KEY_Q)) {
				lockInCam = false;
			}
		}
		
		if(flyCam) {
			if(Mouse.isGrabbed() != Mouse.isButtonDown(1)) {
				Mouse.setGrabbed(Mouse.isButtonDown(1));
			}
			
			
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				speeds = 6;
			} else {
				speeds = 2;
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
				position.y += speed * speeds;
			} else if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
				position.y -= speed * speeds;
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
				moveAt = -speed * speeds;
			} else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
				moveAt = speed * speeds;
			} else {
				moveAt = 0;
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
				position.x += (float) -((speed * speeds) * Math.cos(Math.toRadians(yaw)));
				position.z -= (float) ((speed * speeds) * Math.sin(Math.toRadians(yaw)));
			} else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
				position.x -= (float) -((speed * speeds) * Math.cos(Math.toRadians(yaw)));
				position.z += (float) ((speed * speeds) * Math.sin(Math.toRadians(yaw)));
			}
			
			pitch -= Mouse.getDY() * GameSettings.sensitivity*2;
			if(pitch < -maxVerticalTurn){
				pitch = -maxVerticalTurn;
			}else if(pitch > maxVerticalTurn){
				pitch = maxVerticalTurn;
			}
			yaw += Mouse.getDX() * GameSettings.sensitivity;
			
			position.x += (float) -(moveAt * Math.sin(Math.toRadians(yaw)));
			position.y += (float) (moveAt * Math.sin(Math.toRadians(pitch)));
			position.z += (float) (moveAt * Math.cos(Math.toRadians(yaw)));
		} else {
			if(Mouse.isGrabbed()) {
				Mouse.setGrabbed(false);
			}
		}
	}
	
	/**
	 * How far the camera is from the player.
	 * @return distanceFromPlayer (float)
	 */
	public float getDistanceFromPlayer() {
		return distanceFromPlayer;
	}
	/**
	 * Uses the {@link #getDistanceFromPlayer()} function to check if the camera is close enough to be first person perspective.
	 * @return firstPerson (boolean)
	 */
	public boolean isFirstPerson() {
		return distanceFromPlayer < 1;
	}
	/**
	 * Turns the camera upside down. Used in {@link MasterRenderer#renderSceneWater()}
	 */
	public void invertPitch(){
		if(!flyCam) {
			this.pitch = -pitch;
		}
		
	}
	
	/**
	 * Returns position of the camera.
	 * @return
	 */
	public Vector3f getPosition() {
		return position;
	}
	
	public float getPitch() {
		return pitch;
	}
	public float getYaw() {
		return yaw;
	}
	public float getRoll() {
		return roll;
	}
	
	/**
	 * Calculates the position of the camera using the player's position and by using {@link #calculateHorizontalDistance()} and {@link #calculateVerticalDistance()}
	 * <br>Uses pythagoreas maths to calulcate position ;-;
	 * @param horizDistance (float)
	 * @param verticDistance (float)
	 */
	private void calculateCameraPosition(float horizDistance, float verticDistance){
		
		// substitute in commented-out RotY lines to make the camera rotate when the player does
		//float theta = player.getRotY() + angleAroundPlayer;
		float theta = angleAroundPlayer;
		float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
		position.y = player.getPosition().y + verticDistance ;
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		
		//this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
		this.yaw = 180 - angleAroundPlayer;
	}
	
	/**
	 * returns the vertical distance from the player using {@link #getDistanceFromPlayer()} and {@link #getPitch()}
	 * @return horizontalDist (float)
	 */
	private float calculateHorizontalDistance(){
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch+2)));
	}
	/**
	 * returns the horrizontal distance from the player using {@link #getDistanceFromPlayer()} and {@link #getPitch()}
	 * @return verticalDist (float)
	 */
	private float calculateVerticalDistance(){
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch+2)))+ this.yOffset;
	}
	
	/**
	 * looksmaxxing
	 */
	private boolean lockIn = false;
	/**
	 * Calculates the scroll wheel or I/O keys to factor in how far the camera is using {@link #distanceFromPlayer}
	 */
	private void calculateZoom(){
		if(Main.currentScreen != null) { if(Main.currentScreen.isLockInput()) { return; } }
		float zoomLevel = Mouse.getDWheel() * 0.05f;
		
		if(!lockIn) {
			if(Keyboard.isKeyDown(Keyboard.KEY_I)){
				zoomLevel = 2;
				lockIn = true;
			} else if(Keyboard.isKeyDown(Keyboard.KEY_O)) {
				zoomLevel = -2;
				lockIn = true;
			}
		} else { 
			if(!Keyboard.isKeyDown(Keyboard.KEY_I) && !Keyboard.isKeyDown(Keyboard.KEY_O)) {
				lockIn = false;
				click.setVolume(GameSettings.globalVolume-(GameSettings.globalVolume/2));
				click.play(source);
			}
		}
		
		distanceFromPlayer = Toolbox.lerp(distanceFromPlayer, distanceFromPlayer - zoomLevel, 0.1f);
		if(distanceFromPlayer < distanceFromPlayerMin){
			distanceFromPlayer = distanceFromPlayerMin;
		} else if(distanceFromPlayer > distanceFromPlayerMax) {
			distanceFromPlayer = distanceFromPlayerMax;
		}
	}
	
	int maxVerticalTurn = 80; // max angle
	/**
	 * Limits the player's angles when looking up and down.
	 */
	private void calculatePitch() {
		float pitchChange = Mouse.getDY() * GameSettings.sensitivity*2;
		pitch = Toolbox.lerp(pitch, pitch - pitchChange, 1f);
		if(pitch < -maxVerticalTurn){
			pitch = -maxVerticalTurn;
		}else if(pitch > maxVerticalTurn){
			pitch = maxVerticalTurn;
		}
	}
	/**
	 * Basically converts the X difference in the mouse to X axis.
	 */
	private void calculateAngleAroundPlayer() {
		float angleChange = Mouse.getDX() * GameSettings.sensitivity;
		angleAroundPlayer -= angleChange;
	}
}