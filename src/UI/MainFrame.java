package UI;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;

import kinasjakk.Game;

public class MainFrame {
	
	JFrame frame;
	BoardPane boardPane;
	SideBar sideBar;
	Game currentGame;
	
	public MainFrame() {
		frame = new JFrame();
		frame.setSize(new Dimension(1000, 500));
		frame.setLayout(new GridLayout(1, 2));
		boardPane = new BoardPane();
		sideBar = new SideBar();
        frame.add(boardPane);
        frame.add(sideBar);
        frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startNewGame();
	}
	
	public void startNewGame() {
		currentGame = new Game();
		boardPane.setBoard(currentGame.getBoard());
		sideBar.setGame(currentGame);
	}
}
