package net.oikmo.main.scene;

import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import net.oikmo.engine.Entity;
import net.oikmo.engine.Part;
import net.oikmo.engine.models.TexturedModel;
import net.oikmo.engine.renderers.MasterRenderer;
import net.oikmo.engine.scene.Scene;
import net.oikmo.engine.terrain.Terrain;
import net.oikmo.engine.textures.TerrainTexture;
import net.oikmo.engine.textures.TerrainTexturePack;
import net.oikmo.engine.water.WaterTile;
import net.oikmo.main.GameSettings;
import net.oikmo.main.entity.Camera;
import net.oikmo.main.entity.Light;
import net.oikmo.toolbox.Maths;

public class MainScene extends Scene {

	public MainScene(String seed, TerrainTexturePack texturePack, TerrainTexture blendMap) {
		super(seed, texturePack, blendMap);
	}
	

	@Override
	public void init() {
		Terrain terrain = new Terrain(this.getSeed(), 0,-1, this.getTexturePack(), this.getBlendMap());	
		
		this.addTerrain(terrain);
		
		Light sun = new Light(new Vector3f(20000,20000,10000), new Vector3f(1.3f, 1.3f, 1.3f));
		this.addLight(sun);
		WaterTile water = new WaterTile(400, -400, -4, 400);
		this.addWater(water);
		
		TexturedModel fern = new TexturedModel("fern");
		fern.getTexture().setNumberOfRows(2);
		TexturedModel lowPoly = new TexturedModel("tree");
		Random random = new Random(Maths.getSeedFromName(this.getSeed()));
		for(int i = 0; i < 400; i++) {
			if(i % 1 == 0) { 
				float x = random.nextFloat() * 800;
				float z = random.nextFloat() * -800;
				Terrain t = terrain;
				Entity fernn = new Entity(fern, new Vector3f(x, t.getHeightOfTerrain(x, z), z), new Vector3f(), 1, random.nextInt(4));
				//fernn.setAABB(null);
				this.addEntity(fernn);
			}
			if(i % 3 == 0) {
				float x = random.nextFloat() * 800;
				float z = random.nextFloat() * -800;
				Terrain t = terrain;
				Entity tree = new Entity(lowPoly, new Vector3f(x, t.getHeightOfTerrain(x, z), z),new Vector3f(), 15);
				this.addEntity(tree);
			}
		}
		
		Part part = new Part(new Vector3f(0,0,0), new Vector3f(0,0,0), new Vector3f(10,1,10));
		this.addPart(part);
		this.setLoaded();
	}
	
	@Override
	public void update(Camera camera) {	
		MasterRenderer.getInstance().renderSceneWater(GameSettings.postProcess, camera);
	}
	
	@Override
	public void update() {}

}
