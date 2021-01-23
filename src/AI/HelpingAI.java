package AI;

import kinasjakk.Board;
import kinasjakk.Game;
import kinasjakk.HexMove;
import kinasjakk.Player;

public class HelpingAI extends AI {
	
	public HelpingAI(Player player) {
		super(player, "HelpingAI");
	}
	
	@Override
	public HexMove nextMove(Board board, Game game) {
		return null;
	}
}
