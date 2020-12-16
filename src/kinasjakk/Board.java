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

	public List<Hex> getStartHexesOfPlayer(Player player) {
		List<Hex> startHexes = playerStartHexes.get(player);
		if(startHexes == null)
			return new ArrayList<>();
		return startHexes;
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
		if(!allPlayersHasFinished()) {
			playerTurn++;

			if(getPlayerToMove().hasFinished()) {
				setNextTurn();
			}

			if(!getPlayerToMove().isHuman()) {
				game.ai.getCrossingMove(this).move();
			}
		}

		return getPlayerToMove();
	}

	public boolean allPlayersHasFinished() {
		for(Player player : players)
			if(!player.hasFinished())
				return false;
		return true;
	}

	public Game game;
	public Board(Game game) {
		this.game = game;
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
