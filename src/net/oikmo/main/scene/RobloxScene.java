package net.oikmo.main.scene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.lwjgl.util.vector.Vector3f;

import net.oikmo.engine.Part;
import net.oikmo.engine.renderers.MasterRenderer;
import net.oikmo.engine.scene.Scene;
import net.oikmo.main.entity.Camera;
import net.oikmo.main.entity.Light;
import net.oikmo.toolbox.rbxl.Item;
import net.oikmo.toolbox.rbxl.Parser;
import net.oikmo.toolbox.rbxl.Roblox;

public class RobloxScene extends Scene {
	
	private List<Part> spawns;
	private List<Part> transparent;
	
	/**
	 * Retrieves parsed RBXL from {@link Parser#loadRBXL(String)} and processes each part into the scene.
	 * @param thing
	 */
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
	
	JFrame frame = null;
	JLabel text = null;
	public List<String> maps;
	@Override
	public void init() {
		maps = new ArrayList<>();
		spawns = new ArrayList<>();
		transparent = new ArrayList<>();
		maps.add("2005PirateShip");
		maps.add("2005StartPlace");
		maps.add("2006Crossroads");
		maps.add("2008ROBLOXHQ");
		maps.add("2008SwordFightonTheHeightsIV");
		maps.add("TESTColours");
		maps.add("TESTShapes");
		
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
				frame = new JFrame();
				frame.setSize(150, 150);
				frame.setVisible(true);
				text = new JLabel("PARTS ADDED : 0");
				
				frame.add(text);
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