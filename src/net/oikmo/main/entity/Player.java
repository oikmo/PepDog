package net.oikmo.main.entity;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import net.oikmo.engine.DisplayManager;
import net.oikmo.engine.Entity;
import net.oikmo.engine.Loader;
import net.oikmo.engine.Part;
import net.oikmo.engine.audio.AudioMaster;
import net.oikmo.engine.audio.Source;
import net.oikmo.engine.collision.AABB;
import net.oikmo.engine.collision.Collision;
import net.oikmo.engine.models.TexturedModel;
import net.oikmo.engine.scene.Scene;
import net.oikmo.engine.terrain.Terrain;
import net.oikmo.main.GameSettings;
import net.oikmo.main.Main;
import net.oikmo.main.scene.SceneManager;

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
		dashing,
		sprinting
	};
	private MovementState prevState;
	private MovementState state;
	
	Source footstepsSFX, fallingSFX, jumpSFX;
	int jumping, falling, footsteps;

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

		this.name = "Player";

		camera = new Camera(this);
		jumping = AudioMaster.loadSound("jump");
		footsteps = AudioMaster.loadSound("bfsl-minifigfoots1");
		falling = AudioMaster.loadSound("fallingSFX");
		footstepsSFX = new Source(0f,0f,0.01f,AL11.AL_LINEAR_DISTANCE_CLAMPED);
		footstepsSFX.setLooping(true);
		footstepsSFX.play(footsteps);
		footstepsSFX.pause();
		fallingSFX = new Source();
		fallingSFX.setLooping(true);
		fallingSFX.play(falling);
		fallingSFX.pause();
		jumpSFX = new Source();
		jumpSFX.setVolume(0.05f);

		this.getAABB().setHalfExtent(0.69f, this.getAABB().getHalfExtent().y, 0.69f);
	}

	float distanceX = 0;
	float distanceY = 0;
	public void update(Terrain terrain) {
		camera.update();

		checkInputs();

		if(Keyboard.isKeyDown(Keyboard.KEY_F)) {
			this.setPosition(0, 0, 0);
		}

		applyForce(currentVertSpeed, currentHorzSpeed);

		if(upwardsSpeed > -125) {
			upwardsSpeed += GameSettings.GRAVITY * DisplayManager.getFrameTimeSeconds();
		}
		super.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		
		footstepsSFX.setPosition(super.getPosition().x, super.getPosition().y, super.getPosition().z);


		if(terrain != null) {
			terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		} else {
			terrainHeight = 0;
		}

		grounded = super.getPosition().y < terrainHeight;

		if(grounded) {
			upwardsSpeed = 0;
			super.getPosition().y = terrainHeight;
		}

		fallingSFX.setVolume(GameSettings.globalVolume);
		footstepsSFX.setVolume(GameSettings.globalVolume);
		footstepsSFX.setPitch(isSprinting() ? 1.175f : 1);

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
		
		Vector3f temp1Pos = new Vector3f(getPosition());
		temp1Pos.y = getPosition().y + this.getAABB().getHalfExtent().y;
		this.getAABB().getCenter().set(temp1Pos);

		Scene scene = SceneManager.getCurrentScene();

		List<AABB> collisionBoxes = new ArrayList<>();

		if (scene.isLoaded()) {
			for (Part e : scene.getParts()) {
				if (e.getAABB() != null) {
					collisionBoxes.add(e.getAABB());
				}
			}
		}
		
		if(!Keyboard.isKeyDown(Keyboard.KEY_V)) {
			for (AABB box : collisionBoxes) {
				Collision data = this.getAABB().intersects(box);
				if (data.intersecting) {
					this.getAABB().correctPosition(box, data);
					Vector3f temp2Pos = new Vector3f(this.getAABB().getCenter());
					temp2Pos.y = this.getAABB().getCenter().y - this.getAABB().getHalfExtent().y;

					if(this.getAABB().getCenter().y - this.getAABB().getHalfExtent().y >= box.getCenter().y + box.getHalfExtent().y) {
						grounded = true;
						upwardsSpeed = 0;
					}

					this.getPosition().set(temp2Pos);
				}
			}
		}
		
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
			desiredMoveSpeed = walkSpeed;
		}
	}
	private void jump() {
		if(!isGrounded()) return;
		jumpSFX.setPitch(1.1f);
		jumpSFX.setVolume(GameSettings.globalVolume-0.5f);
		jumpSFX.play(jumping);
		this.upwardsSpeed = JUMP_POWER;
	}
	public String getSpeed() {
		if(currentVertSpeed == 0) {
			return currentVertSpeed + " " + -currentHorzSpeed;
		} else if(currentHorzSpeed == 0) {
			return -currentVertSpeed + " " + currentHorzSpeed;
		} else if(currentVertSpeed == 0 && currentHorzSpeed == 0) {
			return currentVertSpeed + " " + currentHorzSpeed;
		}
		return -currentVertSpeed + " " + -currentHorzSpeed;
	}
	public boolean isSprinting() {
		return state == MovementState.sprinting;
	}

	public void pause() {
		footstepsSFX.stop();
	}

	private void applyForce(float vert, float horz) {
		float distanceVert = vert * DisplayManager.getFrameTimeSeconds();
		float distanceHorz = horz * DisplayManager.getFrameTimeSeconds();
		super.setRotation(getRotX(), -camera.getYaw(), getRotZ());

		float dVertX = (float) (distanceVert * Math.sin(Math.toRadians(super.getRotY())));
		float dVertZ = (float) (distanceVert * Math.cos(Math.toRadians(super.getRotY())));

		float dHorzX = (float) (distanceHorz * Math.sin(Math.toRadians(super.getRotY() + 90)));
		float dHorzZ = (float) (distanceHorz * Math.cos(Math.toRadians(super.getRotY() + 90)));
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
