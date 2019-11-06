package simulacioFoc;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ControlPanel extends JPanel implements MouseListener {
	
	private static final long serialVersionUID = 1L;
	
	JButton btnXispesBordes, btnXispesLiniaInferior;
	Viewer v;

	public void mouseClicked(MouseEvent e) {
		if (e.getSource().equals(btnXispesBordes)) {
			this.v.getFoc().setXispesABordes(true);
		} else if (e.getSource().equals(btnXispesLiniaInferior)) {
			this.v.getFoc().setXispesABordes(false);
		}
	}
	
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	
	public ControlPanel(Viewer v) {
		super(new GridBagLayout());
		this.v = v;
		
		GridBagConstraints b = new GridBagConstraints();
		b.fill = GridBagConstraints.HORIZONTAL;
		
		this.btnXispesBordes = afegirBotoNou(btnXispesBordes, "Generar foc als bordes", 0, 0, b);
		this.btnXispesLiniaInferior = afegirBotoNou(this.btnXispesLiniaInferior, "Generar xispes a part inferior", 0, 1, b);
	}
	
	private JButton afegirBotoNou(JButton boto, String titol, int x, int y, GridBagConstraints b) {
		boto = new JButton(titol);
		boto.addMouseListener(this);
		b.gridx = x;
		b.gridy = y;
		this.add(boto, b);
		
		return boto;
	}

}
