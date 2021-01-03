package UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import kinasjakk.Game;
import kinasjakk.Hex;
import kinasjakk.HexJump;
import kinasjakk.HexMove;
import kinasjakk.Player;

public class SideBar extends JPanel {

	BoardPane boardPane;
	JButton button;
	JComboBox<ComboItem<HexMove>> inputMoveTo = new JComboBox<>();
	JComboBox<ComboItem<Hex>> inputMoveFrom = new JComboBox<>();
	JList<HexJump> listOfHexJumps;
	JScrollPane listScroller;
	JCheckBox shouldDrawArrowsCheckBox;
	JCheckBox shouldDrawPossibleMovesCheckBox;
	JCheckBox doEntireRoundOnMoveIfAllPlayersAI;
	Game game;
	
	JButton moveForward;
	JButton moveBackward;
	JLabel whoseTurn;
	JLabel moveCounter;
	JLabel primaryAI;
	
	public SideBar(BoardPane boardPane) {
		this.boardPane = boardPane;
		this.setLayout(new GridBagLayout());
		//History controls
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weighty = 0;
		c.gridy = 0;
		c.gridx = 0;
		c.ipady = 10;
		this.add(historyUI(), c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		this.add(new JSeparator(), c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		this.add(executeMoveUI(), c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		this.add(new JSeparator(), c);
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1.2;
		c.gridx = 0;
		c.gridy = 4;
		listOfHexJumps = new JList<HexJump>();
		listScroller = new JScrollPane(listOfHexJumps);
		listScroller.setMinimumSize(new Dimension(1000, 1000));
		this.add(listScroller, c);
		c.ipady = 0;
		c.weighty = 0;
		c.gridx = 0;
		c.weightx = 0.5;
		c.gridwidth = GridBagConstraints.BOTH;
		c.gridy = 5;
		shouldDrawArrowsCheckBox = new JCheckBox("Draw move arrow");
		shouldDrawArrowsCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					boardPane.setDrawArrow(true);
					boardPane.repaint();
				}else {
					boardPane.setDrawArrow(false);
					boardPane.repaint();
				}
			}
		});
		this.add(shouldDrawArrowsCheckBox, c);
		shouldDrawArrowsCheckBox.setSelected(true);
		c.gridx = 1;
		c.gridy = 5;
		shouldDrawPossibleMovesCheckBox = new JCheckBox("Highlight possibilities");
		shouldDrawPossibleMovesCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					boardPane.setDrawPossibleMoves(true);
					boardPane.repaint();
				}else {
					boardPane.setDrawPossibleMoves(false);
					boardPane.repaint();
				}
			}
		});
		this.add(shouldDrawPossibleMovesCheckBox, c);
		shouldDrawPossibleMovesCheckBox.setSelected(true);
		c.gridx = 0;
		c.gridy = 6;
		c.weightx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		doEntireRoundOnMoveIfAllPlayersAI = new JCheckBox("If all AI: Make AI move for all (one round)");
		this.add(doEntireRoundOnMoveIfAllPlayersAI, c);
		doEntireRoundOnMoveIfAllPlayersAI.setSelected(true);
		
		inputMoveFrom.addItemListener(new ItemListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					updateToHexes();
				}
			}
		});
		
		inputMoveTo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					updateHexJumpList();
					// Update Draw Arrow
					@SuppressWarnings("unchecked")
					ComboItem<HexMove> item = (ComboItem<HexMove>) inputMoveTo.getSelectedItem();
					boardPane.setCurrentMove((HexMove) item.getValue());
			        boardPane.repaint();
				}
			}
		});

		button.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent event) {
				ComboItem<HexMove> item = (ComboItem<HexMove>) inputMoveTo.getSelectedItem();
				HexMove move = item.getValue();
				game.doMove(move);
				updateAll();
			}
		});
	}
	
	public JPanel executeMoveUI() {
		JPanel executeMove = new JPanel();
		executeMove.add(new Label("Move from: "));
		executeMove.add(inputMoveFrom);
		executeMove.add(new Label("Move to: "));
		executeMove.add(inputMoveTo);
		button = new JButton("Make move");
		executeMove.add(button);
		JButton makeAIMove = new JButton("Make AI move");
		makeAIMove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (doEntireRoundOnMoveIfAllPlayersAI.isSelected()) {
					for(int i = 0; i < game.getPlayerCount(); i++) {
						game.doAIMove();
					}
				}else {
					game.doAIMove();
				}
				updateAll();
			}
			
		});
		executeMove.add(makeAIMove);
		primaryAI = new JLabel("");
		executeMove.add(primaryAI);
		return executeMove;
	}
	
	public JPanel historyUI() {
		JPanel history = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		JButton gotoStart = new JButton("<<");
		gotoStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				while(game.getMoveCounter() > 0) {
					game.goBackward();
				}
				updateAll();
			}
		});
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.01;
		c.gridx = 0;
		c.gridy = 0;
		history.add(gotoStart, c);
		moveBackward = new JButton("<");
		c.weightx = 0.45;
		c.gridx = 1;
		c.gridy = 0;
		history.add(moveBackward, c);
		moveBackward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				game.goBackward();
				updateAll();
			}
		});
		
		moveForward = new JButton(">");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.45;
		c.gridx = 2;
		c.gridy = 0;
		history.add(moveForward, c);
		JButton gotoEnd = new JButton(">>");
		c.weightx = 0.01;
		c.gridx = 3;
		c.gridy = 0;
		gotoEnd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				while(game.getMoveCounter() < game.getTotalMoves()) {
					game.goForward();
				}
				updateAll();
			}
		});
		history.add(gotoEnd, c);
		moveForward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				game.goForward();
				updateAll();
			}
		});

		whoseTurn = new JLabel("Turn: Player1");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.weightx = 0.25;
		c.gridy = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		history.add(whoseTurn, c);
		
		moveCounter = new JLabel("Move: 0/0");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.weightx = 0.25;
		c.gridy = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		history.add(moveCounter, c);
		
		return history;
	}
	
	public void setGame(Game game) {
		this.game = game;
		updateFromHexes();
	}
	
	public void updateFromHexes() {
		inputMoveFrom.removeAllItems();
		// Get all hexes with pieces belonging to current player
		List<Hex> hexes = game.getWhoseTurn().getPieceHexes();
		Collections.sort(hexes);
		for(Hex hex : hexes) {
			inputMoveFrom.addItem(new ComboItem<Hex>(Integer.toString(hex.id), hex));
		}
	}
	
	public void updateToHexes() {
		inputMoveTo.removeAllItems();
		@SuppressWarnings("unchecked")
		ComboItem<Hex> item = (ComboItem<Hex>) inputMoveFrom.getSelectedItem();
		Hex hex = item.getValue();
		List<HexMove> possibleMoves = hex.getPossibleMoves();
		Collections.sort(possibleMoves);
		boardPane.setPossibleMoves(possibleMoves);
		for(HexMove move : possibleMoves) {
			Hex end = move.getEndHex();
			inputMoveTo.addItem(new ComboItem<HexMove>(Integer.toString(end.id), move));
		}
	}
	
	public void updateWhoseTurn() {
		Player p = this.game.getWhoseTurn();
		Color c = p.getColor();
		String hex = String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());  
		whoseTurn.setText("<html>Turn: <font color='"+ hex +"'>●</font> "+this.game.getWhoseTurn().getName()+"</html>");
	}
	
	public void updateMoveCounter() {
		moveCounter.setText("Move: " + this.game.getMoveCounter() + "/" + this.game.getTotalMoves());
	}
	
	public void updateHexJumpList() {
		DefaultListModel<HexJump> model = new DefaultListModel<HexJump>();
		@SuppressWarnings("unchecked")
		ComboItem<HexMove> item = (ComboItem<HexMove>) inputMoveTo.getSelectedItem();
		if (item != null) {
			HexMove move = item.getValue();
			for(HexJump jump : move.getPath()) {
				model.addElement(jump);
			}
		}
		listOfHexJumps = new JList<HexJump>(model);
		/*listOfHexJumps.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
			}
		});*/
		listScroller.setViewportView(listOfHexJumps);
		listOfHexJumps.setSelectedIndex(0);
		listScroller.revalidate();
		listScroller.repaint();

	}
	
	public void updateAll() {
		primaryAI.setText(game.getPreferredAI().getName());
		updateFromHexes();
		updateToHexes();
		updateWhoseTurn();
		updateMoveCounter();
		updateHexJumpList();
		boardPane.repaint();
	}
}