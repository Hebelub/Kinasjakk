package UI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import AI.GreedyAI;
import AI.HerdAI;
import AI.RandomAI;
import kinasjakk.HexMove;
import kinasjakk.Player;

public class PeopleWindow {
	JFrame frame;
	private List<Player> players;
	JComboBox<ComboItem<Player>> selectPlayer;
	
	public PeopleWindow() {
		players = new ArrayList<Player>();
		frame = new JFrame();
		frame.setSize(new Dimension(300, 180));
		frame.setLayout(new GridBagLayout());
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		selectPlayer = new JComboBox<>();
		selectPlayer.addItemListener(new ItemListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					ComboItem<Player> item = (ComboItem<Player>) event.getItem();
					Player player = item.getValue();
					frame.getContentPane().removeAll();
					JPanel panel = createPlayerForm(player);
					GridBagConstraints c = new GridBagConstraints();
					c.fill = GridBagConstraints.HORIZONTAL;
					c.gridwidth = GridBagConstraints.REMAINDER;
					c.weightx = 1;
					c.gridx = 0;
					c.gridy = 0;
					frame.add(selectPlayer, c);
					c.gridx = 0;
					c.gridy = 1;
					frame.add(panel, c);
					c.gridx = 0;
					c.gridy = 2;
					JButton button = new JButton("Close");
					button.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent event) {
							frame.dispose();
						}
					});
					frame.add(button, c);
					frame.validate();
					frame.repaint();
				}
			}
		});
		frame.add(selectPlayer);
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	public void open(List<Player> players) {
		this.players = players;
		updatePlayerList();
		frame.setVisible(true);
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	
	private void updatePlayerList() {
		selectPlayer.removeAllItems();
		for(Player player : players) {
			selectPlayer.addItem(new ComboItem<Player>(player.getName(), player));
		}
	}
	
	private JPanel createPlayerForm(Player player) {
		JPanel p = new JPanel(new SpringLayout());
		JTextField playerName = new JTextField(player.getName());
		JLabel playerNameLabel = new JLabel("Name: ");
		playerNameLabel.setLabelFor(playerName);
		p.add(playerNameLabel);
		p.add(playerName);
		 
		JComboBox<String> playerType = new JComboBox<>();
		playerType.addItem("Human");
		playerType.addItem("GreedyAI");
		playerType.addItem("HerdAI");
		playerType.addItem("RandomAI");
		if (player.isHumanPlayer()) playerType.setSelectedIndex(0);
		else if (player.getAI() instanceof GreedyAI) playerType.setSelectedIndex(1);
		JLabel playerTypeLabel = new JLabel("Brain: ");
		playerTypeLabel.setLabelFor(playerType);
		p.add(playerTypeLabel);
		p.add(playerType);
		 
		p.add(new JLabel());
		 
		JButton button = new JButton("Save Changes");
		p.add(button);
		 
		button.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent event) {
				ComboItem<Player> playerItem = (ComboItem<Player>) selectPlayer.getSelectedItem();
				String type = (String) playerType.getSelectedItem();
				Player p = playerItem.getValue();
				if (type.equals("Human")) {
					p.setHumanPlayer();
				}else if (type.equals("GreedyAI")) {
					p.setAIPlayer(new GreedyAI(p));
				}else if (type.equals("HerdAI")) {
					p.setAIPlayer(new HerdAI(p));
				}else if (type.equals("RandomAI")) {
					p.setAIPlayer(new RandomAI(p));
				}
				p.setName(playerName.getText().trim());
				updatePlayerList();
			}
		});
		 
		SpringUtilities.makeCompactGrid(p,
                 3, 2,	//rows, cols
                 7, 7,	//initX, initY
                 7, 7);	//xPad, yPad
		
		return p;
	}
}
