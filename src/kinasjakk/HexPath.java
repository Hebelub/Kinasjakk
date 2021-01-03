package kinasjakk;

import java.util.ArrayList;
import java.util.List;

// A mutable HexJump path
// i.e. can add jumps
// finally turn into HexMove
public class HexPath {
	
	private Piece piece;
	private List<HexJump> path;
	
    HexPath(HexJump jump, Piece piece) {
    	path = new ArrayList<HexJump>();
    	addJump(jump);
    	this.piece = piece;
    }
    
    public void addJump(HexJump jump) {
    	path.add(jump);
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
    
    public HexPath copy() {
    	HexPath copy = new HexPath(this.path.get(0), this.piece);
    	for(int i = 1; i < path.size(); i++) {
    		HexJump jump = this.path.get(i);
    		copy.addJump(jump);
    	}
		return copy;
    }
    
    // Turns HexPath into a HexMove, i.e. a finished path that cannot be altered
    public HexMove makeMove() {
    	List<HexJump> paths = new ArrayList<HexJump>();
    	for (HexJump jump : path) {
    		paths.add(new HexJump(jump.getStartHex(), jump.getEndHex()));
    	}
    	return new HexMove(paths, piece);
    }
}
