package net.oikmo.main.entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import net.oikmo.engine.audio.AudioMaster;
import net.oikmo.engine.audio.Source;
import net.oikmo.main.GameSettings;
import net.oikmo.main.Main;
import net.oikmo.main.Main.GameState;
import net.oikmo.toolbox.Maths;

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
	 * {@code public Camera(Vector3f position, Vector3f rotation, float scale)}<br><br>
	 * 
	 * Camera constructor. Basically creates the camera with the right variables.
	 * 
	 * @param position
	 * @param rotation
	 * @param scale
	 */
	public Camera(Vector3f position, Vector3f rotation) {
		this.position = position;
		this.pitch = rotation.x;
		this.roll = rotation.z;
	}
	
	private int source;
	private Source click;
	
	public Camera(Player player) {
		this.position = new Vector3f(0,0,0);
		this.player = player;
		source = AudioMaster.loadSound("SWITCH3");
		click = new Source();
	}
	
	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}
	public void increaseRotation(float dx, float dy, float dz) {
		this.pitch += dx;
		this.yaw += dy;
		this.roll += dz;
	}
	
	public void setRotation(float dx, float dy, float dz) {
		this.pitch = dx;
		this.yaw = dy;
		this.roll = dz;
	}

	private float speeds = 0f;
	private float speed = 0.1f;
	private float moveAt;
	private boolean flyCam = false;
	private boolean lockInCam;
	/**
	 * {@code public void move()}<br><br>
	 * Moves the camera in which direction the player is facing [will be removed soon or reused for debug]
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
					calculatePitch();
					calculateAngleAroundPlayer();
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
	
	public float getDistanceFromPlayer() {
		return distanceFromPlayer;
	}
	public boolean isFirstPerson() {
		return distanceFromPlayer < 1;
	}
	
	public void invertPitch(){
		if(!flyCam) {
			this.pitch = -pitch;
		}
		
	}

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
	
	private void calculateCameraPosition(float horizDistance, float verticDistance){
		float yOffset = this.yOffset;
		
		// substitute in commented-out RotY lines to make the camera rotate when the player does
		//float theta = player.getRotY() + angleAroundPlayer;
		float theta = angleAroundPlayer;
		float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
		position.x = Maths.lerp(position.x, player.getPosition().x - offsetX, 0.35f);
		position.z = Maths.lerp(position.z, player.getPosition().z - offsetZ, 0.35f);
		position.y = player.getPosition().y + verticDistance + yOffset;
		float heightOfTerrain = Main.getHeightFromPosition(position.x, position.z);
		if(Main.getTerrainFromPosition(position.x, position.z) != null) {
			if(position.y <= heightOfTerrain+1) {
				position.y = heightOfTerrain+1;
			}
		}
		//this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
		this.yaw = 180 - angleAroundPlayer;
	}
	
	private float calculateHorizontalDistance(){
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch+2)));
	}
	private float calculateVerticalDistance(){
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch+2)));
	}
	
	private long lastClick = 150;
	private long coolDownTime = 150;
	private boolean zooming = false;
	private boolean lockIn = false;
	private void calculateZoom(){
		if(Main.currentScreen != null) { if(Main.currentScreen.isLockInput()) { return; } }
		
		if(Keyboard.isKeyDown(Keyboard.KEY_F)) {
			long timeNow = System.currentTimeMillis();
		    long time = timeNow - lastClick;
		    if (time < 0 || time > coolDownTime) {
		    	lastClick = timeNow;
		    	zooming = !zooming;
		    	if(zooming) {
					distanceFromPlayer = 0;
				} else {
					distanceFromPlayer = 8;
				}
		    }
		} 
		
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
		
		distanceFromPlayer = Maths.lerp(distanceFromPlayer, distanceFromPlayer - zoomLevel, 1f);
		if(distanceFromPlayer < distanceFromPlayerMin){
			distanceFromPlayer = distanceFromPlayerMin;
		} else if(distanceFromPlayer > distanceFromPlayerMax) {
			distanceFromPlayer = distanceFromPlayerMax;
		}
	}
	
	int maxVerticalTurn = 80;
	private void calculatePitch() {
		float pitchChange = Mouse.getDY() * GameSettings.sensitivity*2;
		pitch = Maths.lerp(pitch, pitch - pitchChange, 1f);
		if(pitch < -maxVerticalTurn){
			pitch = -maxVerticalTurn;
		}else if(pitch > maxVerticalTurn){
			pitch = maxVerticalTurn;
		}
	}
	
	private void calculateAngleAroundPlayer() {
		float angleChange = Mouse.getDX() * GameSettings.sensitivity;
		angleAroundPlayer -= angleChange;
	}
}