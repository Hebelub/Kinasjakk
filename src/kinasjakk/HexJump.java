package kinasjakk;

// A jump made by a piece, 
// i.e. from one hex to another in a straight line.
public class HexJump {

	private Hex start;
	private Hex end;
	
	public HexJump(Hex from, Hex to) {
		this.start = from;
		this.end = to;
	}

	public Piece getPiece() {
		return getStartHex().getPiece();
	}
	
	public Hex getStartHex() {
		return start;
	}

	public Hex getEndHex() {
		return end;
	}
	
	public String toString() {
		return start.id + " => " + end.id;
	}
}
