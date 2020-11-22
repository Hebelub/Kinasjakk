package UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import kinasjakk.Board;
import kinasjakk.Game;

public class SideBar extends JPanel {

	BoardPane boardPane;
	JButton button;
	JTextField inputMoveFrom = new JTextField("0");
	JTextField inputMoveTo = new JTextField("0");
	Game game;
	
	public SideBar(BoardPane boardPane) {
		this.boardPane = boardPane;
		button = new JButton("Make move");
		this.add(button);
		this.add(new Label("Move from: "));
		this.add(inputMoveFrom);
		this.add(new Label("Move to: "));
		this.add(inputMoveTo);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Board b = game.getBoard();
				/*System.out.println(b.getHexes().size());
				System.out.println(b.getHexes().get(0));
				System.out.println(b.getHexes().get(1));
				System.out.println(b.getHexes().get(2));*/

				int from = Integer.parseInt(inputMoveFrom.getText());
				int to = Integer.parseInt(inputMoveTo.getText());

			//	b.getHexes().get(2).getPiece().setPlayer(6);
				b.makeMove(b.getHexes().get(from), b.getHexes().get(to));

				boardPane.repaint();
			}
			
		});
	}
	
	public void setGame(Game game) {
		this.game = game;
	}
	
}
