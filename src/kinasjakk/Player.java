package kinasjakk;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import AI.AI;

public class Player {

	private int id;
    private String name;
    private boolean isHuman;
    private Color color;
    private AI ai;
    private List<Piece> pieces;
    private Player opponent; // the player opposite this player, owning hexes that are my goals
    private List<Hex> myStartHexes;

    public Player(int id) {
    	this.id = id;
        name = "Player" + Integer.toString(id);
        isHuman = true;
        Random r = new Random();
        color = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
        pieces = new ArrayList<Piece>();
        myStartHexes = new ArrayList<Hex>();
    }
    
    public void setOpponent(Player opponent) {
    	this.opponent = opponent;
    	if (opponent.getOpponent() == null)
    		opponent.setOpponent(this);
    }

    public Player getOpponent() {
    	return opponent;
    }
    
    public List<Hex> getStartHexes() {
    	return myStartHexes;
    }
    
    public void addStartHex(Hex hex) {
    	myStartHexes.add(hex);
    }
    
    public void addPiece(Piece piece) {
    	this.pieces.add(piece);
    }
    
    public List<Piece> getPieces() {
    	return pieces;
    }
    
    public boolean hasFinished() {
    	for(Hex hex : getPieceHexes()) {
    		// If hex does not has owner, we are not finished
    		if (hex.getOwner() == null) return false;
    		// If hex owner is not my opponent, we are not finished
			Player owner = hex.getOwner(); 
			if (owner.getID() != getOpponent().getID()) return false;
    	}
    	// All my pieces are on my opponent's hexes, so I have finished!
    	return true;
    }
    
    //Check if all my starting hexes contain a piece, no matter the piece owner.
    public boolean allStartingHexesHavePiece() {
    	for (Hex hex : getStartHexes()) {
    		if (!hex.hasPiece()) return false;
    	}
    	return true;	
    }
    
    public boolean hasPieceAtStart() {
    	for(Hex hex : getPieceHexes()) {
    		if (hex.ownedByPlayer(this)) return true;
    	}
    	return false;
    }

	public List<Hex> getPieceHexes() {
    	List<Hex> hexes = new ArrayList<Hex>();
    	for(Piece piece : pieces) {
    		hexes.add(piece.getHex());
    	}
    	return hexes;
    }
    
    public AI getAI() {
    	return this.ai;
    }
    
    
    public int getID() {
    	return id;
    }
    
    public String getName() {
        return isHuman ? name : ai.getName();
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setColor(Color color) {
    	this.color = color;
    }
    
    public Color getColor() {
    	return color;
    }

    public boolean isHumanPlayer() {
        return isHuman;
    }

    public void setHumanPlayer() {
        isHuman = true;
    }
    
    public void setAIPlayer(AI ai) {
    	isHuman = false;
    	this.ai = ai;
    }
}
