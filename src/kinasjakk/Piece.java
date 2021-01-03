package kinasjakk;

public class Piece {

    private Player player;
    private Hex hex;

	public Piece(Player player) {
    	this.player = player;
    	player.addPiece(this);
    }
	
    public int getPlayerID() {
		return player.getID();
	}
    
    public Player getPlayer() {
    	return this.player;
    }
    
    public void setHex(Hex hex) {
    	this.hex = hex;
    }
    
    public Hex getHex() {
    	return hex;
    }
}
