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

	Board board;

	public Hex(Board atBoard) {
		board = atBoard;
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

	public Hex getNeighbor(Direction d) {
		return neighbours[d.ordinal()];
	}
	public Hex getNeighbor(int d) {
		return neighbours[d];
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
		if(piece != null) {
			piece.setHex(this);
		}
	}

	public boolean isAtGoal(Player from, Hex check) {
		Player oppositePlayer = from.getOppositePlayer();
		return board.getStartHexesOfPlayer(oppositePlayer).contains(check);
	}

	public class Moves {

		List<Hex> possibleHexes;
		List<Hex> hexesToTest;

		Moves() {
			possibleHexes = new ArrayList<>();
			hexesToTest = new ArrayList<>();
		}

		private void addHexToLists() {
			possibleHexes.add(Hex.this);
			hexesToTest.add(Hex.this); // This is removed at the end
		}

		public List<Hex> getPossibleHexes() {

			Piece piece = getPiece();
			setPiece(null); // Temporarily removing piece

			addHexToLists();

			addJumpHexes();
			addOneDistanceHexes();

			possibleHexes.remove(0); // Removes Hex.this from List

			setPiece(piece);

			return possibleHexes;
		}

		private void addJumpHexes() {
			while(hexesToTest.size() > 0) {
				hexesToTest = getAvailableHexesInAllDirectionsFromHexes();
				possibleHexes.addAll(hexesToTest);
			}
		}

		private List<Hex> getAvailableHexesInAllDirectionsFromHexes() {
			List<Hex> hexesInAllDirections = new ArrayList<>();
			for(Hex hex : hexesToTest) {
				hexesInAllDirections.addAll(getAvailableHexesInAllDirectionsFrom(hex));
			}
			return hexesInAllDirections;
		}

		private List<Hex> getAvailableHexesInAllDirectionsFrom(Hex from) {
			List<Hex> possibleHexesFromPosition = new ArrayList<>();

			possibleHexesFromPosition.addAll(getAvailableHexesInLineFrom(new PointingHex(from, Direction.TOP_LEFT)));
			possibleHexesFromPosition.addAll(getAvailableHexesInLineFrom(new PointingHex(from, Direction.TOP_RIGHT)));
			possibleHexesFromPosition.addAll(getAvailableHexesInLineFrom(new PointingHex(from, Direction.RIGHT)));
			possibleHexesFromPosition.addAll(getAvailableHexesInLineFrom(new PointingHex(from, Direction.BOTTOM_RIGHT)));
			possibleHexesFromPosition.addAll(getAvailableHexesInLineFrom(new PointingHex(from, Direction.BOTTOM_LEFT)));
			possibleHexesFromPosition.addAll(getAvailableHexesInLineFrom(new PointingHex(from, Direction.LEFT)));

			return possibleHexesFromPosition;
		}

		public List<Hex> getAvailableHexesInLineFrom(PointingHex from) {

			List<Hex> hexLine = from.getHexLine();

			List<Hex> possibleMoves = new ArrayList<>();

			boolean hasIteratedOverAPiece = false; // No hex can jump without at least one piece between

			int checkUntil = (hexLine.size() + 1) / 2;

			for(int end = 1; end < checkUntil; end++) {

				Hex endHex = hexLine.get(end);

				if(hasIteratedOverAPiece) {
					if(endHex.isEmpty() && !isHexInBlockedList(endHex)) {
						if(LineSegmentHasSymmetry(hexLine, end)) {
							possibleMoves.add(endHex);
							end += end - 1; // Skipping impossible iterations
						}
					}
				}
				else if(endHex.hasPiece()) {
					checkUntil = hexLine.size();
					end += end - 1; // Skipping impossible iterations
					hasIteratedOverAPiece = true;
				}
			}

			return possibleMoves;
		}

		public void addOneDistanceHexes() {

			List<Hex> oneDistanceHexes = new ArrayList<>();

			for (Hex hex : neighbours) {
				if (hex != null && hex.isEmpty() && !isHexInBlockedList(hex)) {
					oneDistanceHexes.add(hex);
				}
			}

			possibleHexes.addAll(oneDistanceHexes);
		}

		private boolean isHexInBlockedList(Hex hex) {
			for (Hex blocked : possibleHexes) {
				if (hex == blocked) {
					return true;
				}
			}
			return false;
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

		public class PointingHex {

			private Hex hex;

			private Direction direction;

			PointingHex(Hex hex, Direction direction) {
				this.hex = hex;
				this.direction = direction;
			}

			public Hex getNextHex() {
				return hex.getNeighbor(direction);
			}

			public PointingHex getNextPointingHex() {
				return new PointingHex(getNextHex(), direction);
			}

			public List<Hex> getHexLine() {
				List<Hex> line = new ArrayList<>();
				line.add(hex);

				while(true) {
					Hex nextHex = line.get(line.size() - 1).getNeighbor(direction);
					if(nextHex != null)
						line.add(nextHex);
					else break;
				}

				return line;
			}

			public Hex getHex() {
				return hex;
			}

			public void setHex(Hex hex) {
				this.hex = hex;
			}

			public Direction getDirection() {
				return direction;
			}

			public void setDirection(Direction direction) {
				this.direction = direction;
			}
		}
	}
}
