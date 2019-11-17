package simulacioFoc;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JFrame;

public class MainFoc extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private ControlPanel cp;
	private Viewer viewer;
	
	public MainFoc() {
		super();
		this.viewer = new Viewer();
		this.cp = new ControlPanel(this.viewer);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setMinimumSize(new Dimension(600, 400));
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.NORTHEAST;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.0;
		c.weighty = 1.0;
		
		this.add(cp, c);
		
		c.weightx = 1.0;
		c.gridx = 1;
		this.add(viewer, c);
		
		this.pack();
	}

	public static void main(String[] args) {
		new MainFoc();
	}

}
