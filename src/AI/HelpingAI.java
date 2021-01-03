package AI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kinasjakk.Board;
import kinasjakk.Hex;
import kinasjakk.HexMove;
import kinasjakk.Player;

public class HelpingAI extends AI {
	
	public HelpingAI(Player player) {
		super(player, "HelpingAI");
	}
	
	@Override
	public HexMove nextMove(Board board) {
		return null;
	}
}
