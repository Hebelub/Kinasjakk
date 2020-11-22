package UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import kinasjakk.Board;
import kinasjakk.Game;

public class SideBar extends JPanel {
	
	JButton button;
	Game game;
	
	public SideBar() {
		button = new JButton("Make move");
		this.add(button);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Board b = game.getBoard();
				System.out.println(b.getHexes().size());
				System.out.println(b.getHexes().get(0));
				System.out.println(b.getHexes().get(1));
				System.out.println(b.getHexes().get(2));
				b.getHexes().get(2).getPiece().setPlayer(6);
				b.makeMove(b.getHexes().get(0), b.getHexes().get(2));
			}
			
		});
	}
	
	public void setGame(Game game) {
		this.game = game;
	}
	
}
