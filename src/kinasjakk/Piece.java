package kinasjakk;

public class Piece {

    public Player player;

    private Hex hex;

	public Hex getHex() {
		return hex;
	}

	public void setHex(Hex hex) {
		this.hex = hex;
	}

	public Piece(Player player, Hex atHex) {
    	this.player = player;
    	hex = atHex;
    }
	
    public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}    
}
