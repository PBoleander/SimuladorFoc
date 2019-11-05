package simulacioFoc;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.util.Hashtable;

public class Foc extends BufferedImage {
	
	private int numCanals;
	private byte[] arrayBytes;
	private int[][] matriuTemperatures;
	private Color[] paleta;
	
	public Foc(ColorModel cm, WritableRaster wr, boolean isRasterPremultiplied, Hashtable<?,?> properties) {
		super(cm, wr, isRasterPremultiplied, properties);
		this.arrayBytes = ((DataBufferByte) this.getRaster().getDataBuffer()).getData();
		this.numCanals = (this.getColorModel().hasAlpha() ? 4 : 3);
		PaletaColors pc = new PaletaColors(new Color(0, 0, 0), new Color(100, 30, 0), new Color(170, 50, 30), new Color(255, 70, 50));
		this.paleta = pc.getPaleta();
		inicialitzarMatriuT();
	}
	
	public void actualitzarMatriuT() {
		for (int fila = 0; fila < this.getHeight() - 1; fila++) {
			for (int columna = 1; columna < this.getWidth() - 1; columna++) {
				this.matriuTemperatures[fila][columna] = 
						(int) ((this.matriuTemperatures[fila][columna - 1] +
						this.matriuTemperatures[fila][columna] +
						this.matriuTemperatures[fila][columna + 1] +
						this.matriuTemperatures[fila + 1][columna - 1] +
						this.matriuTemperatures[fila + 1][columna] +
						this.matriuTemperatures[fila + 1][columna + 1]) / 5.95);
				this.matriuTemperatures[fila][columna] = (this.matriuTemperatures[fila][columna] > 255 ? 255 : this.matriuTemperatures[fila][columna]);
			}
		}
		
		colorejarImatge();
		generarXispes();
	}
	
	private void colorejarImatge() {
		for (int fila = 0; fila < this.getHeight(); fila++) {
			for (int columna = 0; columna < this.getWidth(); columna++) {
				setColorPixel(this.arrayBytes, columna, fila, this.paleta[this.matriuTemperatures[fila][columna]]);
			}
		}
	}
	
	private void generarXispes() {
		for (int columna = 0; columna < this.getWidth(); columna++) {
			this.matriuTemperatures[this.getHeight() - 1][columna] = ((int) (2 * Math.random()) == 0 ? 255 : 0);
		}
	}
	
	private void inicialitzarMatriuT() {
		this.matriuTemperatures = new int[this.getHeight()][this.getWidth()];
		
		for (int fila = 0; fila < this.getHeight() - 1; fila++) {
			for (int columna = 0; columna < this.getWidth(); columna++) {
				this.matriuTemperatures[fila][columna] = 0;
			}
		}
		generarXispes();
	}
	
	private int passarXYAIndexArray(int x, int y) {
		return this.numCanals * (y * this.getWidth() + x) + this.numCanals - 3; // this.numCanals - 3 = offset per si hi ha canal alfa
	}
	
	private void setColorPixel(byte[] ba, int x, int y, Color c) {
		int i = passarXYAIndexArray(x, y);
		
		ba[i] = (byte) c.getBlue();
		ba[i + 1] = (byte) c.getGreen();
		ba[i + 2] = (byte) c.getRed();
	}

}
