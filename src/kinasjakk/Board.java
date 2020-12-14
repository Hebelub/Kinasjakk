package kinasjakk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Board implements Cloneable {

	@Override
	public Board clone() throws CloneNotSupportedException {
		try {
			return (Board) super.clone();

		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	List<Hex> hexes;
	private HashMap<Player, ArrayList<Hex>> playerStartHexes;
	// private HashMap<Player, ArrayList<Hex>> playerGoalHexes; // TODO: Maybe fill this with goalHexes for each player

	public ArrayList<Hex> getStartHexesOfPlayer(Player player) {
		return playerStartHexes.get(player);
	}
//	public ArrayList<Hex> getGoalHexesOfPlayer(Player player) {
//		return playerGoalHexes.get(player);
//	}

	Player[] players;
	int playerTurn = 0;

	public Player getPlayerNumber(int turn) {
		return players[turn % players.length];
	}
	public Player getPlayerToMove() {
		return getPlayerNumber(playerTurn);
	}
	public Player setNextTurn() {
		// TODO: Check if next player has finished, if so setNextTurn()
		playerTurn++;
		return getPlayerToMove();
	}

	public Board() {
		playerStartHexes = new HashMap<>();
		// playerGoalHexes = new HashMap<>();
		hexes = new ArrayList<>();
	}
	
	public void addHex(Hex hex) {
		hexes.add(hex);
		addHexToStartHexes(hex);
		//addHexToGoalHexes(hex);
	}

	private void addHexToStartHexes(Hex hex) {
		if(hex.hasPiece()) {
			if (getStartHexesOfHexPlayer(hex) == null) {
				playerStartHexes.put(hex.getPiece().getPlayer(), new ArrayList<>());
			}
			getStartHexesOfHexPlayer(hex).add(hex);
		}
	}
	private ArrayList<Hex> getStartHexesOfHexPlayer(Hex hex) {
		return playerStartHexes.get(hex.getPiece().player);
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
	public void makeMove(HexMove move) {
		if(move != null) {
			makeMove(move.getFrom(), move.getTo());
		}
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}
}
