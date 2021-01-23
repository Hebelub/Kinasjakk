package kinasjakk;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import AI.RegisteredAI;

public class Board  {
	
	private List<Hex> hexes;
	
	public Board(List<Hex> hexes) {
		this.hexes = hexes;
	}

	public List<Hex> getHexes() {
		return hexes;
	}

	public List<Hex> getPossibleHexesFrom(Hex startHex) {
		return startHex.getPossibleHexes();
	}
	
	public List<HexMove> getPossibleMovesFrom(Hex startHex) {
		return startHex.getPossibleMoves();
	}
	
 	public List<HexMove> getPossibleMovesFrom(Player player) {
 		List<HexMove> moves = new ArrayList<HexMove>();
 		for(Hex hex : player.getPieceHexes()) {
 			moves.addAll(getPossibleMovesFrom(hex));
 		}
 		return moves;
 	}

	/**
	 * Make move by giving start and end hex
	 * @param start Hex containing piece to move
	 * @param end Hex where piece will end up
	 */
	public void makeMove(Hex start, Hex end) {
		Piece pieceToMove = start.getPiece();
		start.setPiece(end.getPiece());
		end.setPiece(pieceToMove);	
	}
	
	/**
	 * Make move by giving a HexMove object with valid HexJumps
	 * @param move
	 */
	public void makeMove(HexMove move) {
		makeMove(move.getStartHex(), move.getEndHex());
	}
	
	public void undoMove(HexMove move) {
		makeMove(move.getEndHex(), move.getStartHex());
	}
	
//	public Board makeCopy() {
//		List<Hex> newHexes = new ArrayList<>();
//		List<Player> newPlayers = new ArrayList<>();		 
//		boolean[] checked = {false, false, false, false, false, false};
//		
//		//Create players
//		for(int i = 0; i < 6; i++) newPlayers.add(new Player(i+1));
//		newPlayers.get(0).setOpponent(newPlayers.get(1));
//		newPlayers.get(2).setOpponent(newPlayers.get(3));
//		newPlayers.get(4).setOpponent(newPlayers.get(5));
//		
//		for(Hex hex : hexes) {
//			Hex newHex = new Hex(hex.id);
//			
//			Direction[] dirs = {Direction.TOP_LEFT, Direction.TOP_RIGHT, Direction.LEFT};
//			for(int i = 0; i < dirs.length; i++) {
//				Direction d = dirs[i];
//				Hex n = hex.getNeighbor(d);
//				if (n == null) continue;
//				if (newHexes.size() >= n.id) {
//					newHex.setNeighbour(d, newHexes.get(n.id));
//				}
//			}		
//			
//			//Check if old hex has piece
//			if (hex.hasPiece()) {
//				int pid = hex.getPiece().getPlayerID();
//				Player p = newPlayers.get(pid - 1);
//				if (!checked[pid - 1]) {
//					Player oldPlayer = hex.getPlayer();
//					p.setColor(oldPlayer.getColor());
//					p.setName(p.getName());
//					if (oldPlayer.isHumanPlayer()) {
//						p.setHumanPlayer();
//					}else {
//						p.setAIPlayer(RegisteredAI.giveInstance(oldPlayer.getAI().getName(), p));
//					}
//					checked[pid - 1] = true;
//				}
//				Piece newPiece = new Piece(p);
//				newHex.setPiece(newPiece);
//			}
//			newHexes.add(newHex);
//		}
//		Board newBoard = new Board(newHexes);
//		return newBoard;
//		
//	}
	
//	private Player getPlayerWithIdInList(int id, List<Player> players) {
//		for(Player player : players) {
//			if (player.getID() == id) return player;
//		}
//		return null;
//	}
//	
//	private boolean playerWithIdInList(int id, List<Player> players) {
//		if (players.size() == 0) return false;
//		for(Player player : players) {
//			if (player.getID() == id) return true;
//		}
//		return false;
//	}
}
