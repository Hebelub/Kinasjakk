package AI;

import java.util.List;

import kinasjakk.Board;
import kinasjakk.Game;
import kinasjakk.HexMove;
import kinasjakk.Player;

public class HerdAI extends AI {
	
	public HerdAI(Player player) {
		super(player, "HerdAI");
	}

	public HexMove findMoveLowestOverallDistance(Board board) {
		List<HexMove> possibleMoves = getPossibleMoves(board);
		HexMove myMove = null; //findRandomMoveNotFromGoal(possibleMoves);
		double lowestScore = Integer.MAX_VALUE;
		for(HexMove move : possibleMoves) {
			// Make move, calculate score, then undo move
			board.makeMove(move);
			double score = distanceRemaining(player);
			if (score < lowestScore) {
				lowestScore = score;
				myMove = move;
			}
			board.undoMove(move);
		}
		return myMove;
	}
	
	@Override
	public HexMove nextMove(Board board, Game game) {
		return findMoveLowestOverallDistance(board);		
	}
}
