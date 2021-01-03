package AI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kinasjakk.Board;
import kinasjakk.Hex;
import kinasjakk.HexMove;
import kinasjakk.Player;

public abstract class AI {
	
	String name;
	Player player;
	
	List<HexMove> latestMoves; // cannot repeat a move in this list
	final int lastestMovesCount = 3; // how many latestMoves to keep in memory
	
	public AI(Player player, String name) {
		this.name = name;
		this.player = player;
		latestMoves = new ArrayList<HexMove>();
	}
	
	public String getName() {
		return name;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	protected void addMoveToLatest(HexMove move) {
		latestMoves.add(move);
		if (latestMoves.size() > lastestMovesCount)
			latestMoves.remove(0);
	}
	
	protected boolean isMoveInLatest(HexMove move) {
		for(HexMove m : latestMoves) {
			if (move.isEquivalentTo(m)) return true;
		}
		return false;
	}
	
	
	protected List<HexMove> getPossibleMoves(Board board) {
		List<HexMove> possibleMoves = board.getPossibleMovesFrom(player);
		possibleMoves.removeIf(move -> isMoveInLatest(move));
		return possibleMoves;
	}
	
	
    public abstract HexMove nextMove(Board board);
    
    protected List<HexMove> findMovesDirectlyToGoal(List<HexMove> possibleMoves, Player player) {
    	List<HexMove> movesToGoal = new ArrayList<HexMove>();
		for(HexMove move : possibleMoves) {
			Hex startPoint = move.getStartHex();
			Hex endPoint = move.getEndHex();
			if (endPoint.getOwner() != null) {
				Player owner = endPoint.getOwner();
				// If owner of move endPoint is my opponent, this is a goal hex
				// Ignore moves that are from goal hex to goal hex.
				boolean moveEndsInGoal = owner.getID() == player.getOpponent().getID();
				boolean moveStartsInGoal = (startPoint.getOwner() == null) ? false : (startPoint.getOwner().getID() == player.getOpponent().getID());
				if (moveEndsInGoal && !moveStartsInGoal) {
					movesToGoal.add(move);
				}
			}
		}
		return movesToGoal;
	}
    
    protected HexMove findClosestMoveToGoal(List<HexMove> possibleMoves, Board board) {
		/* 
		 * Randomly pick an empty goal hex and calculate distance 
		 * between this and all possibleMoves to find closest move
		 * If no empty goal hex exists, this returns null
		 */
		Hex goal = player.getOpponent().getStartHexes().get(0);		
		HexMove closestMove = possibleMoves.get(0);
		double currentDistance = Integer.MAX_VALUE;
		for(int i = 1; i < possibleMoves.size(); i++) {
			HexMove move = possibleMoves.get(i);
			// Ignore move if startHex is already in goal hex
			if (move.getStartHex().ownedByOpponent(player)) continue;
			Hex end = move.getEndHex();
			//Calculate difference
			int diffX = end.getX() - goal.getX();
			int diffY = end.getY() - goal.getY();
			double distance = Math.sqrt(diffX*diffX + diffY*diffY);
			// If closer than current record, set new record
			if (distance < currentDistance) {
				currentDistance = distance;
				closestMove = move;
			}
		}
		return closestMove;
	}
	
    protected List<Hex> findEmptyGoalHexes() {
		List<Hex> goalHexes = player.getOpponent().getStartHexes();
		List<Hex> emptyGoalHexes = new ArrayList<Hex>();
		for(Hex goal : goalHexes) {
			if (goal.isEmpty())
				emptyGoalHexes.add(goal);
		}
		return emptyGoalHexes;
	}
    
	
	protected HexMove findRandomMoveNotFromGoal(List<HexMove> possibleMoves) {
		List<HexMove> notFromGoal = new ArrayList<HexMove>();
		for(HexMove move : possibleMoves) {
			if (!move.getStartHex().ownedByOpponent(player)) {
				notFromGoal.add(move);
			}
		}
		Random rand = new Random();
		return notFromGoal.get(rand.nextInt(notFromGoal.size()));
	}
    
    protected void printAction(String actionText) {
		System.out.println("(Player" + player.getID() + ") " + player.getName() + " " + actionText);
    }
}
