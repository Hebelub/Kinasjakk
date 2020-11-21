package UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class BoardPane extends JPanel {

	BufferedImage boardImage;
	BufferedImage pieceImage;
    BufferedImage bufferedImage; 
    Graphics g;
 
	
	public BoardPane() {
		loadImages();
		bufferedImage = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB);
		g = bufferedImage.getGraphics();
		setBackground(Color.darkGray);
	}
	
	private void loadImages() {
		try {
			boardImage = ImageIO.read(new File("img/board.png"));
			pieceImage = ImageIO.read(new File("img/piece.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
    protected void paintComponent(Graphics gfx) {
        super.paintComponent(gfx);
        int size = Math.min(getWidth(), getHeight());
        draw();
        gfx.drawImage(bufferedImage, 0, 0, size, size, null);
    }
	
	protected void draw() {
        g.drawImage(boardImage, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null);
        g.setXORMode(Color.YELLOW);
        g.drawImage(pieceImage, 475, 20, 50, 50, null);
	}
	
}
