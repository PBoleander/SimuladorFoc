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
	
	private Color corregirColor(int r, int g, int b, int a) {
		r = (r > 255 ? 255 : r);
		g = (g > 255 ? 255 : g);
		b = (b > 255 ? 255 : b);
		a = (a > 255 ? 255 : a);
		
		r = (r < 0 ? 0 : r);
		g = (g < 0 ? 0 : g);
		b = (b < 0 ? 0 : b);
		a = (a < 0 ? 0 : a);
		
		return new Color(r, g, b, a);
	}
	
	private void inicialitzarPaleta(Color c0, Color c85, Color c170, Color c255) {
		this.paleta = new Color[256];
		for (int i = 0; i < this.paleta.length; i++) {
			this.paleta[i] = new Color(0, 0, 0, 0);
		}
		this.paleta[0] = c0;
		this.paleta[85] = c85;
		this.paleta[170] = c170;
		this.paleta[255] = c255;
	}
	
	private void rellenarPaleta() {
		double deltaR, deltaG, deltaB, deltaA;
		deltaR = deltaG = deltaB = deltaA = 0;
		
		for (int i = 1; i < this.paleta.length - 1; i++) {
			switch (i) {
			case 1:
				deltaR = (this.paleta[ 85].getRed()   - this.paleta[ 0 ].getRed()  ) / 85.;
				deltaG = (this.paleta[ 85].getGreen() - this.paleta[ 0 ].getGreen()) / 85.;
				deltaB = (this.paleta[ 85].getBlue()  - this.paleta[ 0 ].getBlue() ) / 85.;
				deltaA = (this.paleta[ 85].getAlpha() - this.paleta[ 0 ].getAlpha()) / 85.;
				break;
			case 86:
				deltaR = (this.paleta[170].getRed()   - this.paleta[ 85].getRed()  ) / 85.;
				deltaG = (this.paleta[170].getGreen() - this.paleta[ 85].getGreen()) / 85.;
				deltaB = (this.paleta[170].getBlue()  - this.paleta[ 85].getBlue() ) / 85.;
				deltaA = (this.paleta[170].getAlpha() - this.paleta[ 85].getAlpha()) / 85.;
				break;
			case 171:
				deltaR = (this.paleta[255].getRed()   - this.paleta[170].getRed()  ) / 85.;
				deltaG = (this.paleta[255].getGreen() - this.paleta[170].getGreen()) / 85.;
				deltaB = (this.paleta[255].getBlue()  - this.paleta[170].getBlue() ) / 85.;
				deltaA = (this.paleta[255].getAlpha() - this.paleta[170].getAlpha()) / 85.;
				break;
			}
			
			int nouR = (int) (this.paleta[i / 85 * 85].getRed()   + (i % 85) * deltaR);
			int nouG = (int) (this.paleta[i / 85 * 85].getGreen() + (i % 85) * deltaG);
			int nouB = (int) (this.paleta[i / 85 * 85].getBlue()  + (i % 85) * deltaB);
			int nouA = (int) (this.paleta[i / 85 * 85].getAlpha() + (i % 85) * deltaA);
			
			this.paleta[i] = corregirColor(nouR, nouG, nouB, nouA);
		}
	}

}
