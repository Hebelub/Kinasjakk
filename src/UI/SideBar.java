package UI;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.*;

import kinasjakk.*;

public class SideBar extends JPanel {

	BoardPane boardPane;

	JButton bestMoveButton;
	JButton moveButton;
	JComboBox<Integer> inputMoveFrom = new JComboBox<>();
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
		moveButton = new JButton("Make move");
		executeMove.add(moveButton);
		bestMoveButton = new JButton("Do Best Move");
		executeMove.add(bestMoveButton);

		this.add(history);
		this.add(new JSeparator());
		this.add(executeMove);
		this.add(new JSeparator());

		moveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				preformSelectedMove();
			}
		});

		bestMoveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				PreformRandomMove();
			}
		});

		inputMoveFrom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				updateMoveToComboBox();

			}
		});

		// updateMoveFromComboBox();
	}

	private void PreformRandomMove() {
		Random r = new Random();
		inputMoveFrom.setSelectedItem(inputMoveFrom.getItemAt(r.nextInt(inputMoveFrom.getItemCount())));
		inputMoveTo.setSelectedItem(inputMoveTo.getItemAt(r.nextInt(inputMoveTo.getItemCount())));
		preformSelectedMove();
	}
	public void preformSelectedMove() {
		Board board = game.getBoard();

		int from = (int)inputMoveFrom.getSelectedItem();
		int to = (int)inputMoveTo.getSelectedItem();

		board.makeMove(board.getHexes().get(from), board.getHexes().get(to));

		boardPane.repaint();

		updateMoveFromComboBox();
	}

	public void updateMoveFromComboBox() {
		// getting existing combo box model
		DefaultComboBoxModel modelFrom = (DefaultComboBoxModel) inputMoveFrom.getModel();
		// removing old data
		modelFrom.removeAllElements();
		// Adding the pieces of the new player
		List<Piece> pieces = game.getBoard().getPlayerToMove().getPieces();
		System.out.println("size of pieces: " + pieces.size());
		for(Piece piece : pieces) {
			modelFrom.addElement(piece.getHex().id);
		}

		updateMoveToComboBox();
	}

	public void updateMoveToComboBox() {

		if(inputMoveFrom.getSelectedItem() == null) return;

		Hex fromHex = game.getBoard().getHexes().get((int)inputMoveFrom.getSelectedItem());
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

	}

	public void setGame(Game game) {
		this.game = game;
	}
	
}
