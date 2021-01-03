package UI;

import kinasjakk.Game;
import kinasjakk.HexMove;

public class GameController {
	
	private Game game;
	private SideBar sideBar;
	private BoardPane boardPane;
	
	public GameController() {
		game = new Game();
		boardPane = new BoardPane();
		sideBar = new SideBar(boardPane);
	}
	
		
	
	
	public void makeMove(HexMove move) {
		game.doMove(move);
		sideBar.repaint();
		boardPane.repaint();
	}
	
	public Game getGame() {
		return game;
	}
	
}
