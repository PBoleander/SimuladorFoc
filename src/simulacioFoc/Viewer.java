package simulacioFoc;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Viewer extends Canvas {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage img;
	private Foc f;
	
	public Viewer(Image i) {
		super();
		this.img = (BufferedImage) i;
		this.f = new Foc(img.getColorModel(), img.copyData(null), img.isAlphaPremultiplied(), null);
	}

	@Override
	public void paint(Graphics g) {
		f.actualitzarMatriuT();
		g.drawImage(f, 0, 0, this.getWidth(), this.getHeight(), Color.BLACK, null);
		repaint(0, this.getHeight() - 10, this.getWidth(), 10);
	}
}
