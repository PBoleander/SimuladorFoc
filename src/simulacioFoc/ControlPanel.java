package simulacioFoc;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;

public class ControlPanel extends JPanel implements MouseListener {
	
	private static final long serialVersionUID = 1L;
	
	private JButton btnXispesBordes, btnXispesLiniaInferior, btnPausa, btnTriaImg;
	final private JFileChooser triadorImg = new JFileChooser();
	private JLabel mostraError;
	private JSlider jsAlturaFoc, jsDireccioVent, jsSensibilitatBordes;
	private JRadioButton rbAmpliarRes, rbAmpliarFons, rbAmpliarConvolucio, rbAmpliarFoc;
	private Viewer viewer;

	public void mouseClicked(MouseEvent e) {
		if (e.getSource().equals(rbAmpliarConvolucio)) {
			this.viewer.setAmpliacio(2);
		} else if (e.getSource().equals(rbAmpliarFoc)) {
			this.viewer.setAmpliacio(3);
		} else if (e.getSource().equals(rbAmpliarFons)) {
			this.viewer.setAmpliacio(1);
		} else if (e.getSource().equals(rbAmpliarRes)) {
			this.viewer.setAmpliacio(0);
		}
		if (e.getSource() instanceof JRadioButton) {
			setTotsSelectedRadios(false);
			((JRadioButton) e.getSource()).setSelected(true);
			this.viewer.repaint();
		}
	}
	public void mouseReleased(MouseEvent e) {
		if (e.getSource() instanceof JSlider && ((JSlider) e.getSource()).isEnabled()) {
			if (e.getSource().equals(jsAlturaFoc)) {
				this.viewer.getFoc().setFactorAltura(calcularAlturaFoc());
			} else if (e.getSource().equals(jsDireccioVent)) {
				this.viewer.getFoc().setVent(jsDireccioVent.getValue());
			} else if (e.getSource().equals(jsSensibilitatBordes)) {
				this.viewer.getFoc().setSensibilitatBordes(jsSensibilitatBordes.getValue());
			}
		}
	}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {
		if (e.getSource() instanceof JButton && ((JButton) e.getSource()).isEnabled()) {
			if (e.getSource().equals(btnXispesBordes)) {
				this.viewer.getFoc().setXispesABordes(true);
				canviaPausa(false);
			} else if (e.getSource().equals(btnXispesLiniaInferior)) {
				this.viewer.getFoc().setXispesABordes(false);
				canviaPausa(false);
			} else if (e.getSource().equals(btnPausa)) {
				canviaPausa(!this.viewer.getPausa());
			} else if (e.getSource().equals(btnTriaImg)) {
				int valorRetornat = triadorImg.showDialog(null, "Obrir");
				if (valorRetornat == JFileChooser.APPROVE_OPTION) {
					try {
						Image img = ImageIO.read(triadorImg.getSelectedFile());
						if (img == null) {
							this.mostraError.setText("Aquest arxiu no és una imatge");
						} else {
							this.viewer.setImatgeFons(img);
							this.mostraError.setText(null);
							activarTotsElsObjectes();
							this.viewer.getFoc().setFactorAltura(calcularAlturaFoc());
							this.viewer.getFoc().setVent(jsDireccioVent.getValue());
							this.viewer.getFoc().setSensibilitatBordes(jsSensibilitatBordes.getValue());
						}
					} catch (IOException e1) {
						mostraError.setText("Imatge no trobada");
					}
				}
			}
		}
	}
	
	public ControlPanel(Viewer v) {
		super(new GridBagLayout());
		this.viewer = v;
		
		GridBagConstraints b = new GridBagConstraints();
		b.fill = GridBagConstraints.HORIZONTAL;
		
		this.mostraError = afegirLabelNou(mostraError, "", 0, 0, new GridBagConstraints());
		this.btnTriaImg = afegirBotoNou(this.btnTriaImg, "Tria imatge", 0, 1, true, b);
		this.btnPausa = afegirBotoNou(this.btnPausa, "Pausar", 1, 1, false, b);
		this.btnXispesBordes = afegirBotoNou(btnXispesBordes, "Xispes als bordes", 0, 2, false, b);
		this.btnXispesLiniaInferior = afegirBotoNou(this.btnXispesLiniaInferior, "Xispes a base", 1, 2, false, b);
		afegirLabelNou("Altura foc", 0, 5, new GridBagConstraints());
		this.jsAlturaFoc = afegirSliderNou(this.jsAlturaFoc, 0, 100, 50, 0, 6, 10, false, new GridBagConstraints());
		afegirLabelNou("Direcció vent", 0, 7, new GridBagConstraints());
		this.jsDireccioVent = afegirSliderNou(this.jsDireccioVent, -1, 1, 0, 0, 8, 1, false, new GridBagConstraints());
		afegirLabelNou("Sensibilitat detecció bordes", 0, 9, new GridBagConstraints());
		this.jsSensibilitatBordes = afegirSliderNou(this.jsSensibilitatBordes, 0, 700, 700, 0, 10, 100, false, new GridBagConstraints());
		afegirLabelNou("Imatge a ampliar", 0, 11, new GridBagConstraints());
		this.rbAmpliarRes = afegirRadioNou(this.rbAmpliarRes, "Cap", 0, 12, true, new GridBagConstraints());
		this.rbAmpliarFons = afegirRadioNou(this.rbAmpliarFons, "Fons", 1, 12, false, new GridBagConstraints());
		this.rbAmpliarConvolucio = afegirRadioNou(this.rbAmpliarConvolucio, "Convolucionada", 0, 13, false, new GridBagConstraints());
		this.rbAmpliarFoc = afegirRadioNou(this.rbAmpliarFoc, "Resultat", 1, 13, false, new GridBagConstraints());
	}
	
	private void activarTotsElsObjectes() {
		this.btnPausa.setEnabled(true);
		this.btnXispesBordes.setEnabled(true);
		this.btnXispesLiniaInferior.setEnabled(true);
		this.jsAlturaFoc.setEnabled(true);
		this.jsDireccioVent.setEnabled(true);
		this.jsSensibilitatBordes.setEnabled(true);
	}
	
	private JButton afegirBotoNou(JButton boto, String titol, int x, int y, boolean enabled, GridBagConstraints b) {
		boto = new JButton(titol);
		boto.addMouseListener(this);
		boto.setEnabled(enabled);
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
	
	private JRadioButton afegirRadioNou(JRadioButton radio, String titol, int x, int y, boolean selected, GridBagConstraints r) {
		radio = new JRadioButton(titol);
		radio.addMouseListener(this);
		radio.setSelected(selected);
		
		r.gridx = x;
		r.gridy = y;
		r.anchor = GridBagConstraints.WEST;
		
		this.add(radio, r);
		return radio;
	}
	
	private JSlider afegirSliderNou(JSlider slider, int valorMinim, int valorMaxim, int valorInicial, int x, int y, int espaiTicks, boolean enabled, GridBagConstraints s) {
		slider = new JSlider(valorMinim, valorMaxim, valorInicial);
		slider.addMouseListener(this);
		slider.setEnabled(enabled);
		textSliders(slider, espaiTicks);
		s.gridx = x;
		s.gridy = y;
		s.weightx = 1.0;
		s.gridwidth = GridBagConstraints.REMAINDER;
		s.fill = GridBagConstraints.HORIZONTAL;
		this.add(slider, s);
		
		return slider;
	}
	
	private double calcularAlturaFoc() {
		return 7.485 + 0.0008 * (100 - jsAlturaFoc.getValue());
	}
	
	private void canviaPausa(boolean pausa) {
		this.viewer.setPausa(pausa);
		this.btnPausa.setText(pausa ? "Reproduir" : "Pausar");
		if (!pausa) this.viewer.repaint();
	}
	
	private void setTotsSelectedRadios(boolean selected) {
		this.rbAmpliarConvolucio.setSelected(false);
		this.rbAmpliarFoc.setSelected(false);
		this.rbAmpliarFons.setSelected(false);
		this.rbAmpliarRes.setSelected(false);
	}
	
	private void textSliders(JSlider s, int espaiTicks) {
		Font f = new Font(Font.MONOSPACED, Font.PLAIN, 12);
		s.setMajorTickSpacing(espaiTicks);
		s.setFont(f);
		s.setPaintTicks(true);
		s.setPaintLabels(true);
	}

}
