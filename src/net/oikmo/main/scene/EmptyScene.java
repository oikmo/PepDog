package net.oikmo.main.scene;

import org.lwjgl.util.vector.Vector3f;

import net.oikmo.engine.renderers.MasterRenderer;
import net.oikmo.engine.scene.Scene;
import net.oikmo.main.GameSettings;
import net.oikmo.main.entity.Camera;
import net.oikmo.main.entity.Light;

public class EmptyScene extends Scene {

	public EmptyScene() {
		super();
		this.setLoaded();
	}
	
	@Override
	public void init() {
		Light sun = new Light(new Vector3f(20000,20000,10000), new Vector3f(1.3f, 1.3f, 1.3f));
		this.addLight(sun);
	}

	@Override
	public void update(Camera camera) {
		MasterRenderer.getInstance().renderScene(GameSettings.postProcess, camera);
	}

	@Override
	public void update() {}

}
