package simulacioFoc;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

public class ControlPanel extends JPanel implements MouseListener {
	
	private static final long serialVersionUID = 1L;
	
	private JButton btnXispesBordes, btnXispesLiniaInferior, btnPausa, btnCarregaImg;
	private JTextField rutaImg;
	private JLabel mostraError;
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
		} else if (e.getSource().equals(btnCarregaImg)) {
			Image img;
			try {
				img = ImageIO.read(new File(rutaImg.getText()));
				if (img == null) {
					this.mostraError.setText("Aquest arxiu no és una imatge");
				} else {
					this.v.setImatgeFons(img);
					this.mostraError.setText(null);
				}
			} catch (IOException e1) {
				mostraError.setText("Imatge no trobada");
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
		
		this.rutaImg = afegirTextFieldNou(rutaImg, "Ruta:", 0, new GridBagConstraints());
		this.btnCarregaImg = afegirBotoNou(btnCarregaImg, "Carrega", 2, 0, b);
		this.mostraError = afegirLabelNou(mostraError, "", 0, 1, new GridBagConstraints());
		this.btnXispesBordes = afegirBotoNou(btnXispesBordes, "Generar xispes als bordes", 0, 2, b);
		this.btnXispesLiniaInferior = afegirBotoNou(this.btnXispesLiniaInferior, "Generar xispes al costat inferior", 0, 3, b);
		this.btnPausa = afegirBotoNou(this.btnPausa, "Pausar animació", 0, 4, b);
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
		l.gridwidth = GridBagConstraints.REMAINDER; // Això no hauria d'anar aquí si volguessim que fos reutilitzable però ara és el més còmode
		this.add(label, l);
	}
	
	private JLabel afegirLabelNou(JLabel label, String titol, int x, int y, GridBagConstraints l) {
		label = new JLabel(titol);
		l.gridx = x;
		l.gridy = y;
		this.add(label, l);
		
		return label;
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
	
	private JTextField afegirTextFieldNou(JTextField field, String titol, int y, GridBagConstraints t) {
		t.gridy = y;
		t.insets = new Insets(0, -210, 0, 0);
		JLabel l = new JLabel(titol);
		this.add(l, t);
		
		field = new JTextField();
		t.fill = GridBagConstraints.HORIZONTAL;
		t.gridx = 1;
		//t.weightx = 1.0;
		this.add(field, t);
		
		return field;
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
