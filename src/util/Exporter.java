package util;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import AI.MinimaxAI_Dist_Goal;
import AI.RegisteredAI;
import kinasjakk.Game;
import kinasjakk.Hex;
import kinasjakk.Player;

/*
 	TODO
 	- Legg til class aka hvem som vant spiller på slutten av hver linje 
 */

public class Exporter {

	final int DEPTH = 2;
	
	enum HexState { EMPTY, PIECE }
	
	class GameState {
		public List<List<HexState>> playerStates;
		public GameState(Game game) {
			playerStates = new ArrayList<>();
			for(int i = 0; i < game.getPlayerCount(); i++) {
				playerStates.add(new ArrayList<HexState>());
			}
		}
		public void append() {
			for(List<HexState> state : playerStates) 
				state.add(HexState.EMPTY);
		}
		public void append(int id) {
			for(int i = 0; i < playerStates.size(); i++) {
				List<HexState> state = playerStates.get(i);
				if (i == id - 1) state.add(HexState.PIECE);
				else state.add(HexState.EMPTY);
			}
		}		
	}
	
	public Exporter() {
		
	}
	
	public void exportDataset()  {
		int numPlayers = 2;
		Game game = setupNewGame(numPlayers);
		
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("dataset.txt", "UTF-8");
		} catch (Exception e) {e.printStackTrace();
		} finally { if (writer == null) return; }
		
		
		
		while(!game.allFinished()) {
			game.doAIMove();
			writeGameState(game, writer);
			
			if (game.getTotalMoves() % 2 == 0)
				System.out.println("Move " + game.getTotalMoves());
		}
		
		writer.close();
	}
	
	public Game setupNewGame(int numPlayers) {
		Game game = new Game();
		game.newGame(numPlayers);
		List<Player> players = game.getPlayers();
		for (Player p : players) {
			p.setAIPlayer(RegisteredAI.giveInstance("MinimaxAI_2deep", p));
		}
		return game;
	}
	
	public void writeGameState(Game game, PrintWriter writer) {
		GameState state = getGameState(game);
		for (int i = 0; i < state.playerStates.size(); i++) {
			List<HexState> pState = state.playerStates.get(i);
			for (int j = 0; j < pState.size(); j++) {
				HexState s = pState.get(j);
				writer.write(""+s.ordinal());
				if (i != state.playerStates.size() - 1 || j != pState.size() - 1)
					writer.write(" ");
			}
		}
		writer.write("\n");
	}
	
	public GameState getGameState(Game game) {
		GameState state = new GameState(game);
		for(Hex hex : game.getBoard().getHexes()) {
			if (hex.isEmpty()) state.append();
			else {
				Player p = hex.getPlayer();
				state.append(p.getID());
			}
		}
		return state;
	}
	
}
