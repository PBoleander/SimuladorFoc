package simulacioFoc;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

public class ControlPanel extends JPanel implements MouseListener, KeyListener {
	
	private static final long serialVersionUID = 1L;
	
	private JButton btnXispesBordes, btnXispesLiniaInferior, btnPausa;
	private JTextField rutaImg;
	private JLabel mostraError;
	private JSlider jsAlturaFoc, jsDireccioVent, jsSensibilitatBordes;
	private Viewer viewer;

	public void mouseClicked(MouseEvent e) {
		if (e.getSource().equals(btnXispesBordes)) {
			this.viewer.getFoc().setXispesABordes(true);
			canviaPausa(false);
		} else if (e.getSource().equals(btnXispesLiniaInferior)) {
			this.viewer.getFoc().setXispesABordes(false);
			canviaPausa(false);
		} else if (e.getSource().equals(btnPausa)) {
			canviaPausa(!this.viewer.getPausa());
		}
	}
	public void mouseReleased(MouseEvent e) {
		if (e.getSource().equals(jsAlturaFoc)) {
			calcularAlturaFoc();
		} else if (e.getSource().equals(jsDireccioVent)) {
			this.viewer.getFoc().setVent(jsDireccioVent.getValue());
		} else if (e.getSource().equals(jsSensibilitatBordes)) {
			this.viewer.getFoc().setSensibilitatBordes(jsSensibilitatBordes.getValue());
		}
	}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			Image img;
			try {
				img = ImageIO.read(new File(rutaImg.getText()));
				if (img == null) {
					this.mostraError.setText("Aquest arxiu no és una imatge");
				} else {
					this.viewer.setImatgeFons(img);
					this.mostraError.setText(null);
					calcularAlturaFoc();
					this.viewer.getFoc().setVent(jsDireccioVent.getValue());
					this.viewer.getFoc().setSensibilitatBordes(jsSensibilitatBordes.getValue());
				}
			} catch (IOException e1) {
				mostraError.setText("Imatge no trobada");
			}
		}
	}
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	
	public ControlPanel(Viewer v) {
		super(new GridBagLayout());
		this.viewer = v;
		
		GridBagConstraints b = new GridBagConstraints();
		b.fill = GridBagConstraints.HORIZONTAL;
		
		this.rutaImg = afegirTextFieldNou(rutaImg, "Ruta:", 0, new GridBagConstraints());
		this.rutaImg.addKeyListener(this);
		this.mostraError = afegirLabelNou(mostraError, "", 0, 1, new GridBagConstraints());
		this.btnXispesBordes = afegirBotoNou(btnXispesBordes, "Generar xispes als bordes", 0, 2, b);
		this.btnXispesLiniaInferior = afegirBotoNou(this.btnXispesLiniaInferior, "Generar xispes al costat inferior", 0, 3, b);
		this.btnPausa = afegirBotoNou(this.btnPausa, "Pausar animació", 0, 4, b);
		afegirLabelNou("Altura foc", 0, 5, new GridBagConstraints());
		this.jsAlturaFoc = afegirSliderNou(this.jsAlturaFoc, 0, 100, 50, 0, 6, 10, new GridBagConstraints());
		afegirLabelNou("Direcció vent", 0, 7, new GridBagConstraints());
		this.jsDireccioVent = afegirSliderNou(this.jsDireccioVent, -1, 1, 0, 0, 8, 1, new GridBagConstraints());
		afegirLabelNou("Sensibilitat detecció bordes", 0, 9, new GridBagConstraints());
		this.jsSensibilitatBordes = afegirSliderNou(this.jsSensibilitatBordes, 0, 700, 700, 0, 10, 100, new GridBagConstraints());
		
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
		this.add(field, t);
		
		return field;
	}
	
	private void calcularAlturaFoc() {
		double factorAlturaFoc = 7.485 + 0.0008 * (100 - jsAlturaFoc.getValue());
		this.viewer.getFoc().setFactorAlturaFoc(factorAlturaFoc);
	}
	
	private void canviaPausa(boolean pausa) {
		this.viewer.setPausa(pausa);
		this.btnPausa.setText((pausa ? "Reproduir" : "Pausar") + " animació");
		if (!pausa) this.viewer.repaint();
	}
	
	private void textSliders(JSlider s, int espaiTicks) {
		Font f = new Font(Font.MONOSPACED, Font.PLAIN, 12);
		s.setMajorTickSpacing(espaiTicks);
		s.setFont(f);
		s.setPaintTicks(true);
		s.setPaintLabels(true);
	}

}
