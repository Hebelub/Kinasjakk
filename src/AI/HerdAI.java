package AI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kinasjakk.Board;
import kinasjakk.Hex;
import kinasjakk.HexMove;
import kinasjakk.Player;

public class HerdAI extends AI {
	
	public HerdAI(Player player) {
		super(player, "HerdAI");
	}

	public HexMove findMoveLowestOverallDistance(Board board) {
		List<HexMove> possibleMoves = getPossibleMoves(board);
		HexMove myMove = findRandomMoveNotFromGoal(possibleMoves);
		double lowestScore = Integer.MAX_VALUE;
		for(HexMove move : possibleMoves) {
			// Make move, calculate score, then undo move
			board.makeMove(move);
			double score = distanceRemaining();
			if (score < lowestScore) {
				lowestScore = score;
				myMove = move;
			}
			board.undoMove(move);
		}
		return myMove;
	}
	
	@Override
	public HexMove nextMove(Board board) {
		return findMoveLowestOverallDistance(board);		
	}
}
