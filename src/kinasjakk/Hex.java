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
			// System.out.println("In the while");
			for(Hex hex : hexesToTestFrom) {
				List<Hex> hexesInAllDirections = testInAllDirectionsFrom(hex, blockedHexes);
				// System.out.println("In the for --> hexesInAllDirections.size(): " + hexesInAllDirections.size());
				hexesToTestFromAfter.addAll(hexesInAllDirections);
				blockedHexes.addAll(hexesInAllDirections);
			}

			if(hexesToTestFromAfter.size() > 0) {
				// System.out.println("If happened");
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

		possibleHexesFromPosition.addAll(getHexesInLine(from, Direction.TOP_LEFT, blockedHexes));
		possibleHexesFromPosition.addAll(getHexesInLine(from, Direction.TOP_RIGHT, blockedHexes));
		possibleHexesFromPosition.addAll(getHexesInLine(from, Direction.RIGHT, blockedHexes));
		possibleHexesFromPosition.addAll(getHexesInLine(from, Direction.BOTTOM_RIGHT, blockedHexes));
		possibleHexesFromPosition.addAll(getHexesInLine(from, Direction.BOTTOM_LEFT, blockedHexes));
		possibleHexesFromPosition.addAll(getHexesInLine(from, Direction.LEFT, blockedHexes));

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

	public List<Hex> getHexesInLine(Hex from, Direction d, List<Hex> blockedHexes) {

		// The list to return
		List<Hex> possibleMoves = new ArrayList<>();
		// The line that is building it selves longer and longer
		List<Hex> currentLine = new ArrayList<>();
		// When currentLine gets the first piece, this will turn true
		boolean lineContainsAPiece = false; // No hex can jump without a piece
		// Add from to the current line
		currentLine.add(from);
		// The last index of the current line
		int last = 0;

		while(currentLine.get(last) != null) {

			Hex nextNeighbor = currentLine.get(last).neighbours[d.ordinal()];

		//	System.out.println("It is breaking out if nextNeighbor: " + nextNeighbor);

			// If it is was at the last neighbor break out!
			if(nextNeighbor == null) {
				break;
			}

			// Adding to the line and recalculating the last index
			currentLine.add(nextNeighbor);
			last = currentLine.size() - 1;

			// Sets lineContainsAPiece if the line now contains a piece
			if(!lineContainsAPiece && !nextNeighbor.isEmpty()) {
				lineContainsAPiece = true;
			}

			// Checks if nextNeighbor is a plausible hex to jump to
			if (nextNeighbor.isEmpty() && lineContainsAPiece) {
				// Breaks out if the position is blocked
				boolean isBlocked = false;
				for (Hex blocked : blockedHexes) {
					if (nextNeighbor == blocked) {
						isBlocked = true;
						break;
					}
				}

				// Checks symmetry if the line is not blocked
				if (!isBlocked) {


					int toMiddle = (last - 1) / 2;

					System.out.println("Inside: " + currentLine + ", toMiddle: " + toMiddle);

					boolean hasSymmetry = true;
					for(int i = 1; i <= toMiddle; i++) {

						if(currentLine.get(i).isEmpty() != currentLine.get(last - i).isEmpty()) {
							System.out.println(currentLine.get(i) + " ---><--- " + currentLine.get(last -i));
							hasSymmetry = false;
							break;
						}
					}
					if(hasSymmetry) {
						System.out.println("Found a possible move at: " + nextNeighbor.toString());
						possibleMoves.add(nextNeighbor);

					}
				}
			}
		}

		// System.out.println("At the end --> PossibleMoves.size(): " + possibleMoves.size() + " From: " + from + ", direction: " + d + ", blockedHexes.size(): " + blockedHexes.size());

		return possibleMoves;
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
