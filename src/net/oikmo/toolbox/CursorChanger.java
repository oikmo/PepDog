package net.oikmo.toolbox;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

public class CursorChanger {
	
	public static  void loadCursor(BufferedImage img) throws LWJGLException {
	    final int w = img.getWidth();
	    final int h = img.getHeight();

	    int rgbData[] = new int[w * h];

	    for (int i = 0; i < rgbData.length; i++) {
	        int x = i % w;
	        int y = h - 1 - i / w; // this will also flip the image vertically

	        rgbData[i] = img.getRGB(x, y);
	    }

	    IntBuffer buffer = BufferUtils.createIntBuffer(w * h);
	    buffer.put(rgbData);
	    buffer.rewind();

	    Cursor cursor = new Cursor(w, h, 2, h - 2, 1, buffer, null);

	    Mouse.setNativeCursor(cursor);
	}
	
	public static  void loadCursor(String imgName) throws LWJGLException {
		BufferedImage img = load(imgName);
		
	    final int w = img.getWidth();
	    final int h = img.getHeight();

	    int rgbData[] = new int[w * h];
	    
	    Color c;

	    
	    for (int i = 0; i < rgbData.length; i++) {
	        int x = i % w;
	        int y = h - 1 - i / w; // this will also flip the image vertically
	        
	        c = new Color(img.getRGB(x, y), true);
	        
	        rgbData[i] = c.getRGB();
	    }
	    
	    IntBuffer buffer = BufferUtils.createIntBuffer(w * h);
	    buffer.put(rgbData);
	    buffer.rewind();

	    Cursor cursor = new Cursor(w, h, 2, h - 2, 1, buffer, null);

	    Mouse.setNativeCursor(cursor);
	}
	
	private static BufferedImage load(String imageName) {
		BufferedImage image = null;

		try {
			image = ImageIO.read(CursorChanger.class.getResourceAsStream("/assets/cursors/" + imageName + ".png"));

		} catch (IOException e) {
			e.printStackTrace();
		}

		return image;
	}
	
}
