package simulacioFoc;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ControlPanel extends JPanel implements MouseListener, ItemListener, ChangeListener {
	
	private static final long serialVersionUID = 1L;
	
	private Choice choiceTriaTipusBorde;
	private JButton btnXispesBordes, btnXispesLiniaInferior, btnPausa, btnTriaImg;
	private JButton btnTriaColor0, btnTriaColor2, btnTriaColor3, btnTriaColor255;
	final private JFileChooser fcTriadorImg = new JFileChooser();
	private JLabel lMostraError;
	private JRadioButton rbAmpliarRes, rbAmpliarFons, rbAmpliarConvolucio, rbAmpliarFoc;
	private JSlider jsAlturaFoc, jsDireccioVent, jsSensibilitatBordes, jsVelocitatFoc;
	private JSpinner selectorT2, selectorT3;
	private Viewer viewer;

	@Override
	public void mouseClicked(MouseEvent e) { // radio buttons
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
			setTotsRadiosASelected(false);
			((JRadioButton) e.getSource()).setSelected(true);
			this.viewer.repaint();
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) { // sliders
		if (e.getSource() instanceof JSlider && ((JSlider) e.getSource()).isEnabled()) {
			if (e.getSource().equals(jsAlturaFoc)) {
				this.viewer.getFoc().setFactorAltura(calcularAlturaFoc());
			} else if (e.getSource().equals(jsDireccioVent)) {
				this.viewer.getFoc().setVent(jsDireccioVent.getValue());
			} else if (e.getSource().equals(jsSensibilitatBordes)) {
				this.viewer.getFoc().setSensibilitatBordes(jsSensibilitatBordes.getValue());
				this.viewer.setPintarImatgesFixes(true);
				this.viewer.repaint();
			} else if (e.getSource().equals(jsVelocitatFoc)) {
				this.viewer.setTempsEspera(jsVelocitatFoc.getValue());
			}
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) { // botons
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
				int valorRetornat = fcTriadorImg.showDialog(null, "Obrir");
				if (valorRetornat == JFileChooser.APPROVE_OPTION) {
					try {
						Image img = ImageIO.read(fcTriadorImg.getSelectedFile());
						if (img == null) {
							this.lMostraError.setText("Aquest arxiu no és una imatge");
						} else {
							this.viewer.setImatgeFons(img);
							this.lMostraError.setText(null);
							activarTotsElsObjectes();
							this.viewer.getFoc().setFactorAltura(calcularAlturaFoc());
							this.viewer.getFoc().setVent(jsDireccioVent.getValue());
							this.viewer.getFoc().setSensibilitatBordes(jsSensibilitatBordes.getValue());
						}
					} catch (IOException e1) {
						lMostraError.setText("Imatge no trobada");
					}
				}
			} else if (e.getSource().equals(btnTriaColor0)) {
				Color c0 = JColorChooser.showDialog(null, "Color de T = 0", this.viewer.getFoc().getColor0(), true);
				if (c0 != null) this.viewer.getFoc().setColor0(c0);
			} else if (e.getSource().equals(btnTriaColor2)) {
				Color c2 = JColorChooser.showDialog(null, "Color de T2", this.viewer.getFoc().getColorT2(), true);
				if (c2 != null) this.viewer.getFoc().setColorT2(c2);
			} else if (e.getSource().equals(btnTriaColor3)) {
				Color c3 = JColorChooser.showDialog(null, "Color de T3", this.viewer.getFoc().getColorT3(), true);
				if (c3 != null) this.viewer.getFoc().setColorT3(c3);
			} else if (e.getSource().equals(btnTriaColor255)) {
				Color c255 = JColorChooser.showDialog(null, "Color de T = 255", this.viewer.getFoc().getColor255(), true);
				if (c255 != null) this.viewer.getFoc().setColor255(c255);
			}
		}
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) { // choice
		this.viewer.getFoc().setTipusBorde(this.choiceTriaTipusBorde.getSelectedIndex());
		this.viewer.setPintarImatgesFixes(true);
		this.viewer.repaint();
	}
	
	@Override
	public void stateChanged(ChangeEvent e) { // spinner
		if ((int) selectorT2.getValue() != (int) selectorT3.getValue()) {
			this.lMostraError.setText(null);
			if (e.getSource().equals(selectorT2)) {
				this.viewer.getFoc().setT2((int) selectorT2.getValue());
			} else if (e.getSource().equals(selectorT3)) {
				this.viewer.getFoc().setT3((int) selectorT3.getValue());
			}
		} else {
			this.lMostraError.setText("Valor de temperatura invàlid");
		}
	}
	
	public ControlPanel(Viewer v) {
		super(new GridBagLayout());
		this.viewer = v;
		
		GridBagConstraints b = new GridBagConstraints();
		b.fill = GridBagConstraints.HORIZONTAL;
		
		this.lMostraError = afegirLabelNou(lMostraError, "", 0, 0, new GridBagConstraints());
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
		
		GridBagConstraints l = new GridBagConstraints();
		l.anchor = GridBagConstraints.WEST;
		afegirLabelNou("Borde de convolució", 0, 14, l);
		this.choiceTriaTipusBorde = afegirChoiceBordes(this.choiceTriaTipusBorde, 1, 14, false, new GridBagConstraints());
		
		afegirLabelNou("Paleta de colors", 0, 15, new GridBagConstraints());
		afegirLabelNou("Temp = 0", 0, 16, l);
		this.btnTriaColor0 = afegirBotoNou(this.btnTriaColor0, "Tria color", 1, 16, false, b);
		afegirLabelNou("Temp =", 0, 17, l);
		this.selectorT2 = afegirSpinnerNou(this.selectorT2, 85, 1, 254, 1, 0, 17, new GridBagConstraints());
		this.btnTriaColor2 = afegirBotoNou(this.btnTriaColor2, "Tria color", 1, 17, false, b);
		afegirLabelNou("Temp =", 0, 18, l);
		this.selectorT3 = afegirSpinnerNou(this.selectorT3, 170, 1, 254, 1, 0, 18, new GridBagConstraints());
		this.btnTriaColor3 = afegirBotoNou(this.btnTriaColor3, "Tria color", 1, 18, false, b);
		afegirLabelNou("Temp = 255", 0, 19, l);
		this.btnTriaColor255 = afegirBotoNou(this.btnTriaColor255, "Tria color", 1, 19, false, b);
		
		afegirLabelNou("Velocitat foc", 0, 20, new GridBagConstraints());
		this.jsVelocitatFoc = afegirSliderNou(this.jsVelocitatFoc, 0, 200, 10, 0, 21, 20, true, new GridBagConstraints());
	}
	
	private void activarTotsElsObjectes() {
		this.btnPausa.setEnabled(true);
		this.btnXispesBordes.setEnabled(true);
		this.btnXispesLiniaInferior.setEnabled(true);
		this.jsAlturaFoc.setEnabled(true);
		this.jsDireccioVent.setEnabled(true);
		this.jsSensibilitatBordes.setEnabled(true);
		this.choiceTriaTipusBorde.setEnabled(true);
		this.btnTriaColor0.setEnabled(true);
		this.btnTriaColor2.setEnabled(true);
		this.btnTriaColor3.setEnabled(true);
		this.btnTriaColor255.setEnabled(true);
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
	
	private Choice afegirChoiceBordes(Choice choice, int x, int y, boolean enabled, GridBagConstraints c) {
		choice = new Choice();
		choice.add("Ambdós");
		choice.add("Horizontal");
		choice.add("Vertical");
		choice.select(0);
		choice.setEnabled(enabled);
		
		choice.addItemListener(this);
		
		c.gridx = x;
		c.gridy = y;
		
		this.add(choice, c);
		return choice;
	}
	
	private void afegirLabelNou(String titol, int x, int y, GridBagConstraints l) {
		JLabel label = new JLabel(titol);
		l.gridx = x;
		l.gridy = y;
		l.gridwidth = GridBagConstraints.REMAINDER;
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
		radio = new JRadioButton(titol, selected);
		radio.addMouseListener(this);
		
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
	
	private JSpinner afegirSpinnerNou(JSpinner spinner, int valorInicial, int valorMin, int valorMax, int pas, int x, int y, GridBagConstraints s) {
		SpinnerNumberModel model = new SpinnerNumberModel(valorInicial, valorMin, valorMax, pas);
		spinner = new JSpinner(model);
		spinner.addChangeListener(this);
		s.gridx = x;
		s.gridy = y;
		s.insets = new Insets(0, 20, 0, 0);
		
		this.add(spinner, s);
		return spinner;
	}
	
	private double calcularAlturaFoc() {
		return 7.485 + 0.0008 * (100 - jsAlturaFoc.getValue());
	}
	
	private void canviaPausa(boolean pausa) {
		this.viewer.setPausa(pausa);
		this.btnPausa.setText(pausa ? "Reproduir" : "Pausar");
		if (!pausa) this.viewer.repaint();
	}
	
	private void setTotsRadiosASelected(boolean selected) {
		this.rbAmpliarConvolucio.setSelected(selected);
		this.rbAmpliarFoc.setSelected(selected);
		this.rbAmpliarFons.setSelected(selected);
		this.rbAmpliarRes.setSelected(selected);
	}
	
	private void textSliders(JSlider s, int espaiTicks) {
		Font f = new Font(Font.MONOSPACED, Font.PLAIN, 12);
		s.setMajorTickSpacing(espaiTicks);
		s.setFont(f);
		s.setPaintTicks(true);
		s.setPaintLabels(true);
	}
}
