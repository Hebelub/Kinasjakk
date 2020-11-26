package kinasjakk;

import java.util.*;
import java.util.List;

public class Hex implements Comparable<Hex> {

	@Override
	public int compareTo(Hex compareId) {

		return this.id - compareId.id;
	}

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
		return "" + id + " " + hasPiece();
		// return "Hex: " + id + ", [neighbours=" + Arrays.toString(ids) + "]";
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

	public void setPiece(Piece piece) {
		this.piece = piece;
	}
}
