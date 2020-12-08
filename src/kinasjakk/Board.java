package kinasjakk;

import java.util.ArrayList;
import java.util.List;

public class Board {

	List<Hex> hexes;

	Player[] players;
	int playerTurn = 0;

	public Player getPlayerNumber(int turn) {
		return players[turn % players.length];
	}
	public Player getPlayerToMove() {
		return getPlayerNumber(playerTurn);
	}
	public Player setNextTurn() {
		playerTurn++;
		return getPlayerToMove();
	}

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
		setNextTurn();
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}
}
