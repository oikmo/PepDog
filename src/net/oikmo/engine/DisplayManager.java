package net.oikmo.engine;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.Calendar;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import net.oikmo.main.Main;
import net.oikmo.toolbox.IconUtils;

/**
 * Handles window layout and updates
 * 
 * @author <i>Oikmo</i>
 */
public class DisplayManager {
	static int fps;
	static int literalfps;
	static long lastFPS;
	private static long lastFrameTime;
	private static float delta;
	/**
	 * Creates window by size and OPENGL version
	 * 
	 * @author <i>Oikmo</i>
	 */
	public static void createDisplay() {
		//ContextAttribs attribs = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true);
		
		try {
			//Display.create(new PixelFormat(), attribs);
			Display.create();
			final ByteBuffer[] windowsFavicon = IconUtils.getFavicon();
			Display.setIcon(windowsFavicon);
			Display.setDisplayMode(new DisplayMode(Main.WIDTH, Main.HEIGHT));
			Display.setTitle("RBXL Loader Engine");
			Display.setResizable(true);
			
			
			lastFPS = getCurrentTime();
		} catch (LWJGLException e) {
			Main.error("Failed to create display", e);
		}
		
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
		lastFrameTime = getCurrentTime();
	}
	
	public static void updateDisplay() {
		updateFPS();
		Display.update();
		Display.sync(60);
		
		if (Display.wasResized() &&  (Display.isFullscreen() || !Display.isFullscreen())) { 
			GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_F11)) {
			try {
				if(Display.isFullscreen()) {
					Display.setDisplayMode(new DisplayMode(Main.WIDTH, Main.HEIGHT));
					Display.setFullscreen(false);
				} else {
					Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
				}
			} catch(LWJGLException e) {
				Main.error("LWJGLException", e);
			}
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_F2)) {
			saveScreenshot();
		}
		
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime)/1000f;
		lastFrameTime = currentFrameTime;		
	}
	
	public static void closeDisplay() {
		Display.destroy();
		System.exit(0);
	}
	
	public static void saveScreenshot() {
	    // Tries to create an image, otherwise throws an exception.
		Calendar cal = Calendar.getInstance();
		int dy = cal.get(Calendar.DAY_OF_MONTH);
		int mon = cal.get(Calendar.MONTH) + 1;
		int yr = cal.get(Calendar.YEAR);
		
		int hr = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		int sec = cal.get(Calendar.SECOND);
		
		String day = "" + dy;
		String month = "" + mon;
		String year = String.valueOf(yr).substring(2);
		
		String hour = "" + hr;
		String minute = "" + min;
		String second = "" + sec;
		
		if(dy < 10) { day = "0" + dy; }
		if(mon < 10) { month = "0" + mon; }
		
		if(hr < 10) { hour = "0" + hr; } 
		if(min < 10) { minute = "0" + min; }
		if(sec < 10) { second = "0" + sec; }
		
	    String name = day + "-" + month + "-" + year + "_" + hour + "." + minute + "." + second;
	    File saveDirectory =  new File(Main.getUNDECIDEDDir().getPath()+"/screenshots/");
	 
	    if (!saveDirectory.exists()) {
	        try {
	        	saveDirectory.mkdir();
	        } catch (SecurityException e) {
	            return;
	        }
	    }
	 
	    File file = new File(saveDirectory + "/" + name + ".png"); // The file to save the pixels too.
	    String format = "png";
	    try {
	        ImageIO.write(getImage(null, null), format, file);
	    } catch (Exception e) {
	    	Main.error("Failed to save screenshot! : " + name+".png", e);
	    }
	}
	
	static void updateFPS() {
		if (getCurrentTime() - lastFPS > 1000)	{
			literalfps = fps;
			fps = 0; //reset the FPS counter
			lastFPS += 1000; //add one second
			//System.gc();
		}
		fps++;
	}
	
	public static int getFPSCount() {
		return (int)literalfps;
	}

	private static long getCurrentTime() {
		return Sys.getTime()*1000/Sys.getTimerResolution();
	}
	
	public static float getDelta() {
		return delta + 1;
	}
	
	/**
	 * Creates a buffered image from the OpenGL pixel buffer.
	 *
	 * @param destination The destination BufferedImage to store in, if null a new one will be created.
	 * @param buffer The buffer to store OpenGL data into, if null a new one will be created.
	 *
	 * @return A new buffered image containing the displays data.
	 */
	public static BufferedImage getImage(BufferedImage destination, ByteBuffer buffer) {
	    // Creates a new destination if it does not exist, or fixes a old one,
	    if (destination == null || buffer == null || destination.getWidth() != Display.getWidth() || destination.getHeight() != Display.getHeight()) {
	        destination = new BufferedImage(Display.getWidth(), Display.getHeight(), BufferedImage.TYPE_INT_RGB);
	        buffer = BufferUtils.createByteBuffer(Display.getWidth() * Display.getHeight() * 4);
	    }
	 
	    // Creates a new buffer and stores the displays data into it.
	    GL11.glReadPixels(0, 0, Display.getWidth(), Display.getHeight(), GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
	 
	    // Transfers the data from the buffer into the image. This requires bit shifts to get the components data.
	    for (int x = destination.getWidth() - 1; x >= 0; x--) {
	        for (int y = destination.getHeight() - 1; y >= 0; y--) {
	            int i = (x + Display.getWidth() * y) * 4;
	            destination.setRGB(x, destination.getHeight() - 1 - y, (((buffer.get(i) & 0xFF) & 0x0ff) << 16) | (((buffer.get(i + 1) & 0xFF) & 0x0ff) << 8) | ((buffer.get(i + 2) & 0xFF) & 0x0ff));
	        }
	    }
	 
	    return destination;
	}
	
	
	public static float getFrameTimeSeconds() {
		return delta;
	}
	public static Vector2f getNormalisedMouseCoords() {
		float normalisedX = -1.0f + 2.0f * (float) Mouse.getX() / (float) Display.getWidth();
		float normalisedY = 1.0f - 2.0f * (float) Mouse.getY() / (float) Display.getHeight();
		return new Vector2f(normalisedX, normalisedY);
	}
}

