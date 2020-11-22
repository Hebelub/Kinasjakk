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
		return "Hex: " + id + ", [neighbours=" + Arrays.toString(ids) + "]";
	}

	public List<Hex> possibleMovesInAllDirections(Hex currentJumpHex, List<Hex> possibleHexes) {
		// Create a list off all possible hexes with one move
		List<Hex> oneDepthHexes = new ArrayList<>();
		oneDepthHexes.addAll(getHexesInLine(currentJumpHex, Direction.TOP_LEFT, possibleHexes));
		oneDepthHexes.addAll(getHexesInLine(currentJumpHex, Direction.TOP_RIGHT, possibleHexes));
		oneDepthHexes.addAll(getHexesInLine(currentJumpHex, Direction.RIGHT, possibleHexes));
		oneDepthHexes.addAll(getHexesInLine(currentJumpHex, Direction.BOTTOM_RIGHT, possibleHexes));
		oneDepthHexes.addAll(getHexesInLine(currentJumpHex, Direction.BOTTOM_LEFT, possibleHexes));
		oneDepthHexes.addAll(getHexesInLine(currentJumpHex, Direction.LEFT, possibleHexes));

		//
		if(oneDepthHexes.size() == 0) return possibleHexes;
		oneDepthHexes.addAll(possibleHexes);

		//
		List<Hex> returnHex = new ArrayList<>();

		for (Hex hex : oneDepthHexes) {
			returnHex.addAll(possibleMovesInAllDirections(hex, oneDepthHexes));
		}

		return returnHex;

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

		List<Hex> possibleMoves = new ArrayList<>();
		List<Hex> currentLine = new ArrayList<>();
		boolean lineContainsAPiece = false;
		int last = 0;

		// Add from to the current line
		currentLine.add(from);

		while(currentLine.get(last) != null) {
			last = currentLine.size() - 1;

			Hex neighbor = currentLine.get(last).neighbours[d.ordinal()];

			System.out.println(neighbor + ", " + d + ", " + blockedHexes);

			if(neighbor == null) break;

			currentLine.add(neighbor);
			if(!lineContainsAPiece && !neighbor.isEmpty()) {
				lineContainsAPiece = true;
			}

			if (neighbor.isEmpty() && lineContainsAPiece) {

				boolean isBlocked = false;
				for (Hex hex : blockedHexes) {
					if (this == hex) {
						isBlocked = true;
						break;
					}
				}

				if (!isBlocked) {

					for(int i = 1; i < Math.floor((last - 2) / 2); i++) {

						if(currentLine.get(i) != currentLine.get(last - i)) {

							break;
						}
						else if(i == Math.floor((last - 2) / 2)) {
							possibleMoves.add(currentLine.get(last));
						}
					}
				}
			}
		}

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
