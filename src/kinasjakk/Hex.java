package kinasjakk;

import java.util.Arrays;

public class Hex {

	Hex[] neighbours;
	
	public Hex() {
		neighbours = new Hex[6];
	}
	
	public void setNeighbour(Direction dir,  Hex otherHex) {
		neighbours[dir.ordinal()] = otherHex;
	}

	@Override
	public String toString() {
		return "Hex [neighbours=" + Arrays.toString(neighbours) + "]";
	}
	
	
}
