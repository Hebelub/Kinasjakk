package kinasjakk;

import java.util.ArrayList;
import java.util.List;

// All HexJumps made by a piece, 
// i.e. a player's turn.
// immutable
public class HexMove implements Comparable<HexMove> {
	
	private Player player;
	private Piece piece;
	private List<HexJump> path;
	
    HexMove(List<HexJump> path, Piece piece) {
    	this.path = path;    	
    	HexJump firstJump = path.get(0);
    	this.piece = piece;
    }
    
    public Piece getPiece() {
    	return getStartHex().getPiece();
    }
    
    public List<HexJump> getPath() {
    	return path;
    }
    
    public Hex getStartHex() {
    	return path.get(0).getStartHex();
    }
    
    public Hex getEndHex() {
    	return path.get(path.size() - 1).getEndHex();
    }
    
    public List<Hex> getHexes() {
    	List<Hex> hexes = new ArrayList<Hex>();
    	hexes.add(getStartHex());
    	for(HexJump jump : path) {
        	hexes.add(jump.getEndHex());
    	}
    	return hexes;
    }
    
    public String toString() {
    	StringBuilder s = new StringBuilder();
    	for (HexJump jump : path) {
    		System.out.print(jump + ", ");
    	}
    	/*List<Hex> hexes = getHexes();
		for(int i = 0; i < hexes.size(); i++) {
			s.append(hexes.get(i).id);
			if (i < hexes.size() - 1) s.append(" => ");
		}*/
		return s.toString();
	}
    
    // Check if another HexMove starts and ends in the same spot as this
    public boolean isEquivalentTo(HexMove other) {
    	return getStartHex().id == other.getStartHex().id && getEndHex().id == other.getEndHex().id;
    }

	@Override
	public int compareTo(HexMove other) {
		return this.getEndHex().id - other.getEndHex().id;
	}
}
