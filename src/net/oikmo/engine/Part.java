package net.oikmo.engine;

import java.text.DecimalFormat;

import javax.vecmath.Quat4f;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

import net.oikmo.engine.models.TexturedModel;
import net.oikmo.engine.renderers.part.BrickColor;
import net.oikmo.engine.renderers.part.PartRenderer;
import net.oikmo.engine.textures.ModelTexture;
import net.oikmo.main.PhysicsSystem;
import net.oikmo.toolbox.Maths;
import net.oikmo.toolbox.Toolbox;
import net.oikmo.toolbox.rbxl.Item;
import net.oikmo.toolbox.rbxl.PropertyContainer;

public class Part {

	public enum ShapeType {

		Cylinder(0),
		Block(1),
		Sphere(2);

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
	
	public enum FormType {

		Symmetric(0),
		Brick(1),
		Plate(2);

		private final int type;
		FormType(int type) {
			this.type = type;
		}

		public static FormType getEnumFromValue(int value) {
			for(int i = 0; i < values().length; i++) {
				if(values()[i].getValue() == value) {
					return values()[i];
				}
			}
			return Brick;
		}

		public int getValue() {
			return type;
		}
	}
	
	private TexturedModel model;
	private Vector3f position, rotation, scale;
	private int textureIndex = 0;
	private Vector3f colour;
	private int shape;
	private float transparency;
	private RigidBody body;
	private Transform transform;
	private int form;
	
	float mass = 1f;
	private boolean loaded = false;
	public Part(Vector3f position, Vector3f rotation, Vector3f scale, Vector3f colour, int shape, int form, float transparency) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.colour = new Vector3f(colour);
		this.shape = shape;
		this.form = form;
		
		this.transparency = transparency;
		
		CollisionShape colShape = new BoxShape(Maths.lwjglToVM(getScale()));
		javax.vecmath.Vector3f localInertia = new javax.vecmath.Vector3f(0, 0, 0);
		colShape.calculateLocalInertia(mass, localInertia);
		transform = new Transform();
		transform.origin.set(getPosition().x, getPosition().y, getPosition().z);
		
		DefaultMotionState motionState = new DefaultMotionState(transform);
		RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(mass, motionState, colShape, localInertia);
		this.body = new RigidBody(rbInfo);
		loaded = true;
		
		ModelTexture texture = new ModelTexture(PartRenderer.texture);
		if(transparency != 1f) { 
			texture.setHasTransparency(true);
		}

		switch(shape) {
		case 0:
			this.model = new TexturedModel(PartRenderer.cylinder, texture);
			break;
		case 1:
			this.model = new TexturedModel(PartRenderer.block, texture);
			break;
		case 2:
			this.model = new TexturedModel(PartRenderer.sphere, texture);
			break;
		}
		
		switch(form) {
		case 2:
			transform.setRotation(Toolbox.get_quaternion_from_euler(getRotation()));
			PhysicsSystem.getWorld().addRigidBody(body);
			break;
		}
	}

	public float getTransparency() {
		return this.transparency;
	}

	public static Part createPartFromItem(Item item) {
		Vector3f position = null;
		Vector3f rotation = null;
		Vector3f scale = null;
		float transparency = 0;
		int colour = -1;
		int shape = -1;
		int form = 1;
		//String name = "";

		for(Object prop : item.getProperties().getStringOrProtectedStringOrInt()) {
			/*if(prop instanceof PropertyContainer.Bool) {
				PropertyContainer.Bool property = (PropertyContainer.Bool)prop;
				//System.out.println("bool " + property.getName() + " " + property.isValue());
			}*/
			if(prop instanceof PropertyContainer.Float) {
				PropertyContainer.Float property = (PropertyContainer.Float)prop;
				if(property.getName().toLowerCase().contentEquals("transparency")) {
					transparency = 1-property.getValue();
					//System.out.println(transparency);
				}
			}
			else if(prop instanceof PropertyContainer.String) {
				//PropertyContainer.String property = (PropertyContainer.String)prop;
				//name = property.getValue();
			}
			else if(prop instanceof PropertyContainer.Token) {
				PropertyContainer.Token property = (PropertyContainer.Token)prop;
				//System.out.println("token " + property.getName() + " " + property.getValue());
				if(property.getName().contentEquals("shape")) {
					shape = property.getValue();
				}
				if(property.getName().toLowerCase().contentEquals("formfactor")) {
					form = property.getValue();
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
				//System.out.println("vector3 " + property.getName() + " " + property.toString());
				if(property.getName().contentEquals("size")) {
					scale = new Vector3f(property.getX(), property.getY(), property.getZ());
				}
			}
			else if(prop instanceof PropertyContainer.CoordinateFrame) {
				PropertyContainer.CoordinateFrame property = (PropertyContainer.CoordinateFrame)prop;

				if(property.getName().contentEquals("CFrame")) {
					position = new Vector3f(property.x(),property.y(),property.z());
					rotation = new Vector3f(property.getAngles());
				}
			}
		}
		//System.out.println(name + " " + BrickColor.getEnumFromValue(colour));
		//System.out.println(item.getReferent() + " " + rotation.toString());
		return new Part(position, rotation, scale, BrickColor.getEnumFromValue(colour).getValue(), ShapeType.getEnumFromValue(shape).getValue(), FormType.getEnumFromValue(form).getValue(), transparency);
	}
	
	private Quat4f quat = new Quat4f();
	public void update() {
		if(loaded) {
			body.getMotionState().getWorldTransform(transform);
			transform.getRotation(quat);
			position.x = transform.origin.x;
			position.y = transform.origin.y;
			position.z = transform.origin.z;
			if(form == 2) {
				rotation = Toolbox.euler_from_quaternion(quat);
			}
			
		}
		
	}

	public int getShape() {
		return shape;
	}

	public void setPosition(float x, float y, float z) {
		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}

	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	public void increaseRotation(float dx, float dy, float dz) {
		this.rotation.x += dx;
		this.rotation.y += dy;
		this.rotation.z += dz;
	}

	public void setRotation(float dx, float dy, float dz) {
		this.rotation.x = dx;
		this.rotation.y = dy;
		this.rotation.z = dz;
	}

	public void setRotation(Vector3f rotation) {
		if(rotation == null) { return; }
		this.rotation = rotation;
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

	DecimalFormat format = new DecimalFormat("#.#");
	public String toString() {
		return "X:"+format.format(getPosition().x)+", Y:"+format.format(getPosition().y)+", Z:"+format.format(getPosition().z);
	}
	public void setPosition(Vector3f position) {
		if(position == null) { return; }
		this.position = position;
	}

	public Vector3f getScale() {
		return scale;
	}
	public void setScale(Vector3f scale) {
		this.scale = scale;
	}

	public float getTextureXOffset(){
		int column = textureIndex%model.getTexture().getNumberOfRows();
		return (float)column/(float)model.getTexture().getNumberOfRows();
	}

	public float getTextureYOffset(){
		int row = textureIndex/model.getTexture().getNumberOfRows();
		return (float)row/(float)model.getTexture().getNumberOfRows();
	}

	public Vector2f getCoords() {
		return new Vector2f(getPosition().x, getPosition().z);
	}

	public Vector3f getColour() {
		return colour;
	}
}
