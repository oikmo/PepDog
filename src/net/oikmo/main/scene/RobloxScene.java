package net.oikmo.main.scene;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.lwjgl.util.vector.Vector3f;

import net.oikmo.engine.Part;
import net.oikmo.engine.renderers.MasterRenderer;
import net.oikmo.engine.scene.Scene;
import net.oikmo.engine.textures.TerrainTexturePack;
import net.oikmo.main.GameSettings;
import net.oikmo.main.entity.Camera;
import net.oikmo.main.entity.Light;
import net.oikmo.toolbox.rbxl.Item;
import net.oikmo.toolbox.rbxl.Parser;
import net.oikmo.toolbox.rbxl.Roblox;

public class RobloxScene extends Scene {
	
	
	public void loadRoblox(String thing) {
		this.setLoaded(false);
		boolean load = false;
		for(String map : maps) {		
			if(map.contentEquals(thing)) {
				load = true;
			}
		}
		if(!load) { return; }
		this.getParts().clear();
		createFrame();
		Roblox roblox = Parser.loadRBXL(thing);

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
		frame.dispose();
		this.setLoaded();
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

	public RobloxScene(String seed, TerrainTexturePack texturePack) {
		super(seed, texturePack, null);
	}
	JFrame frame = null;
	JLabel text = null;
	public List<String> maps;
	@Override
	public void init() {
		maps = new ArrayList<>();
		
		maps.add("2005StartPlace");
		maps.add("2006Crossroads");
		maps.add("2008ROBLOXHQ");
		maps.add("Colours");
		maps.add("Shapes");
		
		Light sun = new Light(new Vector3f(1,1000,1), new Vector3f(1.3f, 1.3f, 1.3f));
		this.addLight(sun);
		
		loadRoblox("2006Crossroads");
	}

	private void createFrame() {
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