package simulacioFoc;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;

public class Foc extends BufferedImage {
	
	private int numCanals;
	private byte[] arrayBytes;
	private int[][] matriuTemperatures;
	private Color[] paleta;
	
	public Foc(int ample, int alt, int tipus) {
		super(ample, alt, tipus);
		this.numCanals = (this.getColorModel().hasAlpha() ? 4 : 3);
		this.arrayBytes = new byte[ample * alt * this.numCanals];
		PaletaColors pc = new PaletaColors(new Color(0, 0, 0), new Color(255, 210, 50), new Color(255, 180, 100), new Color(255, 150, 150));
		this.paleta = pc.getPaleta();
		inicialitzarMatriuT();
	}
	
	public void actualitzarMatriuT(boolean generarXispes) {
		for (int fila = this.getHeight() - 2; fila >= 0; fila--) {
			for (int columna = 1; columna < this.getWidth() - 1; columna++) {
				this.matriuTemperatures[fila][columna] = 
						(int) ((this.matriuTemperatures[fila][columna - 1] * 0.8 +
						this.matriuTemperatures[fila][columna] * 1.2 +
						this.matriuTemperatures[fila][columna + 1] * 0.8 +
						this.matriuTemperatures[fila + 1][columna - 1] * 0.8 +
						this.matriuTemperatures[fila + 1][columna] * 0.8 +
						this.matriuTemperatures[fila + 1][columna + 1] * 0.8) / 5.3);
				this.matriuTemperatures[fila][columna] = (this.matriuTemperatures[fila][columna] > 255 ? 255 : this.matriuTemperatures[fila][columna]);
			}
		}
		
		colorejarImatge();
		if (generarXispes)
			generarXispes();
	}
	
	public Foc getFoc() {
		this.setData(Raster.createRaster(this.getSampleModel(), new DataBufferByte(this.arrayBytes, this.arrayBytes.length), new Point()));
		return this;
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
		
		if (this.numCanals == 4) { // transparències
			if (this.matriuTemperatures[y][x] < 85)
				ba[i - 1] = (byte) 75;
			else if (this.matriuTemperatures[y][x] < 150)
				ba[i - 1] = (byte) 175;
			else
				ba[i - 1] = (byte) 255;
		}
	}

}
