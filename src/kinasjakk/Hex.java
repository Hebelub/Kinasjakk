package kinasjakk;

import java.util.*;
import java.util.List;

public class Hex {
	private static int nextId = 0;
	public int id;
	private Piece piece;
	
	private int x;
	private int y;

	public Hex[] neighbours;

	public Hex() {
		id = nextId++;
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
		return "" + id + " " + isEmpty();
		// return "Hex: " + id + ", [neighbours=" + Arrays.toString(ids) + "]";
	}

	// Returns all the possible hexes a piece can jump to from any given Hex
	public List<Hex> possibleHexesFrom(Hex from) {

		// Creating list to test for and adding the first position to it
		List<Hex> hexesToTestFrom = new ArrayList<>();
		hexesToTestFrom.add(from);
		// Hexes to test after all in hexesToTestFrom is tested
		List<Hex> hexesToTestFromAfter = new ArrayList<>();
		// Create a list of all tested positions
		List<Hex> blockedHexes = new ArrayList<>();

		blockedHexes.addAll(hexesToTestFrom);

		while(true) {
			for(Hex hex : hexesToTestFrom) {
				List<Hex> hexesInAllDirections = testInAllDirectionsFrom(hex, blockedHexes);

				hexesToTestFromAfter.addAll(hexesInAllDirections);
				blockedHexes.addAll(hexesInAllDirections);
			}

			if(hexesToTestFromAfter.size() > 0) {
				hexesToTestFrom = hexesToTestFromAfter;
				hexesToTestFromAfter = new ArrayList<>();
			}
			else break;
		}

		// Removes the Hex that is the startingHex
		blockedHexes.remove(0);

		return blockedHexes;

	}

	private List<Hex> testInAllDirectionsFrom(Hex from, List<Hex> blockedHexes) {
		List<Hex> possibleHexesFromPosition = new ArrayList<>();

		possibleHexesFromPosition.addAll(getAvailableHexesInLineFrom(new PointingHex(from, Direction.TOP_LEFT), blockedHexes));
		possibleHexesFromPosition.addAll(getAvailableHexesInLineFrom(new PointingHex(from, Direction.TOP_RIGHT), blockedHexes));
		possibleHexesFromPosition.addAll(getAvailableHexesInLineFrom(new PointingHex(from, Direction.RIGHT), blockedHexes));
		possibleHexesFromPosition.addAll(getAvailableHexesInLineFrom(new PointingHex(from, Direction.BOTTOM_RIGHT), blockedHexes));
		possibleHexesFromPosition.addAll(getAvailableHexesInLineFrom(new PointingHex(from, Direction.BOTTOM_LEFT), blockedHexes));
		possibleHexesFromPosition.addAll(getAvailableHexesInLineFrom(new PointingHex(from, Direction.LEFT), blockedHexes));

		return possibleHexesFromPosition;
	}

	public List<Hex> getOneDistanceHexes() {
		List<Hex> oneDistanceHexes = new ArrayList<>();

		for (Hex hex : neighbours) {
			if (hex != null && hex.isEmpty()) {
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

		int checkToDynamic = hexLine.size() / 2;

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
			else if(!endHex.isEmpty()) {
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
			if(hexLine.get(i).isEmpty() != hexLine.get(end - i).isEmpty()) {
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

	public Piece getPiece() {
		return piece;
	}

	public void setPiece(Piece piece) {
		this.piece = piece;
	}
}
