package net.pepdog.engine.gui;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.Texture;

import net.pepdog.engine.ResourceLoader;
import net.pepdog.engine.gui.component.slick.Cursor;
import net.pepdog.engine.gui.component.slick.GuiComponent;
import net.pepdog.main.Main;

public class Gui {
	
	private static Map<Integer, UnicodeFont> fontSizes = new HashMap<>();
	
	public static GuiComponent current = null;
	public static boolean lockedRightNow = false;

	protected static Image guiAtlas;
	
	protected static Graphics g = new Graphics();
	protected static UnicodeFont font;

	private static Font awtFont = null;
	protected static int fontSize = 16;

	protected static Cursor cursor;

	@SuppressWarnings("unchecked")
	public static void initFont() {
		try {
			awtFont = Font.createFont(Font.TRUETYPE_FONT, Main.class.getResourceAsStream("/assets/fonts/comic.ttf"));
		} catch (FontFormatException | IOException e) {}
		font = new UnicodeFont(awtFont.deriveFont(Font.PLAIN, fontSize));
		font.getEffects().add(new ColorEffect());

		font.addAsciiGlyphs();
		try {
			font.loadGlyphs();
		} catch (SlickException e1) {
			e1.printStackTrace();
		}
		
		fontSizes.put(fontSize, font);
		
		guiAtlas = new Image(ResourceLoader.loadUITexture("ui/gui"));
		cursor = new Cursor();
	}

	protected static void init() {}

	private void setupGL() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0,Display.getWidth(), Display.getHeight(), 0, -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	private void dropGL() {
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	protected void drawBackground(Texture texture) {
		setupGL();
		Image img = new Image(texture);
		img.setFilter(Image.FILTER_NEAREST);
		img.draw(0, 0, Display.getWidth(), Display.getHeight());
		dropGL();
	}

	protected void drawTiledBackground(Texture texture, int size) {
		setupGL();
		for(int x = 0; x < (Display.getWidth()+size)/size; x++) {
			for(int y = 0; y < (Display.getHeight()+size)/size; y++) {
				Image img = new Image(texture);
				
				img.setFilter(Image.FILTER_NEAREST);
				img.draw(x*size, y*size, size, size);
			}
		}
		dropGL();
	}

	protected void drawStringCentered(Color c, float x, float y, String text) {
		drawString(c, x-font.getWidth(text)/2, y-fontSize/2, text);
	}

	protected void drawStringCentered(float x, float y, String text) {
		drawString(Color.white, x-font.getWidth(text), y-fontSize/2, text);
	}

	protected void drawString(float x, float y, String text) {
		drawString(Color.white, x, y, text);
	}

	protected void drawString(Color c, float x, float y, String text) {
		setupGL();
		font.drawString(x, y, text, c);
		dropGL();	
	}

	protected void drawString(float fontSize, Color c, float x, float y, String text) {
		setupGL();
		UnicodeFont font = new UnicodeFont(awtFont.deriveFont(fontSize));
		font.drawString(x, y, text, c);
		dropGL();	
	}

	protected void drawShadowStringCentered(Color c, float x, float y, String text) {
		drawShadowString(c, x-font.getWidth(text)/2, y-fontSize/2, text);
	}
	
	protected void drawShadowStringCentered(float x, float y, String text) {
		drawShadowString(Color.white, x-font.getWidth(text)/2, y-fontSize/2, text);
	}
	
	protected void drawShadowStringCentered(UnicodeFont font, float x, float y, String text) {
		drawShadowString(font, Color.white, x-font.getWidth(text)/2, y-fontSize/2, text);
	}
	
	protected void drawShadowStringCentered(UnicodeFont font, Color c, float x, float y, String text) {
		drawShadowString(font, c, x-font.getWidth(text)/2, y-fontSize/2, text);
	}

	protected void drawShadowString(float x, float y, String text) {
		drawShadowString(font, Color.white, x, y, text);
	}
	
	protected void drawShadowString(Color c, float x, float y, String text) {
		drawShadowString(font, c, x, y, text);
	}
	
	protected void drawShadowString(UnicodeFont font, int x, int y, String text) {
		drawShadowString(font, Color.white, x, y, text);
	}
	
	protected void drawShadowString(UnicodeFont f, Color c, float x, float y, String text) {
		setupGL();
		UnicodeFont font = f;
		if(font == null) {
			font = Gui.font;
		}
		
		font.drawString(x+2, y+2, text, Color.gray);
		font.drawString(x, y, text, c);
		
		dropGL();	
	}
	
	
	
	@SuppressWarnings("unchecked")
	protected UnicodeFont calculateFont(int fontSize) {
		UnicodeFont font = fontSizes.get(fontSize);
		
		if(font == null)  {
			font = new UnicodeFont(awtFont.deriveFont(Font.PLAIN, fontSize));
			font.getEffects().add(new ColorEffect());

			font.addAsciiGlyphs();
			try {
				font.loadGlyphs();
			} catch (SlickException e1) {
				e1.printStackTrace();
			}
			fontSizes.put(fontSize, font);
		}
		return font;
	}

	protected void drawTexture(Texture texture, float x, float y, float width, float height) {
		drawTextureRaw(texture, x-width/2, y-height/2, width, height);
	}

	protected void drawImage(Image image, float x, float y, float width, float height) {
		if(image == null) { return; }
		setupGL();
		image.draw(x-width/2, y-height/2, width, height);
		dropGL();
	}

	protected void drawTextureRaw(Texture texture, float x, float y, float width, float height) {
		setupGL();
		Image img = new Image(texture);
		img.setFilter(Image.FILTER_NEAREST);
		img.draw(x, y, width, height);
		dropGL();
	}
	
	protected void drawSquare(float x, float y, float width, float height) {
		drawSquare(Color.darkGray, 1f, x, y, width, height);
	}

	protected void drawSquare(Color c, float lineWidth, float x, float y, float width, float height) {
		setupGL();
		g.setColor(c);
		g.setLineWidth(lineWidth);
		g.fillRect(x, y, width, height);
		g.draw(new Rectangle(x, y, width, height));
		
		g.setLineWidth(0);
		dropGL();
	}

	protected void drawSquareFilled(float x, float y, float width, float height) {
		drawSquareFilled(Color.darkGray, x, y, width, height);
	}

	protected void drawSquareFilled(Color c, float x, float y, float width, float height) {
		setupGL();
		g.setColor(c);
		g.draw(new Rectangle(x, y, width, height));
		g.fillRect(x, y, width, height);
		g.fillRoundRect(x, y, width, height, 1);
		dropGL();
	}

	public static void cleanUp() {
		Gui.lockedRightNow = false;
		Gui.current = null;
	}
}
