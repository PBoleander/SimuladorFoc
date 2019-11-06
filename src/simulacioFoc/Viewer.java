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
		this.f = new Foc(img.getWidth(), img.getHeight(), BufferedImage.TYPE_4BYTE_ABGR, img);
		this.numRepaint = 0;
	}
	
	public Foc getFoc() {
		return this.f;
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), Color.BLACK, null);
		g.drawImage(f.getFoc(), 0, 0, this.getWidth(), this.getHeight(), null, null);
		try {
			TimeUnit.MILLISECONDS.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		numRepaint++;
		boolean actualitzarXispa = (numRepaint % 10 == 0 ? true : false);
		f.actualitzarMatriuT(actualitzarXispa);
		repaint();
	}
}
