package kinasjakk;

public class Piece {

	Board board;

    public Player player;

    private Hex hex;

	public Hex getHex() {
		return hex;
	}

	public void setHex(Hex hex) {
		this.hex = hex;
	}

	public Piece(Player player, Hex atHex) {
		board = atHex.board;
    	this.player = player;
    	hex = atHex;
    }
	
    public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}


	public boolean isAtGoal() {
		return getPlayer().getGoalHexes().contains(this);
	}
	public boolean isAtStart() {
		return getPlayer().getStartHexes().contains(this);
	}
	public boolean isInMiddle() {
		return !(isAtGoal() || isAtStart());
	}
	// TODO: Add boolean isAtIllegalPosition()

}
