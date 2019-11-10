package simulacioFoc;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;

public class Foc extends BufferedImage {
	
	private int numCanals;
	private byte[] arrayBytesFoc;
	private BufferedImage imatgeFons;
	private byte[] arrayBytesImatgeFons;
	private int nCanalsImgFons;
	private int[][] matriuTemperatures;
	private byte[] matriuBordes;
	private Color[] paleta;
	private boolean xispesABordes;
	
	public Foc(int ample, int alt, int tipus, BufferedImage imgFons) {
		super(ample, alt, tipus);
		
		this.imatgeFons = imgFons;
		this.nCanalsImgFons = this.imatgeFons.getColorModel().hasAlpha() ? 4 : 3;
		this.arrayBytesImatgeFons = ((DataBufferByte) this.imatgeFons.getRaster().getDataBuffer()).getData();
		
		this.numCanals = (this.getColorModel().hasAlpha() ? 4 : 3);
		this.arrayBytesFoc = new byte[ample * alt * this.numCanals];
		this.matriuBordes = new byte[this.arrayBytesImatgeFons.length];
		
		PaletaColors pc = new PaletaColors(new Color(255, 255, 200, 0), new Color(255, 255, 100, 50), new Color(255, 255, 0, 180), new Color(255, 255, 255, 255));
		this.paleta = pc.getPaleta();
		
		this.xispesABordes = false;
		
		inicialitzarMatriuT();
		inicialitzarArrayBytes();
		generarXispes(true);
	}
	
	public void actualitzarMatriuT(boolean generarXispes) {
		for (int fila = this.getHeight() - 2; fila >= 0; fila--) {
			for (int columna = 1; columna < this.getWidth() - 1; columna++) {
				if (!this.xispesABordes || (this.xispesABordes && !esBordeAquestPixel(fila, columna))) {
					this.matriuTemperatures[fila][columna] = 
							(int) ((this.matriuTemperatures[fila][columna - 1] * 1.25 +
							this.matriuTemperatures[fila][columna] * 2 +
							this.matriuTemperatures[fila][columna + 1] * 1.25 +
							this.matriuTemperatures[fila + 1][columna - 1] * 0.75 +
							this.matriuTemperatures[fila + 1][columna] * 1.5 +
							this.matriuTemperatures[fila + 1][columna + 1] * 0.75) / 7.55);
					
					this.matriuTemperatures[fila][columna] = (this.matriuTemperatures[fila][columna] > 255 ? 255 : this.matriuTemperatures[fila][columna]);
				}
			}
		}
		
		colorejarImatge();
		if (generarXispes)
			generarXispes(false);
	}
	
	public Foc getFoc() {
		this.setData(Raster.createRaster(this.getSampleModel(), new DataBufferByte(this.arrayBytesFoc, this.arrayBytesFoc.length), new Point()));
		return this;
	}
	
	public void setXispesABordes(boolean bordes) {
		this.xispesABordes = bordes;
		inicialitzarMatriuT();
		inicialitzarArrayBytes();
		generarXispes(true);
	}
	
	private void colorejarImatge() {
		for (int fila = 0; fila < this.getHeight(); fila++) {
			for (int columna = 0; columna < this.getWidth(); columna++) {
				setColorPixel(this.arrayBytesFoc, this.numCanals, columna, fila, this.paleta[this.matriuTemperatures[fila][columna]]);
			}
		}
	}
	
	private Color corregirColor(int r, int g, int b) {
		r = (r > 255 ? 255 : r);
		g = (g > 255 ? 255 : g);
		b = (b > 255 ? 255 : b);
		
		r = (r < 0 ? 0 : r);
		g = (g < 0 ? 0 : g);
		b = (b < 0 ? 0 : b);
		
		return new Color(r, g, b);
	}
	
	private boolean costatsEncesos(int fila, int columna) {
		if (this.xispesABordes)
			return ((this.matriuTemperatures[fila - 1][columna - 1] > 0 || this.matriuTemperatures[fila - 1][columna] > 0 ||
					 this.matriuTemperatures[fila - 1][columna + 1] > 0 || this.matriuTemperatures[fila][columna - 1] > 0 ||
					 this.matriuTemperatures[fila][columna + 1] > 0 || this.matriuTemperatures[fila + 1][columna - 1] > 0 ||
					 this.matriuTemperatures[fila + 1][columna] > 0 || this.matriuTemperatures[fila + 1][columna + 1] > 0) ? true : false);
		else
			return this.matriuTemperatures[fila][columna - 1] > 0 || this.matriuTemperatures[fila][columna + 1] > 0 ? true : false;
	}
	
	private void detectarBordes() {
		int[][] matriu = {{0, 1, 0}, {1, -4, 1}, {0, 1, 0}};
		int nouR, nouG, nouB;
		
		for (int filaFons = 1; filaFons < this.getHeight() - 1; filaFons++) {
			for (int columnaFons = 1; columnaFons < this.getWidth() - 1; columnaFons++) {
				nouR = nouG = nouB = 0;
				
				for (int filaMatriu = 0; filaMatriu < 3; filaMatriu++) {
					for (int columnaMatriu = 0; columnaMatriu < 3; columnaMatriu++) {
						Color c = getColorPixel(this.arrayBytesImatgeFons, this.nCanalsImgFons, columnaFons + columnaMatriu - 1, filaFons + filaMatriu - 1);
						
						nouR += matriu[filaMatriu][columnaMatriu] * c.getRed();
						nouG += matriu[filaMatriu][columnaMatriu] * c.getGreen();
						nouB += matriu[filaMatriu][columnaMatriu] * c.getBlue();
					}
				}
				
				setColorPixel(this.matriuBordes, this.nCanalsImgFons, columnaFons, filaFons, corregirColor(nouR, nouG, nouB));
			}
		}
	}
	
	private boolean esBordeAquestPixel(int fila, int columna) {
		Color c = getColorPixel(this.matriuBordes, this.nCanalsImgFons, columna, fila);
		return ((c.getBlue() + c.getGreen() + c.getRed() > 50) ? true : false);
	}
	
	private void generarXispes(boolean inici) {
		if (inici && this.xispesABordes)
			detectarBordes();
		
		int filaInici, filaFi;
		if (this.xispesABordes) {
			filaInici = 1;
			filaFi = this.getHeight() - 1;
		} else {
			filaInici = this.getHeight() - 1;
			filaFi = this.getHeight();
		}
		
		for (int fila = filaInici; fila < filaFi; fila++) {
			recorrerFila(fila, inici);
		}
	}
	
	private Color getColorPixel(byte[] ba, int numCanalsImatge, int x, int y) {
		int i = passarXYAIndexArray(x, y, numCanalsImatge);
		
		return new Color(Byte.toUnsignedInt(ba[i + 2]), Byte.toUnsignedInt(ba[i + 1]), Byte.toUnsignedInt(ba[i]));
	}
	
	private void inicialitzarArrayBytes() {
		for (int i = 0; i < this.arrayBytesFoc.length; i++) {
			this.arrayBytesFoc[i] = 0;
		}
	}
	
	private void inicialitzarMatriuT() {
		this.matriuTemperatures = new int[this.getHeight()][this.getWidth()];
		
		for (int fila = 0; fila < this.getHeight(); fila++) {
			for (int columna = 0; columna < this.getWidth(); columna++) {
				this.matriuTemperatures[fila][columna] = 0;
			}
		}
	}
	
	private int passarXYAIndexArray(int x, int y, int nCanals) {
		return nCanals * (y * this.getWidth() + x) + nCanals - 3; // this.numCanals - 3 = offset per si hi ha canal alfa
	}
	
	private void recorrerFila(int fila, boolean inici) {
		boolean generarXispaAqui;
		
		if (this.xispesABordes) generarXispaAqui = false;
		else generarXispaAqui = true;
		
		for (int columna = 1; columna < this.getWidth() - 1; columna++) {
			if (this.xispesABordes)
				generarXispaAqui = esBordeAquestPixel(fila, columna);
			
			if (generarXispaAqui) {
				if (inici || !costatsEncesos(fila, columna))
					this.matriuTemperatures[fila][columna] = ((int) (100 * Math.random()) == 0 ? 255 : 0);
				else
					this.matriuTemperatures[fila][columna] = ((int) (10 * Math.random()) != 0 ? 255 : 0);
				
				if (this.xispesABordes) generarXispaAqui = false;
			}
		}
	}
	
	private void setColorPixel(byte[] ba, int numCanalsImg, int x, int y, Color c) {
		int i = passarXYAIndexArray(x, y, numCanalsImg);
		
		ba[i] = (byte) c.getBlue();
		ba[i + 1] = (byte) c.getGreen();
		ba[i + 2] = (byte) c.getRed();
		
		if (numCanalsImg == 4) {
			ba[i - 1] = (byte) c.getAlpha();
		}
	}

}
