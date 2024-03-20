package net.oikmo.engine;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GLContext;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import net.oikmo.engine.models.RawModel;
import net.oikmo.engine.textures.TextureData;
import net.oikmo.main.Main;
import net.oikmo.toolbox.Logger;
import net.oikmo.toolbox.Logger.LogLevel;

/**
 * Loader class.
 * Loads textures, cubemaps, vbos (and updates them) and texture atlase.
 * 
 * @author Oikmo
 */
public class Loader {	
	private static final int BYTES_PER_PIXEL = 4;//3 for RGB, 4 for RGBA
	
	private static List<Integer> vaos = new ArrayList<Integer>();
	private static List<Integer> vbos = new ArrayList<Integer>();
	private static List<Integer> textures = new ArrayList<Integer>();
	
	public static int createEmptyVBO(int floatCount) {
		int vbo = GL15.glGenBuffers();
		vbos.add(vbo);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, floatCount * 4, GL15.GL_STREAM_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return vbo;
	}
	
	public static void addInstancedAttribute(int vao, int vbo, int attribute, int dataSize, int instancedDataLength, int offset) {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL30.glBindVertexArray(vao);
		GL20.glVertexAttribPointer(attribute, dataSize, GL11.GL_FLOAT, false, instancedDataLength * 4, offset * 4);
		GL33.glVertexAttribDivisor(attribute, 1);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	}
	
	public static void updateVBO(int vbo, float[] data, FloatBuffer buffer) {
		buffer.clear();
		buffer.put(data);
		buffer.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer.capacity() * 4, GL15.GL_STREAM_DRAW);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	public static int loadToVAO(float[] positions, float[] textureCoords) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, 2, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		unbindVAO();
		return vaoID;
	}
	
	/**
	 * Quad maker (a plane)
	 * 
	 * @param positions
	 * @param dimensions
	 * @return RawModel
	 */
	public static RawModel loadToVAO(float[] positions, int dimensions) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, dimensions, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length / dimensions);
	}
	
	/**
	 * Model loading. Used by the OBJFileLoader class, this method takes in the vertices, indices, normals and texture coordinates to return a RawModel
	 * 
	 * @param positions
	 * @param textureCoords
	 * @param normals
	 * @param indices
	 * @return RawModel
	 */
	public static RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}
	
	public static RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, float[] tangents, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		storeDataInAttributeList(3, 3, tangents);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}
	
	/**
	 * Loads textures based on input
	 * @param name
	 * @return texture (int)
	 */
	public static int loadGameTexture(String name) {
		return loadTexture("/assets/textures/" + name);
	}
	
	/**
	 * Loads font textures based on input
	 * @param name
	 * @return texture (int)
	 */
	public static int loadFontTexture(String name) {
		return loadTexture("/assets/fonts/" + name);
	}
	
	/**
	 * 
	 * @param name
	 * @param stoopid <- literally does nothing just put 0
	 * @return texture (int)
	 */
	public static int loadTexture(String name) {
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(Loader.class.getResourceAsStream(name + ".png"));
		} catch (FileNotFoundException e) {
			try {
				image = ImageIO.read(Loader.class.getResourceAsStream("missingTexture.png"));
			} catch (FileNotFoundException e1) {
				Main.error("No texture of missing texture???", e1);
			} catch (IOException e1) {
				Main.error("missing texture???", e1);
			}
			Logger.log(LogLevel.ERROR, "Texture name \"" + name + "\" could not be found! Loading placeholder!");
		} catch (IOException e) {
			try {
				image = ImageIO.read(Loader.class.getResourceAsStream("missingTexture.png"));
			} catch (FileNotFoundException e1) {
				Main.error("No texture of missing texture???", e1);
			} catch (IOException e1) {
				Main.error("missing texture???", e1);
			}
			
		} catch(IllegalArgumentException e) {
			Logger.log(LogLevel.ERROR, "Texture name \"" + name + "\" could not be found! Loading placeholder!");
			try {
				image = ImageIO.read(Loader.class.getResourceAsStream("missingTexture.png"));
			} catch (FileNotFoundException e1) {
				Main.error("No texture of missing texture???", e1);
			} catch (IOException e1) {
				Main.error("missing texture???", e1);
			}
		}
		
		int[] pixels = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL); //4 for RGBA, 3 for RGB

		for(int y = 0; y < image.getHeight(); y++){
			for(int x = 0; x < image.getWidth(); x++){
				int pixel = pixels[y * image.getWidth() + x];
				buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
				buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
				buffer.put((byte) (pixel & 0xFF));               // Blue component
				buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
			}
		}

		buffer.flip(); //DONT FORGET THIS OMG
		
		int textureID = GL11.glGenTextures(); //Generate texture ID
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID); //Bind texture ID
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		//Setup wrap mode
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

		//Setup texture scaling filtering
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		//GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST); //sharp

		//Send texel data to OpenGL
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
		//GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0f);
		
		textures.add(textureID);
		//Return the texture ID so we can bind it later again
		return textureID;
	}
	
	public static int loadCubeMap(String[] textureFiles) {
		int texID = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);
		
		for(int i = 0;i < textureFiles.length; i++) {
			TextureData data = decodeTextureFile(textureFiles[i]);
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
		}
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		textures.add(texID);
		return texID;
	}
	
	private static TextureData decodeTextureFile(String fileName) {
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		try {
			InputStream in = Loader.class.getResourceAsStream("/assets/textures/skybox/" + fileName + ".png");
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, Format.RGBA);
			buffer.flip();
			in.close();
		} catch (Exception e) {
			Main.error("Tried to load texture " + fileName + ", didn't work", e);
		}
		return new TextureData(buffer, width, height);
	}
	
	/* everything from here to there is vertex arrays stuff */
	private static int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	private static void storeDataInAttributeList(int attributeNummber, int coordinateSize, float[] data) {
		int vboID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNummber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
	}
	
	private static void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
	/* ------- everything from here is Index Buffers ------ */
	
	private static void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		
	}
	
	/**
	 * BUFFER CREATION!!!
	 * @param data int[]
	 * @return FloatBuffer
	 */
	private static IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	/**
	 * BUFFER CREATION!!!
	 * @param data int[]
	 * @return FloatBuffer
	 */
	private static FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	/**
	 * Cleans up all the buffers and data stored on memory
	 */
	public static void cleanUp() {
		vaos.forEach(GL30::glDeleteVertexArrays);
		vbos.forEach(GL15::glDeleteBuffers);
		textures.forEach(GL11::glDeleteTextures);
	}
}
