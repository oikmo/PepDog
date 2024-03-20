package net.pepdog.engine.renderers.particles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.util.vector.Matrix4f;

import net.pepdog.main.entity.Camera;

public class ParticleMaster {
	
	private static Map<ParticleTexture, List<Particle>> particles = new HashMap<>();
	private static ParticleRenderer renderer;
	
	public static void init(Matrix4f projectionMatrix) {
		renderer = new ParticleRenderer(projectionMatrix);
	}
	
	public static void update(Camera camera) {
		Iterator<Entry<ParticleTexture, List<Particle>>> mapIterator = particles.entrySet().iterator();
		while(mapIterator.hasNext()) {
			List<Particle> list = mapIterator.next().getValue();
			Iterator<Particle> iterator = list.iterator();
			while(iterator.hasNext()) {
				Particle p = iterator.next();
				boolean stillAlive = p.update(camera);
				if(!stillAlive) {
					iterator.remove();
					if(list.isEmpty()) {
						mapIterator.remove();
					}
				}
			}
		}
		
	}
	
	public static void renderParticles(Camera camera) {
		renderer.render(particles, camera);
	}
	
	public static void cleanUp() {
		renderer.cleanUp();
	}
	
	public static void addParticle(Particle particle) {
		ParticleTexture model = particle.getTexture();
		List<Particle> batch = particles.get(model);
		if(batch != null) {
			batch.add(particle);
		} else {
			List<Particle> newBatch = new ArrayList<>();
			newBatch.add(particle);
			particles.put(model, newBatch);
		}
	}
}
