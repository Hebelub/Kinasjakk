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
}
