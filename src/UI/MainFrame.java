package UI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame {



	JFrame frame;
	BufferedImage boardImage;
	
	public MainFrame() {
		frame = new JFrame();
		frame.setSize(new Dimension(500, 500));
		
		try {
			boardImage = ImageIO.read(new File("img/board.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

        JPanel pane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(boardImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        frame.add(pane);
        frame.setVisible(true);
	}
	
}
