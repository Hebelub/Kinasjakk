package kinasjakk;

import java.util.*;
import java.util.List;

public class Hex {
	private static int nextId = 0;
	public int id;
	private Piece piece;

	Hex[] neighbours;

	public Hex() {
		id = nextId++;
		neighbours = new Hex[6];
	}

	public void setNeighbour(Direction dir,  Hex otherHex) {
		neighbours[dir.ordinal()] = otherHex;
	}

	@Override
	public String toString() {
		return "Hex: " + id + ", [neighbours=" + Arrays.toString(neighbours) + "]";
	}

	public Hex[] getPossibleMoves() {

		getHexesInLine(Direction.TOP_LEFT);
		getHexesInLine(Direction.TOP_RIGHT);
		getHexesInLine(Direction.RIGHT);
		getHexesInLine(Direction.BOTTOM_RIGHT);
		getHexesInLine(Direction.BOTTOM_LEFT);
		getHexesInLine(Direction.LEFT);

		return null;
	}

	public List<Hex> getHexesInLine(Direction d) {

		List<Hex> possibleMoves = new ArrayList<Hex>();

		List<Hex> currentLine = new ArrayList<Hex>();

		int last = 0;

		while(currentLine.get(last) != null) {

			Hex neighbor = neighbours[d.ordinal()];
			currentLine.add(neighbor);

			if (neighbor.isEmpty()) {

				for(int i = 1; i < Math.floor((last - 2) / 2); i++) {

					if(currentLine.get(i) != currentLine.get(last - i)) {

						break;
					}
					else if(i == Math.floor((last - 2) / 2)) {
						possibleMoves.add(currentLine.get(last));
					}
				}

				last++;
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
