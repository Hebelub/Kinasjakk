package kinasjakk;

import java.util.ArrayList;
import java.util.List;

public class Board  {
	
	private List<Hex> hexes;
	
	public Board(List<Hex> hexes) {
		this.hexes = hexes;
	}

	public List<Hex> getHexes() {
		return hexes;
	}

	public List<Hex> getPossibleHexesFrom(Hex startHex) {
		return startHex.getPossibleHexes();
	}
	
	public List<HexMove> getPossibleMovesFrom(Hex startHex) {
		return startHex.getPossibleMoves();
	}
	
 	public List<HexMove> getPossibleMovesFrom(Player player) {
 		List<HexMove> moves = new ArrayList<HexMove>();
 		for(Hex hex : player.getPieceHexes()) {
 			moves.addAll(getPossibleMovesFrom(hex));
 		}
 		return moves;
 	}

	/**
	 * Make move by giving start and end hex
	 * @param start Hex containing piece to move
	 * @param end Hex where piece will end up
	 */
	public void makeMove(Hex start, Hex end) {
		Piece pieceToMove = start.getPiece();
		start.setPiece(end.getPiece());
		end.setPiece(pieceToMove);	
	}
	
	/**
	 * Make move by giving a HexMove object with valid HexJumps
	 * @param move
	 */
	public void makeMove(HexMove move) {
		makeMove(move.getStartHex(), move.getEndHex());
	}
	
	public void undoMove(HexMove move) {
		makeMove(move.getEndHex(), move.getStartHex());
	}
}
