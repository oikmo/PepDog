package net.oikmo.engine.models;

public class CylinderModel {
	public static float[] vertices;
	public static float[] uv;
	public static int[] indices;

	static {
		int precisionDeg = 10;
		vertices = createVertices(precisionDeg);
		uv = createUVs(precisionDeg);
		indices = createIndices(precisionDeg);
	}

	

	private static float[] createVertices(int precisionDeg) {
		// Adjust precision as needed
		int numVertices = (360 / precisionDeg + 1) * 2 + 2; // Number of vertices in one loop

		float[] vertices = new float[numVertices * 3]; // Each vertex has x, y, z coordinates

		int index = 0;

		// Generate circular cross-section vertices
		for (int j = 0; j <= 360; j += precisionDeg) {
			vertices[index++] = (float) Math.cos(Math.toRadians(j));
			vertices[index++] = 1.0f;
			vertices[index++] = (float) Math.sin(Math.toRadians(j));

			vertices[index++] = (float) Math.cos(Math.toRadians(j));
			vertices[index++] = -1.0f;
			vertices[index++] = (float) Math.sin(Math.toRadians(j));
		}

		// Generate top cap vertex
		vertices[index++] = 0.0f;
		vertices[index++] = 1.0f;
		vertices[index++] = 0.0f;

		// Generate bottom cap vertex
		vertices[index++] = 0.0f;
		vertices[index++] = -1.0f;
		vertices[index++] = 0.0f;

		return vertices;
	}

	private static float[] createUVs(int precisionDeg) {
		int numVertices = 360 / precisionDeg + 1;
		int numCircles = 2;

		float[] uvs = new float[numVertices * numCircles * 2];

		int index = 0;

		// UVs for the circular cross-sections
		for (int j = 0; j <= 360; j += precisionDeg) {
			uvs[index++] = (float) j / 360.0f;
			uvs[index++] = 0.0f; // Assuming no v-component variation for the circular cross-sections
		}
		
		// UVs for the top and bottom caps
		for (int i = 0; i < numCircles; i++) {
			for (int k = 0; k <= 360; k += precisionDeg) {
				try {
					uvs[index++] = (float) k / 360.0f;
					uvs[index++] = i == 0 ? 1.0f : 0.0f; // 1.0f for the top cap, 0.0f for the bottom cap
				} catch(java.lang.ArrayIndexOutOfBoundsException e) {}
				
			}
		}

		return uvs;
	}


	private static int[] createIndices(int precisionDeg) {
		int numVerticesPerCircle = 360 / precisionDeg + 1;

		int numCircles = 2;  // Top and bottom caps
		int numIndicesPerCircle = numVerticesPerCircle * 2 * 3;  // Each circle has 2 triangles

		int[] indices = new int[numCircles * numIndicesPerCircle];
		int index = 0;

		// Indices for the side faces
		for (int i = 0; i < numVerticesPerCircle - 1; i++) {
			indices[index++] = i;
			indices[index++] = i + numVerticesPerCircle;
			indices[index++] = i + 1;

			indices[index++] = i + 1;
			indices[index++] = i + numVerticesPerCircle;
			indices[index++] = i + numVerticesPerCircle + 1;
		}

		// Wrap around for the last vertices in the circle
		indices[index++] = numVerticesPerCircle - 1;
		indices[index++] = 2 * numVerticesPerCircle - 1;
		indices[index++] = 0;

		indices[index++] = 0;
		indices[index++] = 2 * numVerticesPerCircle - 1;
		indices[index++] = numVerticesPerCircle;

		// Indices for the top and bottom caps
		for (int i = 0; i < numVerticesPerCircle - 1; i++) {
			// Top cap
			indices[index++] = i;
			indices[index++] = i + 1;
			indices[index++] = numVerticesPerCircle * 2;

			// Bottom cap
			indices[index++] = i + numVerticesPerCircle;
			indices[index++] = i + numVerticesPerCircle + 1;
			indices[index++] = numVerticesPerCircle * 2 + 1;
		}

		// Wrap around for the last vertices in the circle
		// Top cap
		indices[index++] = numVerticesPerCircle - 1;
		indices[index++] = 0;
		indices[index++] = numVerticesPerCircle * 2;

		// Bottom cap
		indices[index++] = 2 * numVerticesPerCircle - 1;
		indices[index++] = numVerticesPerCircle;
		indices[index++] = numVerticesPerCircle * 2 + 1;

		return indices;
	}

}