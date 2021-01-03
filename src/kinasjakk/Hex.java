package kinasjakk;

import java.util.*;
import java.util.List;

import javafx.scene.shape.Line;

public class Hex implements Comparable<Hex> {

	@Override
	public int compareTo(Hex compareId) {
		return this.id - compareId.id;
	}
	
	public int id;
	private Piece piece;
	private Player hexOwner;
	
	private int x;
	private int y;

	public Hex[] neighbours;

	public Hex(int id) {
		this.id = id;
		neighbours = new Hex[6];
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setNeighbour(Direction dir,  Hex otherHex) {
		if (neighbours[dir.ordinal()] == null) {
			neighbours[dir.ordinal()] = otherHex;
			otherHex.setNeighbour(Direction.opposite(dir), this);
		}
	}

	@Override
	public String toString() {
		String[] ids = new String[6];
		for (int i = 0; i < neighbours.length; i++) {
			String id = "null";
			if (neighbours[i] != null)
				id = String.valueOf(neighbours[i].id);
			ids[i] = String.valueOf(Direction.values()[i]) + "=" + id;
		}
		return "" + id + " " + hasPiece();
	}

	public List<Hex> getPossibleHexes() {

		Piece piece = getPiece();
		setPiece(null); // Temporarily removing piece

		List<Hex> untestedHexes = new ArrayList<>();
		List<Hex> possibleHexes = new ArrayList<>();

		untestedHexes.add(this);
		possibleHexes.add(this); // This is removed at the end

		testAllHexes(untestedHexes, possibleHexes);

		possibleHexes.remove(0); // Removes the Hex that is the from-Hex

		possibleHexes.addAll(getOneDistanceHexes(possibleHexes));

		setPiece(piece);
		return possibleHexes;
	}

	private void testAllHexes(List<Hex> untestedHexes, List<Hex> testedHexes) {
		while(true) {
			untestedHexes = getAvailableHexesInAllDirectionsFromHexes(untestedHexes, testedHexes);

			if(untestedHexes.size() > 0) {
				testedHexes.addAll(untestedHexes);
			} else break;
		}
	}

	private List<Hex> getAvailableHexesInAllDirectionsFromHexes(List<Hex> untestedHexes, List<Hex> blockedHexes) {
		List<Hex> hexesInAllDirections = new ArrayList<>();
		for(Hex hex : untestedHexes) {
			hexesInAllDirections.addAll(getAvailableHexesInAllDirectionsFrom(hex, blockedHexes));
		}
		return hexesInAllDirections;
	}

	private List<Hex> getAvailableHexesInAllDirectionsFrom(Hex from, List<Hex> blockedHexes) {
		List<Hex> possibleHexesFromPosition = new ArrayList<>();

		possibleHexesFromPosition.addAll(getAvailableHexesInLineFrom(new PointingHex(from, Direction.TOP_LEFT), blockedHexes));
		possibleHexesFromPosition.addAll(getAvailableHexesInLineFrom(new PointingHex(from, Direction.TOP_RIGHT), blockedHexes));
		possibleHexesFromPosition.addAll(getAvailableHexesInLineFrom(new PointingHex(from, Direction.RIGHT), blockedHexes));
		possibleHexesFromPosition.addAll(getAvailableHexesInLineFrom(new PointingHex(from, Direction.BOTTOM_RIGHT), blockedHexes));
		possibleHexesFromPosition.addAll(getAvailableHexesInLineFrom(new PointingHex(from, Direction.BOTTOM_LEFT), blockedHexes));
		possibleHexesFromPosition.addAll(getAvailableHexesInLineFrom(new PointingHex(from, Direction.LEFT), blockedHexes));

		return possibleHexesFromPosition;
	}

	public List<Hex> getOneDistanceHexes(List<Hex> blockedHexes) {

		List<Hex> oneDistanceHexes = new ArrayList<>();

		for (Hex hex : neighbours) {
			if (hex != null && hex.isEmpty() && !isHexInBlockedList(hex, blockedHexes)) {
				oneDistanceHexes.add(hex);
			}
		}

		return oneDistanceHexes;
	}

	public Hex getNeighbor(Direction d) {
		return neighbours[d.ordinal()];
	}

	public List<Hex> getAvailableHexesInLineFrom(PointingHex from, List<Hex> blockedHexes) {

		List<Hex> hexLine = from.getHexLine();

		List<Hex> possibleMoves = new ArrayList<>();

		boolean hasIteratedOverAPiece = false; // No hex can jump without at least one piece between

		int checkToDynamic = (hexLine.size() + 1) / 2;

		for(int end = 1; end < checkToDynamic; end++) {

			Hex endHex = hexLine.get(end);

			if(hasIteratedOverAPiece) {
				if(endHex.isEmpty() && !isHexInBlockedList(endHex, blockedHexes)) {
					if(LineSegmentHasSymmetry(hexLine, end)) {
						possibleMoves.add(endHex);
						end += end - 1; // Skipping impossible iterations
					}
				}
			}
			else if(endHex.hasPiece()) {
				checkToDynamic = hexLine.size();
				end += end - 1; // Skipping impossible iterations
				hasIteratedOverAPiece = true;
			}
		}

		return possibleMoves;
	}

	private Boolean LineSegmentHasSymmetry(List<Hex> hexLine, int end) {
		int toMiddle = (end - 1) / 2;

		for(int i = 1; i <= toMiddle; i++) {
			if(hexLine.get(i).hasPiece() != hexLine.get(end - i).hasPiece()) {
				return false;
			}
		}

		return true;
	}

	private boolean isHexInBlockedList(Hex hex, List<Hex> blockedHexes) {
		for (Hex blocked : blockedHexes) {
			if (hex == blocked) {
				return true;
			}
		}
		return false;
	}

	public boolean isEmpty() {
		return piece == null;
	}

	public boolean hasPiece() {
		return !(piece == null);
	}

	public Piece getPiece() {
		return piece;
	}

	public boolean ownedByOpponent(Player player) {
		if (hexOwner == null) return false;
		else if (hexOwner.getID() == player.getOpponent().getID()) return true;
		else return false;
	}
	
	public boolean ownedByPlayer(Player player) {
		if (hexOwner == null) return false;
		else if (hexOwner.getID() == player.getID()) return true;
		else return false;
	}
	
	public void setPiece(Piece piece) {
		this.piece = piece;
		if (piece != null) piece.setHex(this);
	}
	
	public void setOwner(Player player) {
		hexOwner = player;
		if (!player.getStartHexes().contains(this))
			player.addStartHex(this);
	}
	
	public Player getOwner() {
		return hexOwner;
	}
	
	public Player getPlayer() {
		return piece.getPlayer();
	}
	
	public List<HexMove> addOneDistanceHexes(List<HexMove> hexMoves) {
		//Get one-distance hexes
		List<Hex> oneDistanceHexes = getOneDistanceHexes(new ArrayList<Hex>());
		// Delete HexMove if its end hex is one of the one-distance hexes,
		// as one-distance move is less jumps
		ListIterator<HexMove> iter = hexMoves.listIterator();
		while(iter.hasNext()) {
			HexMove move = iter.next();
			Hex endHex = move.getEndHex();
			if (oneDistanceHexes.contains(endHex)) {
				oneDistanceHexes.remove(endHex); // Remove oneHex
				iter.remove(); // Remove current move
				// Add new move with a single jump, from myself to one-distance hex
				HexJump hexJump = new HexJump(this, endHex);
				HexPath path = new HexPath(hexJump, piece);
				iter.add(path.makeMove());
			}
		}
		
		//Add remaining one-distance moves
		for (Hex hex : oneDistanceHexes) {
			HexJump hexJump = new HexJump(this, hex);
			HexPath path = new HexPath(hexJump, piece);
			hexMoves.add(path.makeMove());
		}
		
		return hexMoves;
	}
	
	public List<HexMove> removeMovesEndingInEnemyTerritory(Player goodGuy, List<HexMove> hexMoves) {
		ListIterator<HexMove> iter = hexMoves.listIterator();
		while(iter.hasNext()) {
			HexMove move = iter.next();
			Hex endHex = move.getEndHex();
			// Check if goodGuy is in hex belonging to enemy
			Player hexOwner = endHex.getOwner();
			if (hexOwner != null && // hex has owner check
				goodGuy.getID() != hexOwner.getID() && // owner is not myself
				hexOwner.getID() != goodGuy.getOpponent().getID() // owner is not my opponent
			) {
				iter.remove();
			}	
		}
		return hexMoves;
	}
	
	// Removes moves that does not open up my territory, if it is full!
	// and I have pieces left at home
	private List<HexMove> removeMovesIfTerritoryFull(Player player, List<HexMove> hexMoves) {
		if (player.hasPieceAtStart() && player.allStartingHexesHavePiece()) {
			List<Hex> playerHexes = player.getStartHexes(); 
			ListIterator<HexMove> iter = hexMoves.listIterator();
			while(iter.hasNext()) {
				HexMove move = iter.next();
				Hex start = move.getStartHex();
				// Remove move (invalid) if it does not clear up my start area
				if (!start.ownedByPlayer(player)) {
					iter.remove();
				}
				
			}
		}
		return hexMoves;
	}

	public List<HexMove> getPossibleMoves() {
		// Make new empty list to populate and return at end of method
		List<HexMove> hexMoves = new ArrayList<HexMove>();

		// Temporarily remove piece (mimicking that piece "moves" when jumping)
		Piece piece = getPiece();
		setPiece(null);
		
		// Find hex jumps and store in paths, that become hex moves
		List<HexPath> hexPaths = new ArrayList<HexPath>();
		
		// Store a list of previously visited hexes, to avoid going in circles
		List<Hex> testedHexes = new ArrayList<Hex>();
		testedHexes.add(this);
		
		// Get initial valid hexes in straight line (a jump)
		List<Hex> initialHexes = getAvailableHexesInAllDirectionsFrom(this, testedHexes);
		for(Hex hex : initialHexes) {
			//Make new jump from myself to hex
			HexJump hexJump = new HexJump(this, hex);
			//This jump is the start of a long path of HexJumps
			HexPath path = new HexPath(hexJump, piece);
			hexPaths.add(path);
			//It's a valid move to stop here
			hexMoves.add(path.makeMove());
			//We don't need to revisit these hexes, as they are now part of a move already
			testedHexes.add(hex);
		}
		
		// Expand initial paths with more jumps
		while(hexPaths.size() > 0) {
			HexPath path = hexPaths.get(0);
			//Continue from endpoint of last jump in the path
			Hex end = path.getEndHex();
			List<Hex> hexesInLine = getAvailableHexesInAllDirectionsFrom(end, testedHexes);
			if (hexesInLine.size() > 0) {
				// If endpoint can reach hexes, new jumps are added to path
				for(Hex hex : hexesInLine) {
					HexJump hexJump = new HexJump(end, hex);
					HexPath continuePath = path.copy();
					continuePath.addJump(hexJump);
					hexPaths.add(continuePath);
					hexPaths.remove(path);
					//It's a valid move to stop here
					hexMoves.add(continuePath.makeMove());
					//We don't need to revisit these hexes.
					testedHexes.add(hex);
				}
			}else {
				//If no more jumps available, path is complete.
				hexPaths.remove(path);
			}
		}
		
		hexMoves = addOneDistanceHexes(hexMoves);
		hexMoves = removeMovesEndingInEnemyTerritory(piece.getPlayer(), hexMoves);
		hexMoves = removeMovesIfTerritoryFull(piece.getPlayer(), hexMoves);
		
		// Remove moves ending in my territory if piece outside already
		// Also make AI do completely random move if goalHexes are all filled up (i.e. no empty ones)
		// Should it be a rule that you must always have an open space in your territory?
		
		setPiece(piece); // Put piece back
		
		return hexMoves;
	}
}
