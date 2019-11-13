package simulacioFoc;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

public class Viewer extends Canvas {
	
	private static final long serialVersionUID = 1L;
	private BufferedImage imgFons;
	private Foc f;
	private int numRepaint;
	
	private Graphics bufferGraphics;
	private BufferedImage borrador;
	private boolean pausa;
	
	public Viewer() {
		super();
		this.setBackground(Color.BLACK);
	}
	
	public Foc getFoc() {
		return this.f;
	}
	
	public boolean getPausa() {
		return this.pausa;
	}

	@Override
	public void paint(Graphics g) {
		if (this.imgFons == null) {
			String s = "Tria una imatge al quadre 'Ruta'";
			int longitudEnPixelsString = g.getFontMetrics().stringWidth(s);
			g.drawString(s, (this.getWidth() - longitudEnPixelsString) / 2, this.getHeight() / 2);
		} else {
			this.borrador = (BufferedImage) createImage(imgFons.getWidth(), imgFons.getHeight());
			this.bufferGraphics = borrador.getGraphics();
		
			bufferGraphics.clearRect(0, 0, imgFons.getWidth(), imgFons.getHeight());
			bufferGraphics.drawImage(imgFons, 0, 0, this);
			bufferGraphics.drawImage(f, 0, 0, this);
		
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
	}
	
	public void setImatgeFons(Image i) {
		this.imgFons = (BufferedImage) i;
		this.f = new Foc(imgFons.getWidth(), imgFons.getHeight(), BufferedImage.TYPE_4BYTE_ABGR, imgFons);
		this.numRepaint = 0;
		this.pausa = false;
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
