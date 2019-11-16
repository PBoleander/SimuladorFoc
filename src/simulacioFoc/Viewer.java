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
			if (pintarImatgesFixes) {
				pintarImatgesFixes = false;
				pintaImatgesFixes(g);
			}
			
			pintaImatgeVariable(g);
			
			try {
				TimeUnit.MILLISECONDS.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			numRepaint++;
			boolean actualitzarXispa = (numRepaint % 5 == 0 ? true : false);
			foc.actualitzar(actualitzarXispa);
			
			if (!pausa)
				repaint();
		}
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
	
	private void pintaImatgesFixes(Graphics g) {
		BufferedImage imgFonsConvolucionada = new BufferedImage(imgFons.getWidth(), imgFons.getHeight(), imgFons.getType());
		imgFonsConvolucionada.setData(Raster.createRaster(imgFons.getSampleModel(), 
														  new DataBufferByte(foc.getMatriuBordesImgFons(), foc.getMatriuBordesImgFons().length),
														  new Point()));
		
		g.drawImage(imgFons, 0, 0, this.getWidth() / 2, this.getHeight() / 2, null);
		g.drawImage(imgFonsConvolucionada, 0, this.getHeight() / 2, this.getWidth() / 2, this.getHeight() / 2, null);
	}
	
	private void pintaImatgeVariable(Graphics g) {
		BufferedImage borrador = (BufferedImage) createImage(this.getWidth() / 2, this.getHeight());
		Graphics bufferGraphics = borrador.getGraphics();
	
		bufferGraphics.clearRect(0, 0, borrador.getWidth(), borrador.getHeight());
		bufferGraphics.drawImage(imgFons, 0, 0, borrador.getWidth(), borrador.getHeight(), null);
		bufferGraphics.drawImage(foc, 0, 0, borrador.getWidth(), borrador.getHeight(), null);
	
		g.drawImage(borrador, this.getWidth() / 2, 0, borrador.getWidth(), borrador.getHeight(), null);
	}
}
