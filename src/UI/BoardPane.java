package UI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import kinasjakk.Game;
import kinasjakk.Hex;
import kinasjakk.HexJump;
import kinasjakk.HexMove;
import kinasjakk.Player;

public class BoardPane extends JPanel {

	BufferedImage boardImage;
	BufferedImage pieceImage;
    BufferedImage bufferedImage; 
    Graphics g;
    Game game;
    
    // draw red arrow showing all jumps in currentMove
    boolean shouldDrawJumpArrows = true;
    HexMove currentMove;
    // draw white circles for endpoint hexes in possibleMoves
    boolean shouldDrawPossibleMoves = true;
    List<HexMove> possibleMoves;
    //Hex numbers
    boolean shouldDrawHexNumbers = true;
    
	public BoardPane() {
		possibleMoves = new ArrayList<HexMove>();
		loadImages();
		bufferedImage = new BufferedImage(2000, 2000, BufferedImage.TYPE_INT_ARGB);
		g = bufferedImage.getGraphics();
	}
	
	public void setDrawPossibleMoves(boolean bool) {
		this.shouldDrawPossibleMoves = bool;
	}
	
	public void setDrawArrow(boolean bool) {
		this.shouldDrawJumpArrows = bool;
	}
	
	public void setDrawHexNumbers(boolean bool) {
		this.shouldDrawHexNumbers = bool;
	}
	
	public void setPossibleMoves(List<HexMove> moves) {
		possibleMoves = moves;
	}
	
	public void setCurrentMove(HexMove move) {
		this.currentMove = move;
	}
	
	public void setGame(Game game) {
		this.game = game;
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
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        if (game.getBoard() == null) return;
        drawBoardBackground();
        drawHexes();
        if (shouldDrawPossibleMoves) drawPossibleMoves();
        if (shouldDrawJumpArrows) drawJumpArrows();
        if (shouldDrawHexNumbers) drawHexNumbers();
        
	}
	
	public void drawJumpArrows() {
		for(HexJump jump : currentMove.getPath()) {
			Point pStart = getHexCenterPoint(jump.getStartHex());
			Point pEnd = getHexCenterPoint(jump.getEndHex());
			drawArrow(Color.RED, pStart, pEnd, 20);
		}
	}

	public void drawPossibleMoves() {
		for(HexMove move : possibleMoves) {
			Point endPoint = getHexCenterPoint(move.getEndHex());
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(10));
			drawCenteredCircle(g2, endPoint.x, endPoint.y, 100, Color.WHITE, false);
		}
	}
	
	public void drawHexNumbers() {
		for (Hex hex : game.getBoard().getHexes()) {
			drawHexNumber(hex);
    	}
	}
	
	public void drawHexes() {
		Player whoseTurn = game.getWhoseTurn();
    	for (Hex hex : game.getBoard().getHexes()) {
    		if (hex.isEmpty()) {
    			drawEmptyHex(hex);
    		}else {
    			boolean highlight = (hex.getPlayer().getID() == whoseTurn.getID());
    			drawPlayerHex(hex, highlight);
    		}
    	}
	}
	
	public void drawBoardBackground() {
        g.drawImage(boardImage, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null);
	}
	
	public Point getHexCenterPoint(Hex hex) {
    	int d = 129;
    	int offset = d / 2;
    	int x = hex.getX();
		int y = hex.getY();
		int drawX = x * d - offset * y + 480;
		int drawY = y * 112 + 100;
		return new Point(drawX, drawY);
	}
	
	public void drawPlayerHex(Hex hex, boolean highlight) {
		Player player = hex.getPlayer();
		Color c = player.getColor();
		Color negative = getNegativeColor(c);
		Point drawPoint = getHexCenterPoint(hex);
		if (highlight)
			drawCenteredCircle(g, drawPoint.x, drawPoint.y, 120, negative, true);
		drawCenteredCircle(g, drawPoint.x, drawPoint.y, 100, c, true);
	}
	
	public void drawEmptyHex(Hex hex) {}
	
	public void drawHexNumber(Hex hex) {
		Point drawPoint = getHexCenterPoint(hex);
		boolean empty = hex.isEmpty();
		g.setFont(g.getFont().deriveFont(Font.BOLD, 60f));
		String hexId = String.valueOf(hex.id);
		int size = g.getFontMetrics().stringWidth(hexId);
		g.setColor(Color.BLACK);
		if (!hex.isEmpty() && hex.getPlayer().getColor() == Color.BLACK) g.setColor(Color.PINK);
		g.drawString(String.valueOf(hex.id), drawPoint.x - size / 2 + 2, drawPoint.y + 24);
		g.setColor(empty ? Color.WHITE : Color.MAGENTA);
		g.drawString(String.valueOf(hex.id), drawPoint.x - size / 2 - 2, drawPoint.y + 20);
	}
	
	public Color getNegativeColor(Color color) {
		return new Color(255 - color.getRed(),  255 - color.getGreen(),  255 - color.getBlue()); 
	}
	
	private void drawArrow(Color color, Point start, Point end, int size) {  
		AffineTransform tx = new AffineTransform();
		Line2D.Double line = new Line2D.Double(start, end);
		
		//Calculate arrowhead rotation
		int arrowheadSize = size + 10;
		Polygon arrowHead = new Polygon();
		arrowHead.addPoint( 0,arrowheadSize);
		arrowHead.addPoint( -arrowheadSize, -arrowheadSize);
		arrowHead.addPoint( arrowheadSize,-arrowheadSize);
	    tx.setToIdentity();
	    double angle = Math.atan2(line.y2-line.y1, line.x2-line.x1);
	    tx.translate(line.x2, line.y2);
	    tx.rotate((angle-Math.PI/2d));  
	
	    //Draw arrow line
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.RED);
		g2.setStroke(new BasicStroke(size));
	    g.drawLine(start.x, start.y, end.x, end.y);
	    
	    //Draw arrowhead
	    Graphics2D tempg2 = (Graphics2D) g.create();
	    tempg2.setTransform(tx);   
	    tempg2.fill(arrowHead);
	    tempg2.dispose();
	}
	
	public void drawCenteredCircle(Graphics g, int x, int y, int r, Color c, boolean fill) {
		x = x-(r/2);
		y = y-(r/2);
		g.setColor(c);
		if (fill) g.fillOval(x,y,r,r);
		else g.drawOval(x, y, r, r);
	}
	
}
