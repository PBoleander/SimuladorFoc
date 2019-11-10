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
	
	private static final long serialVersionUID = 1L;
	private Viewer v;
	private ControlPanel cp;
	
	public MainFoc(Image imatge) {
		super();
		this.v = new Viewer(imatge);
		this.cp = new ControlPanel(this.v);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setMinimumSize(new Dimension(1366, 768));
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
		this.add(v, c);
		
		this.pack();
	}

	public static void main(String[] args) throws IOException {
		Image imatge = ImageIO.read(new File("/home/oleander/Imágenes/Fondos/sunset_in_rome-wallpaper-1366x768.jpg"));
		//Image imatge = ImageIO.read(new File("/home/oleander/Imágenes/ImatgePerFocJava.png"));
		//Image imatge = ImageIO.read(new File("/home/oleander/Imágenes/Fondos/water_background_aesthetic-wallpaper-1366x768.jpg"));
		//Image imatge = ImageIO.read(new File("/home/oleander/Imágenes/Fondos/hidden_waterfalls-wallpaper-1366x768.jpg"));
		new MainFoc(imatge);
	}

}
