package simulacioFoc;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

public class Viewer extends Canvas {
	
	private static final long serialVersionUID = 1L;
	private BufferedImage img;
	private Foc f;
	private int numRepaint;
	
	private Graphics bufferGraphics;
	private BufferedImage borrador;
	private boolean pausa;
	
	public Viewer(Image i) {
		super();
		this.img = (BufferedImage) i;
		this.f = new Foc(img.getWidth(), img.getHeight(), BufferedImage.TYPE_4BYTE_ABGR, img);
		
		this.setBackground(Color.BLACK);
		this.numRepaint = 0;
		this.pausa = false;
	}
	
	public Foc getFoc() {
		return this.f;
	}
	
	public boolean getPausa() {
		return this.pausa;
	}

	@Override
	public void paint(Graphics g) {
		this.borrador = (BufferedImage) createImage(img.getWidth(), img.getHeight());
		this.bufferGraphics = borrador.getGraphics();
		
		bufferGraphics.clearRect(0, 0, this.getWidth(), this.getHeight());
		bufferGraphics.drawImage(img, 0, 0, this);
		bufferGraphics.drawImage(f.getFoc(), 0, 0, this);
		
		g.drawImage(borrador, 0, 0, this.getWidth(), this.getHeight(), null);
		
		try {
			TimeUnit.MILLISECONDS.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		numRepaint++;
		boolean actualitzarXispa = (numRepaint % 10 == 0 ? true : false);
		f.actualitzarMatriuT(actualitzarXispa);
		if (!pausa)
			repaint();
	}
	
	public void setPausa(boolean pausa) {
		this.pausa = pausa;
	}
	
	@Override
	public void update(Graphics g) {
		paint(g);
	}
}
