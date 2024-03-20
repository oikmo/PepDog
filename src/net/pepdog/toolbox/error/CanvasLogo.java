package net.pepdog.toolbox.error;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

@SuppressWarnings("serial")
public class CanvasLogo extends Canvas {

	private BufferedImage logo;

	private boolean isIn = false;
	private int scale = 2;

	private byte size = 100;

	public CanvasLogo() {
		loadImage("logo");
	}

	public CanvasLogo(boolean isIn) {
		this.isIn = isIn;
		loadImage("logo");
	}

	public CanvasLogo(String credits) {
		this.isIn = true;
		loadImage(credits);
	}

	public CanvasLogo(String credits, int scale) {
		this.isIn = true;
		this.scale = scale;
		loadImage(credits);
	}

	public CanvasLogo(String credits, int scale, byte size) {
		this.isIn = true;
		this.scale = scale;
		loadImage(credits, size);
	}

	void loadImage(String fileName) {
		try {
			logo = ImageIO.read(this.getClass().getResourceAsStream("/assets/" + fileName + ".png"));
		}
		catch(IOException ioexception) { }
		setPreferredSize(new Dimension(size, size));
		setMinimumSize(new Dimension(size, size));
	}
	void loadImage(String fileName, byte size) {
		try {
			logo = ImageIO.read(this.getClass().getResourceAsStream("/assets/" + fileName + ".png"));
		}
		catch(IOException ioexception) { }
		setPreferredSize(new Dimension(size, size));
		setMinimumSize(new Dimension(size, size));
	}

	public void paint(Graphics g) {
		super.paint(g);
		if(!isIn) {
			g.drawImage(logo, getWidth() / 2 - logo.getWidth() / 2, 32, null);
		} else {
			g.drawImage(logo, (getWidth() - logo.getWidth()*scale)/2, (getHeight() - logo.getHeight()*scale)/2, logo.getWidth()*scale, logo.getHeight()*scale, null);
		}
	}
}