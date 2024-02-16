package net.oikmo.main.entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import net.oikmo.engine.Entity;
import net.oikmo.main.GameSettings;
import net.oikmo.main.Main;
import net.oikmo.main.Main.GameState;
import net.oikmo.main.scene.SceneManager;
import net.oikmo.toolbox.Maths;

/**
 * Camera class. Allows the player to see the world.
 * 
 * @author <i>Oikmo</i>
 */
public class Camera {
	
	
	float speeds = 0f;
	float speed = 0.1f;
	float turnSpeed = 0.1f;
	float moveAt;
	
	Vector3f position;
	private float distanceFromPlayerMax = 128;
	private float distanceFromPlayerMin = 0;
	public float distanceFromPlayer = 8;
	public float angleAroundPlayer = 0;

	public float pitch = 0;
	public float yaw = 0;
	public float roll = 0;
	private final float yOffset = 4.5f;
	
	Player player;
	
	Entity entity;
	
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
		this.yaw = rotation.y;
		this.roll = rotation.z;
	}
	
	public Camera(Player player) {
		this.position = new Vector3f(0,0,0);
		this.player = player;
		
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


	public boolean flyCam = false;
	
	/**
	 * {@code public void move()}<br><br>
	 * Moves the camera in which direction the player is facing [will be removed soon or reused for debug]
	 */
	public void update(){
		
		if(Main.gameState == GameState.game) {
			
			if(entity == null) {
				entity = new Entity("fern", new Vector3f(player.getPosition()), new Vector3f(0,0,0));
				entity.setAABB(null);
				entity.getModel().getTexture().setNumberOfRows(2);
				entity.setTextureIndex(1);
				SceneManager.getCurrentScene().addEntity(entity);
			} else {
				
				entity.setRotation(0, player.getRotY(), 0);
			}
			
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
	
	public float getDistanceFromPlayer() {
		return distanceFromPlayer;
	}
	
	public boolean isFirstPerson() {
		return distanceFromPlayer < 1;
	}
	
	public void invertPitch(){
		this.pitch = -pitch;
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
		distanceFromPlayer = Maths.lerp(distanceFromPlayer, distanceFromPlayer - zoomLevel, 1f);
		if(distanceFromPlayer < distanceFromPlayerMin){
			distanceFromPlayer = distanceFromPlayerMin;
		} else if(distanceFromPlayer > distanceFromPlayerMax) {
			distanceFromPlayer = distanceFromPlayerMax;
		}
		
	}
	
	int maxVerticalTurn = 80;
	private void calculatePitch() {
		float pitchChange = Mouse.getDY() * GameSettings.SENSITIVITY*2;
		pitch = Maths.lerp(pitch, pitch - pitchChange, 1f);
		if(pitch < -maxVerticalTurn){
			pitch = -maxVerticalTurn;
		}else if(pitch > maxVerticalTurn){
			pitch = maxVerticalTurn;
		}
	}
	
	private void calculateAngleAroundPlayer() {
		float angleChange = Mouse.getDX() * GameSettings.SENSITIVITY;
		angleAroundPlayer -= angleChange;
	}

	public void setRoll(float lerp) {
		this.roll = lerp;
	}
}