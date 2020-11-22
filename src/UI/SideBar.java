package UI;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;

import kinasjakk.Board;
import kinasjakk.Game;
import kinasjakk.Hex;

public class SideBar extends JPanel {

	BoardPane boardPane;
	JButton button;
	JTextField inputMoveFrom = new JTextField("0", 5);
	JTextField inputMoveTo = new JTextField("0", 5);
	JTextArea possibleMoves = new JTextArea("Possible Moves");
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
		executeMove.add(possibleMoves);

		this.add(history);
		this.add(new JSeparator());
		this.add(executeMove);
		this.add(new JSeparator());

		inputMoveFrom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				Hex fromHex = game.getBoard().getHexes().get(Integer.parseInt(inputMoveFrom.getText()));
				List<Hex> possibleHexes = game.getBoard().getPossibleHexesFrom(fromHex);

				String debug = "";
				for(Hex hex : possibleHexes) {
					debug += hex.id + " ";
				}

				System.out.println(debug);
			}
		});

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Board b = game.getBoard();

				int from = Integer.parseInt(inputMoveFrom.getText());
				int to = Integer.parseInt(inputMoveTo.getText());
				inputMoveFrom.setText("");
				inputMoveTo.setText("");

				b.makeMove(b.getHexes().get(from), b.getHexes().get(to));

				boardPane.repaint();
			}
		});
	}
	
	public void setGame(Game game) {
		this.game = game;
	}
	
}
