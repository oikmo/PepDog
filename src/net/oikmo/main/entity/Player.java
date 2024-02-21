package net.oikmo.main.entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import net.oikmo.engine.DisplayManager;
import net.oikmo.engine.Entity;
import net.oikmo.engine.Loader;
import net.oikmo.engine.audio.AudioMaster;
import net.oikmo.engine.audio.Source;
import net.oikmo.engine.models.TexturedModel;
import net.oikmo.main.GameSettings;
import net.oikmo.main.Main;

public class Player extends Entity {

	private Camera camera;
	private int visibleTexture, invisibleTexture = Loader.getInstance().loadTexture("models/playerTrans");

	private final int FORWARD_KEY = Keyboard.KEY_W;
	private final int BACKWARD_KEY = Keyboard.KEY_S;
	private final int LEFT_KEY = Keyboard.KEY_A;
	private final int RIGHT_KEY = Keyboard.KEY_D;

	private final int JUMP_KEY = Keyboard.KEY_SPACE;

	private float terrainHeight = 0;
	private final float JUMP_POWER = 15f; //12.5

	/** Speed of player going backwards or forwards */
	private float currentVertSpeed = 0;
	/** Speed of player strafing */
	private float currentHorzSpeed = 0;
	/** Y Velocity */
	private float upwardsSpeed = 0;

	private float moveSpeed;
	private float desiredMoveSpeed;
	//private final float speedIncreaseMultiplier = 1.5f;

	private float walkSpeed = 15;

	private boolean grounded = true;

	enum MovementState {
		idle,
		walking,
	};
	private MovementState prevState;
	private MovementState state;
	
	Source footstepsSFX, jumpSFX;
	int jump, footsteps;

	public Player(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
		super(model, position, rotation, scale);
		init();
	}
	public Player(String model, Vector3f position, Vector3f rotation, float scale) {
		super(model, position, rotation, scale);
		init();
	}

	private void init() {
		this.visibleTexture = this.model.getTexture().getID();

		camera = new Camera(this);
		jump = AudioMaster.getSound("swoosh");
		footsteps = AudioMaster.getSound("bfsl-minifigfoots1");
		footstepsSFX = new Source(0f,0f,0.01f,AL11.AL_LINEAR_DISTANCE_CLAMPED);
		footstepsSFX.setLooping(true);
		footstepsSFX.play(footsteps);
		footstepsSFX.pause();
		jumpSFX = new Source();
	}
	
	private boolean pause;
	public void update() {
		camera.update();
		if(pause) { return; }
		checkInputs();
		applyForce(currentVertSpeed, currentHorzSpeed);

		if(upwardsSpeed > -125) {
			upwardsSpeed += GameSettings.GRAVITY * DisplayManager.getFrameTimeSeconds();
		}
		super.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		
		footstepsSFX.setPosition(super.getPosition().x, super.getPosition().y, super.getPosition().z);
		
		terrainHeight = 0;

		grounded = super.getPosition().y < terrainHeight;

		if(grounded) {
			upwardsSpeed = 0;
			super.getPosition().y = terrainHeight;
		}
		footstepsSFX.setVolume(GameSettings.globalVolume);

		if(Main.isMoving() && isGrounded(1f)) {
			if(!footstepsSFX.isPlaying())
				footstepsSFX.play(footsteps);
		} if(!Main.isMoving() || !isGrounded(1f)) {
			if(footstepsSFX.isPlaying())
				footstepsSFX.pause();
		}
		
		model.getTexture().setID(camera.isFirstPerson() ? invisibleTexture : visibleTexture);
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			walkSpeed = 40;
		} else {
			walkSpeed = 15;
		}
	}
	
	public boolean isPaused() {
		return pause;
	}
	public void setPaused(boolean pause) {
		this.pause = pause;
	}
	
	private void checkInputs() {
		if(Main.currentScreen != null) { if(Main.currentScreen.isLockInput()) { this.currentVertSpeed = 0; this.currentHorzSpeed = 0; return; } }

		stateHandler();

		Vector2f input = getInput();
		moveSpeed = desiredMoveSpeed;
		this.currentVertSpeed = input.x * moveSpeed;
		this.currentHorzSpeed = input.y * moveSpeed;

		if(Keyboard.isKeyDown(JUMP_KEY)) {
			jump();
		}
	}
	private Vector2f getInput() {
		int x = 0, y = 0;
		if(Keyboard.isKeyDown(FORWARD_KEY)) { x = -1; } else if(Keyboard.isKeyDown(BACKWARD_KEY)) { x = 1; }
		if(Keyboard.isKeyDown(LEFT_KEY))  { y = -1; } else if(Keyboard.isKeyDown(RIGHT_KEY)) { y = 1;  }
		return new Vector2f(x,y);
	}
	private void stateHandler() {
		if(prevState != state) {
			prevState = state;
		}

		if(Main.isMoving()) {
			state = MovementState.walking;
			desiredMoveSpeed = walkSpeed;
		} else {
			state = MovementState.idle;
		}
	}
	private void jump() {
		if(!isGrounded()) return;
		jumpSFX.setVolume(GameSettings.globalVolume);
		jumpSFX.play(AudioMaster.getSound("swoosh"));
		this.upwardsSpeed = JUMP_POWER;
	}
	
	public void pause() {
		footstepsSFX.stop();
	}

	private void applyForce(float vert, float horz) {
		float distanceVert = vert * DisplayManager.getFrameTimeSeconds();
		float distanceHorz = horz * DisplayManager.getFrameTimeSeconds();
		float dVertX = 0;
		float dVertZ = 0;
		float dHorzX = 0;
		float dHorzZ = 0;
		
		Vector2f input = this.getInput();
		
		float yaw = -camera.getYaw();
		if(input.x != 0 || camera.isFirstPerson()) {
			super.setRotationLerp(getRotX(), yaw, getRotZ());
		}
		
		dVertX = (float) (distanceVert * Math.sin(Math.toRadians(yaw)));
		dVertZ = (float) (distanceVert * Math.cos(Math.toRadians(yaw)));
		dHorzX = (float) (distanceHorz * Math.sin(Math.toRadians(yaw + 90)));
		dHorzZ = (float) (distanceHorz * Math.cos(Math.toRadians(yaw + 90)));

		super.increasePosition((dHorzX + dVertX), 0, (dHorzZ + dVertZ));
	}
	
	public boolean isGrounded() {
		return grounded;
	}
	public boolean isGrounded(float offset) {
		return !(super.getPosition().y > terrainHeight + offset) && grounded ? true : grounded;
	}

	public Camera getCamera() {
		return camera;
	}
}
