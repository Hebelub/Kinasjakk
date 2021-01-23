package AI;

import java.util.List;

import kinasjakk.Board;
import kinasjakk.Game;
import kinasjakk.HexMove;
import kinasjakk.Player;

public class GreedyAI extends AI {
	
	public GreedyAI(Player player) {
		super(player, "GreedyAI");
	}

	@Override
	public HexMove nextMove(Board board, Game game) {
		// This AI has the following priorities:
		// 1. Find move that ends in goal hex (an opponent's hex)
		// 2. Do move that gets closest to goal hex
		HexMove myMove;
		List<HexMove> possibleMoves = getPossibleMoves(board);
		List<HexMove> goalMoves = findMovesDirectlyToGoal(possibleMoves, player);
		if (goalMoves.size() > 0) { // If move to goal exists, do it!
			myMove = goalMoves.get(0);
			printAction("found goal move");
		} else { // Try to find move that gets closest to goal hex
			myMove = findClosestMoveToGoal(possibleMoves, board, player);
			printAction("tried to get as close as possible");
		}
		addMoveToLatest(myMove);
		return myMove;
	}
}
