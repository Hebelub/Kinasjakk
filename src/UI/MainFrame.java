package UI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame {



	JFrame frame;
	BoardPane boardPane;
	SideBar sideBar;
	
	public MainFrame() {
		frame = new JFrame();
		frame.setSize(new Dimension(1000, 500));
		frame.setLayout(new GridLayout(1, 2));
		boardPane = new BoardPane();
		sideBar = new SideBar();
        frame.add(boardPane);
        frame.add(sideBar);
        frame.setVisible(true);
	}
	
}
