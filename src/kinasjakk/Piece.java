package kinasjakk;

public class Piece {

    public int player;

    private Hex hex;

	public Hex getHex() {
		return hex;
	}

	public void setHex(Hex hex) {
		this.hex = hex;
	}

	public Piece(int player, Hex atHex) {
    	this.player = player;
    	hex = atHex;
    }
	
    public int getPlayer() {
		return player;
	}

	public void setPlayer(int player) {
		this.player = player;
	}    
}
