package AI;

import java.util.List;

import kinasjakk.Board;
import kinasjakk.Game;
import kinasjakk.HexMove;
import kinasjakk.Player;

public class RandomAI extends AI {

	public RandomAI(Player player) {
		super(player, "RandomAI");
	}
	
	@Override
	public HexMove nextMove(Board board, Game game) {
		// Do random move if piece not in goal
		List<HexMove> possibleMoves = board.getPossibleMovesFrom(player);
		return findRandomMoveNotFromGoal(possibleMoves);
	}

}
