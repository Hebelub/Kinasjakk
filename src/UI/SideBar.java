package UI;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import kinasjakk.Board;
import kinasjakk.Game;

public class SideBar extends JPanel {

	BoardPane boardPane;
	JButton button;
	JTextField inputMoveFrom = new JTextField("0", 5);
	JTextField inputMoveTo = new JTextField("0", 5);
	Game game;
	
	public SideBar(BoardPane boardPane) {
		this.setLayout(new GridLayout(4, 1));
		//History controls
		JPanel history = new JPanel();
		history.add(new JButton("<"));
		history.add(new JLabel("Move: 0"));
		history.add(new JButton(">"));
		
		//Execute move
		JPanel executeMove = new JPanel();
		this.boardPane = boardPane;
		executeMove.add(new Label("Move from: "));
		executeMove.add(inputMoveFrom);
		executeMove.add(new Label("Move to: "));
		executeMove.add(inputMoveTo);
		button = new JButton("Make move");
		executeMove.add(button);
		
		this.add(history);
		this.add(new JSeparator());
		this.add(executeMove);
		this.add(new JSeparator());
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
