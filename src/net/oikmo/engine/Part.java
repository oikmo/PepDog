package net.oikmo.engine;

import java.text.DecimalFormat;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import net.oikmo.engine.models.CubeModel;
import net.oikmo.engine.models.RawModel;
import net.oikmo.engine.models.TexturedModel;
import net.oikmo.engine.textures.ModelTexture;
import net.oikmo.main.Main;
import net.oikmo.toolbox.rbxl.Item;
import net.oikmo.toolbox.rbxl.PropertyContainer;

public class Part {
	public TexturedModel model;
	private Vector3f position, rotation, scale;
	private int textureIndex = 0;
	
	public Part(Vector3f position, Vector3f rotation, Vector3f scale) {
		RawModel raw = Loader.getInstance().loadToVAO(CubeModel.vertices, CubeModel.indices, CubeModel.uv);
		this.model = new TexturedModel(raw, new ModelTexture(Main.textureSponge));
		
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}
	
	public static Part createPartFromItem(Item item) {
		Vector3f position = null;
		Vector3f rotation = null;
		Vector3f scale    = null;
		
		for(Object prop : item.getProperties().getStringOrProtectedStringOrInt()) {
			if(prop instanceof PropertyContainer.Bool) {
				PropertyContainer.Bool property = (PropertyContainer.Bool)prop;
				//System.out.println("bool " + property.getName() + " " + property.isValue());
			}
			else if(prop instanceof PropertyContainer.Float) {
				PropertyContainer.Float property = (PropertyContainer.Float)prop;
				//System.out.println("float " + property.getName() + " " + property.getValue());
			}
			else if(prop instanceof PropertyContainer.Token) {
				PropertyContainer.Token property = (PropertyContainer.Token)prop;
				//System.out.println("token " + property.getName() + " " + property.getValue());
			}
			else if(prop instanceof PropertyContainer.Int) {
				PropertyContainer.Int property = (PropertyContainer.Int)prop;
				//System.out.println("int " + property.getName() + " " + property.getValue());
			} 
			else if(prop instanceof PropertyContainer.String) {
				PropertyContainer.String property = (PropertyContainer.String)prop;
				//System.out.println("string " + property.getName() + " " + property.getValue());
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
			else {
				System.out.println(prop.getClass());
			}
		}
		System.out.println(item.getReferent() + " " + rotation.toString());
		return new Part(position, rotation, scale);
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
	
	public void setModel(TexturedModel model) {
		this.model = model;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	DecimalFormat format = new DecimalFormat("#.#");
	public String getPrintPosition() {
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
}
