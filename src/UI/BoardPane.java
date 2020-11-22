package UI;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import kinasjakk.Board;
import kinasjakk.Hex;

public class BoardPane extends JPanel {

	BufferedImage boardImage;
	BufferedImage pieceImage;
    BufferedImage bufferedImage; 
    Graphics g;
    Board board;
	
	public BoardPane() {
		loadImages();
		bufferedImage = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB);
		g = bufferedImage.getGraphics();
		setBackground(Color.darkGray);
	}
	
	public void setBoard(Board board) {
		this.board = board;
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
        if (board != null) {
        	List<Hex> hexes = board.getHexes();
        	int d = 64;
        	int offset = d / 2;
        	for (Hex hex : hexes) {
				int x = hex.getX();
				int y = hex.getY();
				int drawX = x * d - offset * y + 240;
				int drawY = y * 56 + 50;
        		if (!hex.isEmpty()) {
        			int playerId = hex.getPiece().getPlayer();
            		Color c = Color.GREEN;
            		if (playerId == 1) c = Color.GREEN;
            		if (playerId == 2) c = Color.BLACK;
            		if (playerId == 3) c = Color.WHITE;
            		if (playerId == 4) c = Color.BLUE;
            		if (playerId == 5) c = Color.RED;
            		if (playerId == 6) c = Color.YELLOW;
            		drawCenteredCircle(
            				g, 
            				drawX,
            				drawY,
            				50, 
            				c
            		);
        		}
				g.setColor(Color.PINK);
				g.setFont(g.getFont().deriveFont(30f));
				g.drawString(String.valueOf(hex.id), drawX - g.getFont().getSize() / 2, drawY + 5);
        	}
        }
	}
	
	public void drawCenteredCircle(Graphics g, int x, int y, int r, Color c) {
		x = x-(r/2);
		y = y-(r/2);
		g.setColor(c);
		g.fillOval(x,y,r,r);
	}
	
}
