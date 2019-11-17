package simulacioFoc;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.util.concurrent.TimeUnit;

public class Viewer extends Canvas implements ComponentListener {
	
	private static final long serialVersionUID = 1L;
	private BufferedImage imgFons;
	private Foc foc;
	private int numRepaint;
	
	private boolean pausa;
	private boolean pintarImatgesFixes;
	private int ampliacio;
	
	@Override
	public void componentHidden(ComponentEvent e) {}
	@Override
	public void componentMoved(ComponentEvent e) {}
	@Override
	public void componentResized(ComponentEvent e) {
		this.pintarImatgesFixes = true;
		repaint();
	}
	@Override
	public void componentShown(ComponentEvent e) {
		this.pintarImatgesFixes = true;
		repaint();
	}
	
	public Viewer() {
		super();
		this.pintarImatgesFixes = false;
		this.setBackground(Color.BLACK);
		this.addComponentListener(this);
	}
	
	public Foc getFoc() {
		return this.foc;
	}
	
	public boolean getPausa() {
		return this.pausa;
	}

	@Override
	public void paint(Graphics g) {
		if (this.imgFons == null) {
			String s = "Tria una imatge per començar el foc";
			int longitudEnPixelsString = g.getFontMetrics().stringWidth(s);
			g.drawString(s, (this.getWidth() - longitudEnPixelsString) / 2, this.getHeight() / 2);
		} else {
			switch (ampliacio) {
			case 1: // Fons
				pintaImatgeFons(g, 0, 0, this.getWidth(), this.getHeight());
				break;
			case 2: // Convolucionada
				pintaImatgeConvolucionada(g, 0, 0, this.getWidth(), this.getHeight());
				break;
			case 3: // Foc
				pintaFoc(g, 0, 0, this.getWidth(), this.getHeight());
				
				if (!pausa) {
					actualitzarFoc();
					repaint();
				}
				break;
			default: // No ampliació
				if (pintarImatgesFixes) {
					pintarImatgesFixes = false;
					pintaImatgeFons(g, 0, 0, this.getWidth() / 2, this.getHeight() / 2);
					pintaImatgeConvolucionada(g, 0, this.getHeight() / 2, this.getWidth() / 2, this.getHeight() / 2);
				}
				
				pintaFoc(g, this.getWidth() / 2, 0, this.getWidth() / 2, this.getHeight());
				
				if (!pausa) {
					actualitzarFoc();
					repaint();
				}
			}
		}
	}
	
	public void setAmpliacio(int ampliacio) {
		if (ampliacio == 0) this.pintarImatgesFixes = true;
		this.ampliacio = ampliacio;
	}
	
	public void setImatgeFons(Image i) {
		this.imgFons = (BufferedImage) i;
		this.foc = new Foc(imgFons.getWidth(), imgFons.getHeight(), BufferedImage.TYPE_4BYTE_ABGR, imgFons);
		this.numRepaint = 0;
		this.pausa = false;
		this.pintarImatgesFixes = true;
		repaint();
	}
	
	public void setPausa(boolean pausa) {
		this.pausa = pausa;
	}
	
	@Override
	public void update(Graphics g) {
		paint(g);
	}
	
	private void actualitzarFoc() {
		try {
			TimeUnit.MILLISECONDS.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		numRepaint++;
		boolean actualitzarXispa = (numRepaint % 5 == 0 ? true : false);
		foc.actualitzar(actualitzarXispa);
	}
	
	private void pintaImatgeConvolucionada(Graphics g, int x, int y, int ample, int alt) {
		BufferedImage imgFonsConvolucionada = new BufferedImage(imgFons.getWidth(), imgFons.getHeight(), imgFons.getType());
		imgFonsConvolucionada.setData(Raster.createRaster(imgFons.getSampleModel(), 
														  new DataBufferByte(foc.getMatriuBordesImgFons(), foc.getMatriuBordesImgFons().length),
														  new Point()));
		
		g.drawImage(imgFonsConvolucionada, x, y, ample, alt, null);
	}
	
	private void pintaImatgeFons(Graphics g, int x, int y, int ample, int alt) {
		g.drawImage(imgFons, x, y, ample, alt, null);
	}
	
	private void pintaFoc(Graphics g, int x, int y, int ample, int alt) {
		BufferedImage borrador = (BufferedImage) createImage(ample, alt);
		Graphics borradorGraphics = borrador.getGraphics();
	
		borradorGraphics.clearRect(0, 0, borrador.getWidth(), borrador.getHeight());
		borradorGraphics.drawImage(imgFons, 0, 0, borrador.getWidth(), borrador.getHeight(), null);
		borradorGraphics.drawImage(foc, 0, 0, borrador.getWidth(), borrador.getHeight(), null);
	
		g.drawImage(borrador, x, y, borrador.getWidth(), borrador.getHeight(), null);
	}
}
