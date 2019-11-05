package simulacioFoc;

import java.awt.Color;

public class PaletaColors {
	
	private Color[] paleta;
	
	public PaletaColors(Color c0, Color c85, Color c170, Color c255) {
		inicialitzarPaleta(c0, c85, c170, c255);
		rellenarPaleta();
	}
	
	public Color[] getPaleta() {
		return this.paleta;
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
	
	private void inicialitzarPaleta(Color c0, Color c85, Color c170, Color c255) {
		this.paleta = new Color[256];
		for (int i = 0; i < this.paleta.length; i++) {
			this.paleta[i] = new Color(0, 0, 0);
		}
		this.paleta[0] = c0;
		this.paleta[85] = c85;
		this.paleta[170] = c170;
		this.paleta[255] = c255;
	}
	
	private void rellenarPaleta() {
		int deltaR, deltaG, deltaB;
		deltaR = deltaG = deltaB = 0;
		
		for (int i = 1; i < this.paleta.length - 1; i++) {
			switch (i) {
			case 1:
				deltaR = (int) ((this.paleta[85].getRed() - this.paleta[0].getRed()) / 85.);
				deltaG = (int) ((this.paleta[85].getGreen() - this.paleta[0].getGreen()) / 85.);
				deltaB = (int) ((this.paleta[85].getBlue() - this.paleta[0].getBlue()) / 85.);
				break;
			case 86:
				deltaR = (int) ((this.paleta[170].getRed() - this.paleta[85].getRed()) / 85.);
				deltaG = (int) ((this.paleta[170].getGreen() - this.paleta[85].getGreen()) / 85.);
				deltaB = (int) ((this.paleta[170].getBlue() - this.paleta[85].getBlue()) / 85.);
				break;
			case 171:
				deltaR = (int) ((this.paleta[255].getRed() - this.paleta[170].getRed()) / 85.);
				deltaG = (int) ((this.paleta[255].getGreen() - this.paleta[170].getGreen()) / 85.);
				deltaB = (int) ((this.paleta[255].getBlue() - this.paleta[170].getBlue()) / 85.);
				break;
			}
			
			if (i != 85 && i != 170) {
				int nouR = this.paleta[i].getRed() + deltaR;
				int nouG = this.paleta[i].getGreen() + deltaG;
				int nouB = this.paleta[i].getBlue() + deltaB;
				
				this.paleta[i] = corregirColor(nouR, nouG, nouB);
			}
		}
	}

}
