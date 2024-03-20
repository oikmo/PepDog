package net.pepdog.main.scene;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JLabel;

import org.lwjgl.util.vector.Vector3f;

import net.pepdog.engine.Part;
import net.pepdog.engine.renderers.MasterRenderer;
import net.pepdog.engine.scene.Scene;
import net.pepdog.main.Main;
import net.pepdog.main.entity.Camera;
import net.pepdog.main.entity.Light;
import net.pepdog.toolbox.rbxl.Item;
import net.pepdog.toolbox.rbxl.Parser;
import net.pepdog.toolbox.rbxl.Roblox;

public class RobloxScene extends Scene {
	
	private List<Part> spawns;
	private List<Part> transparent;
	
	/**
	 * Retrieves parsed RBXL from {@link Parser#loadRBXL(String)} and processes each part into the scene.
	 * @param thing
	 */
	public void loadRoblox(String thing) {
		this.setLoaded(false);

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
		frame.setVisible(false);
		if(transparent.size() != 0) {
			this.getParts().addAll(transparent.size()+1, transparent);
		}
		
		this.setLoaded();
	}
	
	/**
	 * Recursion loop to skim through each part (and spawnlocation that is nested in a model or not.
	 * @param item
	 */
	public void processItem(Item item) {
		if(text != null) {
			text.setText("PARTS ADDED : "+this.getParts().size());
			frame.paintAll(frame.getGraphics());
		}
	    if (item.getClazz().contentEquals("Part") || item.getClazz().contentEquals("SpawnLocation")) {
	    	Part part = Part.createPartFromItem(item);
	    	if(part.getTransparency() != 1f && !item.getClazz().contentEquals("SpawnLocation")) {
	    		this.transparent.add(part);
	    	} else {
	    		this.addPart(Part.createPartFromItem(item));
	 	        if(item.getClazz().contentEquals("SpawnLocation")) {
	 		    	this.spawns.add(part);
	 	        }
	    	}
	       
	    } else if (item.getClazz().contentEquals("Model")) {
	        for (Item nestedItem : item.getItem()) {
	            processItem(nestedItem); // Recursive call
	        }
	    }
	}
	
	Frame frame = Main.frame;
	JLabel text = null;
	@Override
	public void init() {
		spawns = new ArrayList<>();
		transparent = new ArrayList<>();
		
		Light sun = new Light(new Vector3f(1,1000,1), new Vector3f(1.3f, 1.3f, 1.3f));
		this.addLight(sun);
	}
	
	
	
	/**
	 * Shows a counter of how many parts have been added.
	 */
	private void createFrame() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				frame.setLocation(10, 10);
				frame.setSize(150, 150);
				text = new JLabel("PARTS ADDED : 0");
				frame.add(text);
				frame.setVisible(true);
				frame.paintAll(frame.getGraphics());
			}
			
		}).start();
	}
	
	public Vector3f getRandomSpawn() {
		if(spawns.size() != 0) { 
			Collections.shuffle(spawns);
			int partNum = ThreadLocalRandom.current().nextInt(0, spawns.size());
			Part part = spawns.get(partNum);
			return new Vector3f(part.getPosition().x,part.getPosition().y+3f,part.getPosition().z);
		} else {
			return new Vector3f(0,0,0);
		}
		
		
	}
	
	@Override
	public void update(Camera camera) {	
		MasterRenderer.getInstance().renderScene(camera);
	}
}