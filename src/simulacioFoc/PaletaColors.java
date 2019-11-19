package simulacioFoc;

import java.awt.Color;

public class PaletaColors {
	
	private Color[] paleta;
	private int temp2, temp3;
	private Color c2, c3;
	
	public PaletaColors(Color c1, int temp2, Color c2, int temp3, Color c3, Color c4) {
		ordenar(temp2, c2, temp3, c3);
		inicialitzarPaleta(0, c1, this.temp2, this.c2, this.temp3, this.c3, 255, c4);
		rellenarPaleta(0, this.temp2, this.temp3, 255);
	}
	
	public Color getColor(int index) {
		return paleta[index];
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
	
	private void inicialitzarPaleta(int temp1, Color c1, int temp2, Color c2, int temp3, Color c3, int temp4, Color c4) {
		this.paleta = new Color[256];
		for (int i = 0; i < this.paleta.length; i++) {
			this.paleta[i] = new Color(0, 0, 0, 0);
		}
		this.paleta[temp1] = c1;
		this.paleta[temp2] = c2;
		this.paleta[temp3] = c3;
		this.paleta[temp4] = c4;
	}
	
	private void ordenar(int t1, Color c1, int t2, Color c2) {
		int tBackup;
		Color cBackup;
		
		if (t1 > t2) {
			tBackup = t2;
			cBackup = c2;
			t2 = t1;
			c2 = c1;
			t1 = tBackup;
			c1 = cBackup;
		}
		
		this.c2 = c1;
		this.c3 = c2;
		this.temp2 = t1;
		this.temp3 = t2;
	}
	
	private void rellenarPaleta(int temp1, int temp2, int temp3, int temp4) {
		double deltaR, deltaG, deltaB, deltaA;
		deltaR = deltaG = deltaB = deltaA = 0;
		int diferenciaTemperatures = 1;
		int iDePartida = 0;
		
		for (int i = 1; i < this.paleta.length - 1; i++) {
			if (i == temp1 + 1) {
				diferenciaTemperatures = temp2 - temp1;
				iDePartida = temp1;
				deltaR = (this.paleta[temp2].getRed()   - this.paleta[temp1].getRed()  ) / (double) diferenciaTemperatures;
				deltaG = (this.paleta[temp2].getGreen() - this.paleta[temp1].getGreen()) / (double) diferenciaTemperatures;
				deltaB = (this.paleta[temp2].getBlue()  - this.paleta[temp1].getBlue() ) / (double) diferenciaTemperatures;
				deltaA = (this.paleta[temp2].getAlpha() - this.paleta[temp1].getAlpha()) / (double) diferenciaTemperatures;
			} else if (i == temp2 + 1) {
				diferenciaTemperatures = temp3 - temp2;
				iDePartida = temp2;
				deltaR = (this.paleta[temp3].getRed()   - this.paleta[temp2].getRed()  ) / (double) diferenciaTemperatures;
				deltaG = (this.paleta[temp3].getGreen() - this.paleta[temp2].getGreen()) / (double) diferenciaTemperatures;
				deltaB = (this.paleta[temp3].getBlue()  - this.paleta[temp2].getBlue() ) / (double) diferenciaTemperatures;
				deltaA = (this.paleta[temp3].getAlpha() - this.paleta[temp2].getAlpha()) / (double) diferenciaTemperatures;
			} else if (i == temp3 + 1) {
				diferenciaTemperatures = temp4 - temp3;
				iDePartida = temp3;
				deltaR = (this.paleta[temp4].getRed()   - this.paleta[temp3].getRed()  ) / (double) diferenciaTemperatures;
				deltaG = (this.paleta[temp4].getGreen() - this.paleta[temp3].getGreen()) / (double) diferenciaTemperatures;
				deltaB = (this.paleta[temp4].getBlue()  - this.paleta[temp3].getBlue() ) / (double) diferenciaTemperatures;
				deltaA = (this.paleta[temp4].getAlpha() - this.paleta[temp3].getAlpha()) / (double) diferenciaTemperatures;
			}
			
			int nouR = (int) (this.paleta[iDePartida].getRed()   + (i - iDePartida) * deltaR);
			int nouG = (int) (this.paleta[iDePartida].getGreen() + (i - iDePartida) * deltaG);
			int nouB = (int) (this.paleta[iDePartida].getBlue()  + (i - iDePartida) * deltaB);
			int nouA = (int) (this.paleta[iDePartida].getAlpha() + (i - iDePartida) * deltaA);
			
			this.paleta[i] = corregirColor(nouR, nouG, nouB, nouA);
		}
	}

}
