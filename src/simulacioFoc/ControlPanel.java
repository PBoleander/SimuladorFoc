package simulacioFoc;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class ControlPanel extends JPanel implements MouseListener {
	
	private static final long serialVersionUID = 1L;
	
	private JButton btnXispesBordes, btnXispesLiniaInferior, btnPausa, btnImg1, btnImg2;
	private JSlider jsAlturaFoc;
	private Viewer v;

	public void mouseClicked(MouseEvent e) {
		if (e.getSource().equals(btnXispesBordes)) {
			this.v.getFoc().setXispesABordes(true);
			canviaPausa(false);
		} else if (e.getSource().equals(btnXispesLiniaInferior)) {
			this.v.getFoc().setXispesABordes(false);
			canviaPausa(false);
		} else if (e.getSource().equals(btnPausa)) {
			canviaPausa(!this.v.getPausa());
		} else if (e.getSource().equals(btnImg1)) {
			if (btnImg1.isEnabled()) {
				try {
					this.v.setImatgeFons(ImageIO.read(new File("/home/oleander/Documentos/FP/Segundo/Empresa/Pasefesa/logo.png")));
					btnImg1.setEnabled(false);
					btnImg2.setEnabled(true);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} else if (e.getSource().equals(btnImg2)) {
			if (btnImg2.isEnabled()) {
				try {
					this.v.setImatgeFons(ImageIO.read(new File("/home/oleander/Imágenes/ImatgePerFocJava.png")));
					btnImg1.setEnabled(true);
					btnImg2.setEnabled(false);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	public void mouseReleased(MouseEvent e) {
		if (e.getSource().equals(jsAlturaFoc)) {
			double factorAlturaFoc = 7.485 + 0.0008 * (100 - jsAlturaFoc.getValue());
			this.v.getFoc().setFactorAlturaFoc(factorAlturaFoc);
		}
	}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	
	public ControlPanel(Viewer v) {
		super(new GridBagLayout());
		this.v = v;
		
		GridBagConstraints b = new GridBagConstraints();
		b.fill = GridBagConstraints.HORIZONTAL;
		
		this.btnXispesBordes = afegirBotoNou(btnXispesBordes, "Generar xispes als bordes", 0, 0, b);
		this.btnXispesLiniaInferior = afegirBotoNou(this.btnXispesLiniaInferior, "Generar xispes al costat inferior", 0, 1, b);
		this.btnPausa = afegirBotoNou(this.btnPausa, "Pausar animació", 0, 2, b);
		this.btnImg1 = afegirBotoNou(this.btnImg1, "Imatge 1", 0, 3, b);
		this.btnImg1.setEnabled(false);
		this.btnImg2 = afegirBotoNou(this.btnImg2, "Imatge 2", 0, 4, b);
		afegirLabelNou("Altura foc", 0, 5, new GridBagConstraints());
		this.jsAlturaFoc = afegirSliderNou(this.jsAlturaFoc, 0, 100, 50, 0, 6, 10, new GridBagConstraints());
		
	}
	
	private JButton afegirBotoNou(JButton boto, String titol, int x, int y, GridBagConstraints b) {
		boto = new JButton(titol);
		boto.addMouseListener(this);
		b.gridx = x;
		b.gridy = y;
		this.add(boto, b);
		
		return boto;
	}
	
	private void afegirLabelNou(String titol, int x, int y, GridBagConstraints l) {
		JLabel label = new JLabel(titol);
		l.gridx = x;
		l.gridy = y;
		this.add(label, l);
	}
	
	private JSlider afegirSliderNou(JSlider slider, int valorMinim, int valorMaxim, int valorInicial, int x, int y, int espaiTicks, GridBagConstraints s) {
		slider = new JSlider(valorMinim, valorMaxim, valorInicial);
		slider.addMouseListener(this);
		textSliders(slider, espaiTicks);
		s.gridx = x;
		s.gridy = y;
		s.weightx = 1.0;
		s.gridwidth = GridBagConstraints.REMAINDER;
		s.fill = GridBagConstraints.HORIZONTAL;
		this.add(slider, s);
		
		return slider;
	}
	
	private void canviaPausa(boolean pausa) {
		this.v.setPausa(pausa);
		this.btnPausa.setText((pausa ? "Reproduir" : "Pausar") + " animació");
		if (!pausa) this.v.paint(this.v.getGraphics());
	}
	
	private void textSliders(JSlider s, int espaiTicks) {
		Font f = new Font(Font.MONOSPACED, Font.PLAIN, 12);
		s.setMajorTickSpacing(espaiTicks);
		s.setFont(f);
		s.setPaintTicks(true);
		s.setPaintLabels(true);
	}

}
