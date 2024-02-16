package net.oikmo.main.scene;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.lwjgl.util.vector.Vector3f;

import net.oikmo.engine.Part;
import net.oikmo.engine.renderers.MasterRenderer;
import net.oikmo.engine.scene.Scene;
import net.oikmo.engine.textures.TerrainTexture;
import net.oikmo.engine.textures.TerrainTexturePack;
import net.oikmo.main.GameSettings;
import net.oikmo.main.entity.Camera;
import net.oikmo.main.entity.Light;
import net.oikmo.toolbox.rbxl.Item;
import net.oikmo.toolbox.rbxl.Parser;
import net.oikmo.toolbox.rbxl.Roblox;

public class RobloxScene extends Scene {
	
	public void loadRoblox() {
		Roblox roblox = Parser.loadRBXL("2006Crossroads");

		List<Object> services = roblox.getItemOrExternalOrDeleteItem();
		for(Object obj : services) {
			if(obj instanceof Item) {
				Item service = (Item) obj;

				if(service.getClazz().contentEquals("Workspace")) {	
					for (Item item : service.getItem()) {
					    processItem(item);
					}
				}
			}
		}
	}
	
	public void processItem(Item item) {
		if(text != null) {
			text.setText("PARTS ADDED : "+this.getParts().size());
		}
	    if (item.getClazz().contentEquals("Part")) {
	        this.addPart(Part.createPartFromItem(item));
	    } else if (item.getClazz().contentEquals("Model")) {
	        for (Item nestedItem : item.getItem()) {
	            processItem(nestedItem); // Recursive call
	        }
	    }
	}

	public RobloxScene(String seed, TerrainTexturePack texturePack, TerrainTexture blendMap) {
		super(seed, texturePack, blendMap);
	}
	JFrame frame = null;
	JLabel text = null;
	@Override
	public void init() {

		Light sun = new Light(new Vector3f(200,200,100), new Vector3f(1.3f, 1.3f, 1.3f));
		this.addLight(sun);
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				frame = new JFrame();
				frame.setSize(150, 150);
				frame.setVisible(true);
				text = new JLabel("PARTS ADDED : 0");
				
				frame.add(text);
			}
			
		}).start();
		
		
		loadRoblox();
		
		frame.dispose();
	}

	@Override
	public void update(Camera camera) {	
		MasterRenderer.getInstance().renderScene(GameSettings.postProcess, camera);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}
}
