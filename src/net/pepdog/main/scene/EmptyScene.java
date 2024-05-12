package net.pepdog.main.scene;

import org.lwjgl.util.vector.Vector3f;

import net.pepdog.engine.entity.Camera;
import net.pepdog.engine.entity.Light;
import net.pepdog.engine.renderers.MasterRenderer;
import net.pepdog.engine.scene.Scene;

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
		MasterRenderer.getInstance().renderScene(camera);
	}
}
