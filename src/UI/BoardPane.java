package UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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
		bufferedImage = new BufferedImage(2000, 2000, BufferedImage.TYPE_INT_ARGB);
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
        	int d = 129;
        	int offset = d / 2;
        	for (Hex hex : hexes) {
				int x = hex.getX();
				int y = hex.getY();
				int drawX = x * d - offset * y + 480;
				int drawY = y * 112 + 100;
        		if (!hex.isEmpty()) {
        			int playerId = hex.getPiece().getPlayer().getId();
            		Color c = Color.GREEN;
            		if (playerId == 0) c = Color.GREEN;
            		else if (playerId == 1) c = Color.BLACK;
            		else if (playerId == 2) c = Color.WHITE;
            		else if (playerId == 3) c = Color.BLUE;
            		else if (playerId == 4) c = Color.RED;
            		else if (playerId == 5) c = Color.YELLOW;
            		drawCenteredCircle(
            				g, 
            				drawX,
            				drawY,
            				100, 
            				c
            		);
    				g.setColor(Color.MAGENTA);
        		}else {
        			g.setColor(Color.WHITE);
        		}
				g.setFont(g.getFont().deriveFont(Font.BOLD, 60f));
				String hexId = String.valueOf(hex.id);
				int size = g.getFontMetrics().stringWidth(hexId);
				g.drawString(String.valueOf(hex.id), drawX - size / 2 - 2, drawY + 20);
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
