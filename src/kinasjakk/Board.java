package kinasjakk;

import java.util.ArrayList;
import java.util.List;

public class Board {
	
	List<Hex> hexes;
	
	public Board() {
		hexes = new ArrayList<Hex>();
	}
	
	public void addHex(Hex hex) {
		hexes.add(hex);
	}

	public List<Hex> getHexes() {
		return hexes;
	}

	public List<Hex> getPossibleHexesFrom(Hex startHex) {

		// Set this hex to empty when calculating moves
		int index = hexes.indexOf(startHex);
		hexes.set(index, new Hex());

		// Get all the possible moves with jumping
		List<Hex> possibleMoves = startHex.possibleMovesInAllDirections(startHex, new ArrayList<Hex>());
		List<Hex> oneDistanceHexes = startHex.getOneDistanceHexes();
		List<Hex> uniqueOneDistanceHexes = new ArrayList<Hex>();
		for (Hex hex : oneDistanceHexes) {
			boolean isBlocked = false;
			for (Hex blocked : possibleMoves) {
				if (blocked == hex) {
					isBlocked = true;
					break;
				}
			}
			if(!isBlocked) {
				uniqueOneDistanceHexes.add(hex);
			}
		}
		possibleMoves.addAll(uniqueOneDistanceHexes);

		// Put the hex back in
		hexes.set(index, startHex);

		return possibleMoves;
	}
}
