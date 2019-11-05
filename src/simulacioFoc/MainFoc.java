package simulacioFoc;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class MainFoc extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Viewer v;
	
	public MainFoc(Image imatge) {
		super();
		this.v = new Viewer(imatge);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setMinimumSize(new Dimension(300, 300));
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.NORTHEAST;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 1.0;
		
		this.add(v, c);
		
		this.pack();
	}

	public static void main(String[] args) throws IOException {
		Image imatge = ImageIO.read(new File("/home/oleander/Imágenes/ImatgePerFocJava.png"));
		new MainFoc(imatge);
	}

}
