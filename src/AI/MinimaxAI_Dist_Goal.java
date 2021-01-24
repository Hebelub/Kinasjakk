package AI;

import java.util.Collections;
import java.util.List;

import kinasjakk.Board;
import kinasjakk.Game;
import kinasjakk.Hex;
import kinasjakk.HexMove;
import kinasjakk.Piece;
import kinasjakk.Player;

public class MinimaxAI_Dist_Goal extends AI {
	
	static int DEPTH = 3;
	
	class EvalMove {
		public int evalValue;
		public HexMove move;
		public EvalMove(HexMove move,  int eval) {
			this.move = move;
			this.evalValue = eval;
		}
	}

	public MinimaxAI_Dist_Goal(Player player) {
		super(player, "MinimaxAI_Dist_Goal");
	}
	
	@Override
	public HexMove nextMove(Board board, Game game) {
		EvalMove evalMove = minimax(game, DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
		System.out.println(evalMove.move);
		return evalMove.move;
	}
	
	private Player whoIsPlayingBasedOnMaximize(boolean maximize) {
		return maximize ? player : player.getOpponent();
	}
	
	private int evaluateBoard(Game game, boolean maximize) {
		int distance = distanceRemaining(player.getOpponent()) - distanceRemaining(player);
		int insidePieces = numOfPiecesInGoal(player) - numOfPiecesInGoal(player.getOpponent());
		return distance + insidePieces * 250;
	}
	
	private EvalMove minimax(Game game, int depth, int alpha, int beta, boolean maximize) {
		if (depth == 0 || game.getWhoseTurn().hasFinished()) {
			int eval = evaluateBoard(game, maximize);
			return new EvalMove(null, eval);
		}
		Board b = game.getBoard();
		HexMove bestMove = null;
		Player whoseTurn = whoIsPlayingBasedOnMaximize(maximize);
		List<HexMove> possibleMoves = game.getBoard().getPossibleMovesFrom(whoseTurn);
		Collections.shuffle(possibleMoves);
		if (maximize) {
			int maxEval = Integer.MIN_VALUE;
			int i = 0;
			if (depth == DEPTH) System.out.println("===================");
			for(HexMove move : possibleMoves) {
				i++;
				if (depth == DEPTH) System.out.println(i + " / " + possibleMoves.size());
				b.makeMove(move);
				int eval = minimax(game, depth - 1, alpha, beta, false).evalValue;
				b.undoMove(move);
				if (eval > maxEval) {
					maxEval = eval;
					bestMove = move;
					if (depth == DEPTH) {
						Hex goal = findFurthestGoalHex(move.getStartHex());
						if (goal != null)
							System.out.println("Calculating distance towards goal = " + goal.id + " for player " + whoseTurn.getID());
						System.out.println(move + " VALUE: " + maxEval);
					}
				}
				alpha = Math.max(alpha, eval);
				if (beta <= alpha) break;
			}
			return new EvalMove(bestMove, maxEval);
		} else {
			int minEval = Integer.MAX_VALUE;
			for(HexMove move : possibleMoves) {
				b.makeMove(move);
				int eval = minimax(game, depth - 1, alpha, beta, true).evalValue;
				b.undoMove(move);
				if (eval < minEval) {
					minEval = eval;
					bestMove = move;
				}
				beta = Math.min(beta, eval);
				if (beta <= alpha) break;
			}
			return new EvalMove(bestMove, minEval);
		}
	}

}
