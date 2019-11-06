package simulacioFoc;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

public class Viewer extends Canvas {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage img;
	private Foc f;
	private int numRepaint;
	
	public Viewer(Image i) {
		super();
		this.img = (BufferedImage) i;
		this.f = new Foc(img.getColorModel(), img.copyData(null), img.isAlphaPremultiplied(), null);
		this.numRepaint = 0;
	}

	@Override
	public void paint(Graphics g) {
		boolean xispa;
		xispa = (numRepaint % 5 == 0 ? true : false);
		f.actualitzarMatriuT(xispa);
		g.drawImage(f, 0, 0, this.getWidth(), this.getHeight(), Color.BLACK, null);
		try {
			TimeUnit.MILLISECONDS.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		numRepaint++;
		repaint();
	}
}
