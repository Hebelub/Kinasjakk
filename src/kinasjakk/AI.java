package kinasjakk;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AI {
	
	public AI() {
		
	}
	
	public void run() {
		Board initialBoard = new Board();
	}

	public int aiStrength = 6;

	public Board getBestMove(int depth, Board board) {
		if(depth > aiStrength) {
			return board;
		}

		for(HexMove move : getPossibleProductiveMoves(board)) {
			Board alteredBoard = board/*.clone()*/;
			alteredBoard.makeMove(move);
			return getBestMove(depth + 1, alteredBoard);
		}

		return null;
	}

	public HexMove getCrossingMove(Board board) {
		for (HexMove move : getPossibleProductiveMoves(board)) {
			if (move.isCrossing()) {
				return move;
			}
		}
		return getRandomMove(board);
	}

	public HexMove getRandomMove(Board board) {
		Random random = new Random();
		List<HexMove> possibleMoves = getPossibleProductiveMoves(board);
		if(possibleMoves.size() > 0)
			return possibleMoves.get(random.nextInt(possibleMoves.size()));
		else return null;
	}

	private ArrayList<HexMove> getPossibleProductiveMoves(Board board) {
		ArrayList<HexMove> possibleMoves = new ArrayList<>();

		for (Piece piece : board.getPlayerToMove().getPieces()) {
			List<Hex> possibleHexes = board.getPossibleHexesFrom(piece.getHex());
			for(Hex hex : possibleHexes) {
				HexMove move = new HexMove(piece.getHex(), hex);
				if(!move.movesFromGoal()) {
					possibleMoves.add(move);
				}
			}
		}

		return possibleMoves;
	}

	private float getValueAfterMove(Board board, HexMove move) {
		board.makeMove(move);
		return getPlayerValue(board, board.getPlayerToMove());
	}

	private float getPlayerValue(Board board, Player player) {

		float value = 0;

		value += getNumberOfHexesInGoal();

		value += calculateScoreOfEmptyGoalHexes(getNumberOgEmptyGoalHexes(player));


		return value;
	}

	private float calculateScoreOfEmptyGoalHexes(int quantity) {
		return 1 - 1/(quantity + 1);
	}
	private int getNumberOgEmptyGoalHexes(Player player) {
		return 0;
	}
	private int getNumberOfHexesInGoal() {
		return 0;
	}
}
