package kinasjakk;

import java.util.ArrayList;
import java.util.List;

public class Board {
	
	List<Hex> hexes;
	
	public Board() {
		hexes = new ArrayList<>();
	}
	
	public void addHex(Hex hex) {
		hexes.add(hex);
	}

	public List<Hex> getHexes() {
		return hexes;
	}

	public List<Hex> getPossibleHexesFrom(Hex startHex) {
		return startHex.new Moves().getPossibleHexes();
	}

	public void makeMove(Hex from, Hex to) {
		Piece pieceToMove = from.getPiece();
		from.setPiece(to.getPiece());
		to.setPiece(pieceToMove);	
	}
}
