package AI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import kinasjakk.Board;
import kinasjakk.Game;
import kinasjakk.Hex;
import kinasjakk.HexMove;
import kinasjakk.Piece;
import kinasjakk.Player;

public class MinimaxAI_2deep extends AI {
	
	static int DEPTH = 2;
	static boolean VERBOSE = false;
	
	Comparator<HexMove> compareHexMoves = new Comparator<HexMove>() {
	    @Override
	    public int compare(HexMove move1, HexMove move2) {
	    	Hex goal = findFurthestGoalHex(move1.getStartHex());
	    	if (goal == null) return 1;
	    	int d1 = nonSqrtDistance(goal, move1.getStartHex());
	    	int d2 = nonSqrtDistance(goal, move2.getStartHex());
	    	if (d1 > d2) return -1;
	    	else if (d2 > d1) return 1;
	    	return 0;
	    }
	};
	
	class EvalMove {
		public int evalValue;
		public HexMove move;
		public EvalMove(HexMove move,  int eval) {
			this.move = move;
			this.evalValue = eval;
		}
	}

	public MinimaxAI_2deep(Player player) {
		super(player, "MinimaxAI_2deep");
	}
	
	@Override
	public HexMove nextMove(Board board, Game game) {
		List<HexMove> possibleMoves = game.getBoard().getPossibleMovesFrom(player);
		EvalMove evalMove = minimax(game, DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
		if (VERBOSE) if (VERBOSE) System.out.println(evalMove.move + " (searched " + DEPTH + " deep)");
		return evalMove.move;
	}
	
	private Player whoIsPlayingBasedOnMaximize(boolean maximize) {
		return maximize ? player : player.getOpponent();
	}
	
	private int evaluateBoard(Game game, boolean maximize) {
		int distance = distanceRemaining(player.getOpponent()) - distanceRemaining(player);
		// TODO if no jumps, its negative stuff
		int insidePieces = numOfPiecesInGoal(player) - numOfPiecesInGoal(player.getOpponent());
		return distance + (int)Math.pow(20, insidePieces);
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
		try {
			Collections.sort(possibleMoves, compareHexMoves);
		} catch(Exception e) {
			
		}
		if (maximize) {
			int maxEval = Integer.MIN_VALUE;
			int i = 0;
			if (depth == DEPTH) if (VERBOSE) System.out.println("===================");
			for(HexMove move : possibleMoves) {
				i++;
				if (depth == DEPTH) if (VERBOSE) System.out.println(i + " / " + possibleMoves.size());
				b.makeMove(move);
				int eval = minimax(game, depth - 1, alpha, beta, false).evalValue;
				b.undoMove(move);
				if (eval > maxEval) {
					maxEval = eval;
					bestMove = move;
					if (depth == DEPTH) if (VERBOSE) System.out.println(move + " VALUE: " + maxEval);
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
