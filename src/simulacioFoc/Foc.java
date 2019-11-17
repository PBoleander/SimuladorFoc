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
	private byte[] matriuBordesImgFons;
	private Color[] paleta;
	private boolean xispesABordes;
	private double factorAltura;
	private int vent;
	private int sensibilitatBordes;
	private int tipusBorde;
	
	public Foc(int ample, int alt, int tipus, BufferedImage imgFons) {
		super(ample, alt, tipus);
		
		this.imatgeFons = imgFons;
		this.nCanalsImgFons = this.imatgeFons.getColorModel().hasAlpha() ? 4 : 3;
		this.arrayBytesImatgeFons = ((DataBufferByte) this.imatgeFons.getRaster().getDataBuffer()).getData();
		
		this.numCanals = (this.getColorModel().hasAlpha() ? 4 : 3);
		this.setData(Raster.createRaster(this.getSampleModel(), new DataBufferByte(ample * alt * this.numCanals), new Point()));
		this.arrayBytesFoc = ((DataBufferByte) this.getRaster().getDataBuffer()).getData();
		this.matriuBordesImgFons = new byte[this.arrayBytesImatgeFons.length];
		
		PaletaColors pc = new PaletaColors(new Color(0, 0, 0, 0),
										   new Color(150, 150, 150, 100),
										   new Color(235, 65, 12, 255),
										   new Color(255, 255, 255, 255));
		this.paleta = pc.getPaleta();
		
		this.xispesABordes = false;
		
		detectarBordes(this.tipusBorde);
		iniciar();
	}
	
	public void actualitzar(boolean generarXispes) {
		actualitzarMatriuT();
		colorejarImatge();
		if (generarXispes) generarXispes(false);
	}
	
	public byte[] getMatriuBordesImgFons() {
		return this.matriuBordesImgFons;
	}
	
	public void setFactorAltura(double factorAlturaFoc) {
		this.factorAltura = factorAlturaFoc;
	}
	
	public void setSensibilitatBordes(int sb) {
		this.sensibilitatBordes = sb;
	}
	public void setTipusBorde(int tipusBorde) {
		this.tipusBorde = tipusBorde;
		detectarBordes(tipusBorde);
	}
	public void setVent(int vent) {
		this.vent = vent;
	}
	
	public void setXispesABordes(boolean bordes) {
		this.xispesABordes = bordes;
		iniciar();
	}
	
	private void actualitzarMatriuT() {
		double[][] matriuCoeficients = matriuCoeficientsVent(this.vent);
		for (int fila = this.getHeight() - 2; fila >= 0; fila--) {
			for (int columna = 1; columna < this.getWidth() - 1; columna++) {
				if (!this.xispesABordes || (this.xispesABordes && !esBordeAquestPixel(fila, columna))) {
					this.matriuTemperatures[fila][columna] = 
							(int) ((this.matriuTemperatures[fila	][columna - 1] * matriuCoeficients[0][0] +
									this.matriuTemperatures[fila	][columna	 ] * matriuCoeficients[0][1] +
									this.matriuTemperatures[fila	][columna + 1] * matriuCoeficients[0][2] +
									this.matriuTemperatures[fila + 1][columna - 1] * matriuCoeficients[1][0] +
									this.matriuTemperatures[fila + 1][columna	 ] * matriuCoeficients[1][1] +
									this.matriuTemperatures[fila + 1][columna + 1] * matriuCoeficients[1][2]) / this.factorAltura);
					
					this.matriuTemperatures[fila][columna] = (this.matriuTemperatures[fila][columna] > 255 ? 255 : this.matriuTemperatures[fila][columna]);
				}
			}
		}
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
			return ((this.matriuTemperatures[fila - 1][columna - 1] > 0 || this.matriuTemperatures[fila - 1][columna	] > 0 ||
					 this.matriuTemperatures[fila - 1][columna + 1] > 0 || this.matriuTemperatures[fila	   ][columna - 1] > 0 ||
					 this.matriuTemperatures[fila	 ][columna + 1] > 0 || this.matriuTemperatures[fila + 1][columna - 1] > 0 ||
					 this.matriuTemperatures[fila + 1][columna	  ] > 0 || this.matriuTemperatures[fila + 1][columna + 1] > 0) ? true : false);
		else
			return ((this.matriuTemperatures[fila	 ][columna - 1] > 0 || this.matriuTemperatures[fila	   ][columna + 1] > 0 ||
				     this.matriuTemperatures[fila - 1][columna - 1] > 0 || this.matriuTemperatures[fila - 1][columna	] > 0 ||
				     this.matriuTemperatures[fila - 1][columna + 1] > 0) ? true : false);
	}
	
	private void detectarBordes(int tipusBordes) {
		int[][] matriu = matriuConvolucioBordes(tipusBordes);
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
				
				setColorPixel(this.matriuBordesImgFons, this.nCanalsImgFons, columnaFons, filaFons, corregirColor(nouR, nouG, nouB));
			}
		}
	}
	
	private boolean esBordeAquestPixel(int fila, int columna) {
		Color c = getColorPixel(this.matriuBordesImgFons, this.nCanalsImgFons, columna, fila);
		return ((c.getBlue() + c.getGreen() + c.getRed() >= 3 * 255 - this.sensibilitatBordes) ? true : false);
	}
	
	private void generarXispes(boolean inici) {
		int filaInici, filaFi;
		if (this.xispesABordes) {
			filaInici = 1;
			filaFi = this.getHeight() - 1;
		} else {
			filaInici = this.getHeight() - 1;
			filaFi = this.getHeight();
		}
		
		for (int fila = filaInici; fila < filaFi; fila++) {
			recorrerFilaEnBuscaDeXispes(fila, inici);
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
	
	private void iniciar() {
		inicialitzarMatriuT();
		inicialitzarArrayBytes();
		generarXispes(true);
	}
	
	private double[][] matriuCoeficientsVent(int vent) {
		switch (vent) {
		case -1:
			double[][] matriuMenos1 = {{0.0, 2.0, 2.5}, 
					 				   {0.0, 1.0, 2.0}};
			return matriuMenos1;
		case 0:
			double[][] matriu0 = {{1.25, 2.0, 1.25}, 
								  {0.75, 1.5, 0.75}};
			return matriu0;
		case 1:
			double[][] matriu1 = {{2.5, 2.0, 0.0}, 
	 				   			  {2.0, 1.0, 0.0}};
			return matriu1;
		default:
			return null;	
		}
	}
	
	private int[][] matriuConvolucioBordes(int tipusBordes) {
		switch (tipusBordes) {
		case 1:
			int[][] matriu1 = {{ 1,  2,  1},
							   { 0,  0,  0},
							   {-1, -2, -1}};
			return matriu1;
		case 2:
			int[][] matriu2 = {{-1, 0, 1},
							   {-2, 0, 2},
							   {-1, 0, 1}};
			return matriu2;
		default:
			int[][] matriu0 = {{0,  1, 0},
					  		   {1, -4, 1},
					  		   {0,  1, 0}};
			return matriu0;
		}
	}
	
	private int passarXYAIndexArray(int x, int y, int nCanals) {
		return nCanals * (y * this.getWidth() + x) + nCanals - 3; // this.numCanals - 3 = offset per si hi ha canal alfa
	}
	
	private void recorrerFilaEnBuscaDeXispes(int fila, boolean inici) {
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
					this.matriuTemperatures[fila][columna] = ((int) (20 * Math.random()) != 0 ? 255 : 0);
				
				if (this.xispesABordes) generarXispaAqui = false;
			}
		}
	}
	
	private void setColorPixel(byte[] ba, int numCanalsImg, int x, int y, Color c) {
		int i = passarXYAIndexArray(x, y, numCanalsImg);
		
		ba[i	] = (byte) c.getBlue();
		ba[i + 1] = (byte) c.getGreen();
		ba[i + 2] = (byte) c.getRed();
		
		if (numCanalsImg == 4) {
			ba[i - 1] = (byte) c.getAlpha();
		}
	}

}
