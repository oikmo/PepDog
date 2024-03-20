package net.pepdog.engine;

import javax.vecmath.Quat4f;

import org.lwjgl.util.vector.Vector3f;

import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.CylinderShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

import net.pepdog.engine.models.TexturedModel;
import net.pepdog.engine.renderers.part.BrickColor;
import net.pepdog.engine.renderers.part.PartRenderer;
import net.pepdog.engine.textures.ModelTexture;
import net.pepdog.main.PhysicsSystem;
import net.pepdog.main.scene.SceneManager;
import net.pepdog.toolbox.Maths;
import net.pepdog.toolbox.rbxl.Item;
import net.pepdog.toolbox.rbxl.PropertyContainer;

/**
 * Building part for part build game ripoff.
 * @author Oikmo
 *
 */
public class Part {

	/**
	 * Returns shape type using integer.
	 * @author Oikmo
	 *
	 */
	public enum ShapeType {

		Cylinder(2),
		Block(1),
		Sphere(0);

		private final int type;
		ShapeType(int type) {
			this.type = type;
		}

		public static ShapeType getEnumFromValue(int value) {
			for(int i = 0; i < values().length; i++) {
				if(values()[i].getValue() == value) {
					return values()[i];
				}
			}
			return Block;
		}

		public int getValue() {
			return type;
		}
	}
	
	private TexturedModel model;
	private Vector3f position, rotation, scale;
	private Vector3f colour;
	private ShapeType shape;
	private float transparency;
	private RigidBody body;
	private Transform transform;
	
	float mass = 1f;
	private boolean loaded = false;
	/**
	 * General loading.<br>
	 * Used in {@link #createPartFromItem(Item)}
	 * @param position
	 * @param rotation
	 * @param scale
	 * @param colour
	 * @param shape
	 * @param transparency
	 */
	public Part(Vector3f position, Quat4f rotation, Vector3f scale, Vector3f colour, ShapeType shape, float transparency) {
		this.position = position;
		this.scale = scale;
		this.colour = new Vector3f(colour);
		this.shape = shape;
		this.rotation = new Vector3f();
		Maths.QuaternionToEulerAngles(rotation, this.rotation);
		this.transparency = transparency;
		
		mass = scale.lengthSquared();
		CollisionShape colShape = null;
		
		
		ModelTexture texture = new ModelTexture(PartRenderer.texture);
		if(transparency != 1f) { 
			texture.setHasTransparency(true);
		}

		switch(shape) {
		case Cylinder:
			colShape = new CylinderShape(new javax.vecmath.Vector3f(scale.x/2,scale.y/2,scale.z/2));
			this.model = new TexturedModel(PartRenderer.cylinder, texture);
			break;
		case Block:
			colShape = new BoxShape(new javax.vecmath.Vector3f(scale.x/2,scale.y/2,scale.z/2));
			this.model = new TexturedModel(PartRenderer.block, texture);
			break;
		case Sphere:
			float total = ((scale.x + scale.y + scale.z) / 3) / 2;
			colShape = new SphereShape(total);
			this.model = new TexturedModel(PartRenderer.sphere, texture);
			break;
		}
		
		javax.vecmath.Vector3f localInertia = new javax.vecmath.Vector3f(0, 0, 0);
		colShape.calculateLocalInertia(mass, localInertia);
		transform = new Transform();
		transform.origin.set(getPosition().x, getPosition().y, getPosition().z);
		transform.setRotation(rotation);
		
		DefaultMotionState motionState = new DefaultMotionState(transform);
	
		RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(mass, motionState, colShape, localInertia);
		this.body = new RigidBody(rbInfo);
		//body.setCcdMotionThreshold(0f);
		body.setCollisionFlags(CollisionFlags.STATIC_OBJECT);
		PhysicsSystem.getWorld().addRigidBody(body);
		loaded = true;
	}
	
	/**
	 * To spawn bawls.
	 * @param position ({@link Vector3f})
	 * @param scale ({@link Float})
	 * @param shape ({@link ShapeType})
	 */
	public Part(Vector3f position, float scale, ShapeType shape) {
		this.position = position;
		this.shape = shape;
		this.colour = new Vector3f(BrickColor.Brown.getValue());
		this.scale = new Vector3f(scale*2, scale*2, scale*2);
		this.transparency = 1;
		this.rotation = new Vector3f(0,0,0);
		
		mass = scale;
		CollisionShape colShape = new SphereShape(scale);
		javax.vecmath.Vector3f localInertia = new javax.vecmath.Vector3f(0, 0, 0);
		colShape.calculateLocalInertia(mass, localInertia);
		transform = new Transform();
		transform.origin.set(getPosition().x, getPosition().y, getPosition().z);
		
		DefaultMotionState motionState = new DefaultMotionState(transform);
		RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(mass, motionState, colShape, localInertia);
		this.body = new RigidBody(rbInfo);
		body.setCcdMotionThreshold(0f);
		PhysicsSystem.getWorld().addRigidBody(body);
		loaded = true;
		
		ModelTexture texture = new ModelTexture(PartRenderer.texture);

		switch(shape) {
		case Cylinder:
			this.model = new TexturedModel(PartRenderer.cylinder, texture);
			break;
		case Block:
			this.model = new TexturedModel(PartRenderer.block, texture);
			break;
		case Sphere:
			this.model = new TexturedModel(PartRenderer.sphere, texture);
			break;
		}
	}
	
	/**
	 * Creates part from {@link net.pepdog.toolbox.rbxl.Item } using its properties.
	 * @param item
	 * @return {@link Part}
	 */
	public static Part createPartFromItem(Item item) {
		Vector3f position = null;
		Quat4f rotation = null;
		Vector3f scale = null;
		float transparency = 1;
		int colour = -1;
		int shape = -1;

		for(Object prop : item.getProperties().getStringOrProtectedStringOrInt()) {
			if(prop instanceof PropertyContainer.Float) {
				PropertyContainer.Float property = (PropertyContainer.Float)prop;
				if(property.getName().toLowerCase().contentEquals("transparency")) {
					transparency = 1-property.getValue();
				}
			}
			else if(prop instanceof PropertyContainer.Token) {
				PropertyContainer.Token property = (PropertyContainer.Token)prop;
				if(property.getName().contentEquals("shape")) {
					shape = property.getValue();
				}
			}
			else if(prop instanceof PropertyContainer.Int) {
				PropertyContainer.Int property = (PropertyContainer.Int)prop;
				if(property.getName().contentEquals("BrickColor")) {
					colour = property.getValue();
				}
			} 
			else if(prop instanceof PropertyContainer.Vector3) {
				PropertyContainer.Vector3 property = (PropertyContainer.Vector3)prop;
				if(property.getName().contentEquals("size")) {
					scale = new Vector3f(property.getX(), property.getY(), property.getZ());
				}
			}
			else if(prop instanceof PropertyContainer.CoordinateFrame) {
				PropertyContainer.CoordinateFrame property = (PropertyContainer.CoordinateFrame)prop;

				if(property.getName().contentEquals("CFrame")) {
					position = new Vector3f(property.x(),property.y(),property.z());
					rotation = new Quat4f(property.getQuaternion());
				}
			}
		}
		return new Part(position, rotation, scale, BrickColor.getEnumFromValue(colour).getValue(), ShapeType.getEnumFromValue(shape), transparency);
	}
	
	private Quat4f quat = new Quat4f();
	/**
	 * Update visuals to physics.
	 */
	public void update() {
		if(loaded) {
			body.getMotionState().getWorldTransform(transform);
			transform.getRotation(quat);
			Maths.VMtoLWJGL(transform.origin, position);
			Maths.QuaternionToEulerUsingMatrix(quat, rotation);
		}
		
		if(position.y < -75f) {
			loaded = false;
			PhysicsSystem.getWorld().removeRigidBody(body);
			body = null;
			SceneManager.getCurrentScene().getParts().remove(this);
		}
	}
	
	/**
	 * Returns transparency of part
	 * @return
	 */
	public float getTransparency() {
		return this.transparency;
	}
	
	public ShapeType getShape() {
		return shape;
	}
	
	public Vector3f getRotation() {
		return this.rotation;
	}

	public TexturedModel getModel() {
		return model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getScale() {
		return scale;
	}
	
	public Vector3f getColour() {
		return colour;
	}
}
