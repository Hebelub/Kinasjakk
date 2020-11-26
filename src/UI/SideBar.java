package UI;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import kinasjakk.Board;
import kinasjakk.Game;
import kinasjakk.Hex;

public class SideBar extends JPanel {

	BoardPane boardPane;
	JButton button;
	JTextField inputMoveFrom = new JTextField("0", 5);
	JComboBox<Integer> inputMoveTo = new JComboBox<>();
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

		// Listen for changes in the text
		inputMoveFrom.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) { warn(); }
			public void removeUpdate(DocumentEvent e) { warn();	}
			public void insertUpdate(DocumentEvent e) { warn();	}

			public void warn() {
				try {
					Hex fromHex = game.getBoard().getHexes().get(Integer.parseInt(inputMoveFrom.getText()));
					List<Hex> possibleHexes = game.getBoard().getPossibleHexesFrom(fromHex);
					Collections.sort(possibleHexes);

					// getting existing combo box model
					DefaultComboBoxModel model = (DefaultComboBoxModel) inputMoveTo.getModel();
					// removing old data
					model.removeAllElements();

					for(Hex hex : possibleHexes) {
						model.addElement(hex.id);
					}

					inputMoveTo.setModel(model);

				} catch (Exception e) { }

			}
		});

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Board b = game.getBoard();

				int from = Integer.parseInt(inputMoveFrom.getText());
				int to = (int)inputMoveTo.getSelectedItem();
				inputMoveFrom.setText("");

				// getting existing combo box model
				DefaultComboBoxModel model = (DefaultComboBoxModel) inputMoveTo.getModel();
				// removing old data
				model.removeAllElements();

				b.makeMove(b.getHexes().get(from), b.getHexes().get(to));

				boardPane.repaint();
			}
		});
	}
	
	public void setGame(Game game) {
		this.game = game;
	}
	
}
