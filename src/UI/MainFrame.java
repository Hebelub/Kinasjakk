package UI;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import AI.AI;
import AI.RegisteredAI;
import kinasjakk.Game;
import kinasjakk.Player;

public class MainFrame {
	
	JFrame frame;
	BoardPane boardPane;
	SideBar sideBar;
	Game currentGame;
	JMenuBar menuBar;
	PeopleWindow peopleWindow;
	
	public MainFrame() {
		frame = new JFrame();
		frame.setSize(new Dimension(1000, 500));
		frame.setLayout(new GridLayout(1, 2));
		peopleWindow = new PeopleWindow();
		boardPane = new BoardPane();
		currentGame = new Game();
		boardPane.setGame(currentGame);
		sideBar = new SideBar(boardPane);
		sideBar.setGame(currentGame);
        frame.add(boardPane);
        frame.add(sideBar);
        frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(createMenuBar());
		
		peopleWindow.getFrame().addComponentListener(new ComponentAdapter() {
			   public void componentHidden(ComponentEvent e) {
				   sideBar.updateAll();
			   }
			   public void componentShown(ComponentEvent e) {
			      /* code run when component shown */
			   }
		});
		
		sideBar.updateAll();
	}
	
	private JMenuBar createMenuBar() {
        menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createPlayerMenu());
        menuBar.add(createAIMenu());
        return menuBar;
    }
	
	private JMenu createAIMenu() {
		JMenuItem allAI = new JMenuItem("Make all players primary AI (" + currentGame.getPreferredAI().getName() + ")");
		JMenu aiMenu = new JMenu("AI");
		JMenu aiSubMenu = new JMenu("Set primary AI");
		String[] ais = RegisteredAI.getNames();
		ButtonGroup group = new ButtonGroup();
		for (int i = 0; i < ais.length; i++) {
			String name = ais[i];
			JRadioButtonMenuItem radioItem = new JRadioButtonMenuItem(name, currentGame.getPreferredAI().getName().equals(name));
			radioItem.setActionCommand(name);
			radioItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					currentGame.setPreferredAI(RegisteredAI.giveInstance(event.getActionCommand(), null));
					allAI.setText("Make all players primary AI (" + currentGame.getPreferredAI().getName() + ")");
					sideBar.updateAll();
				}
			});
			aiSubMenu.add(radioItem);
			group.add(radioItem);
		}
		aiMenu.add(aiSubMenu);
		aiMenu.add(allAI);
		allAI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
            	//Make all player into AIs
            	List<Player> players = currentGame.getPlayers();
            	for(int i = 0; i < players.size(); i++) {
            		AI ai = RegisteredAI.giveInstance(currentGame.getPreferredAI().getName(), players.get(i));
            		players.get(i).setAIPlayer(ai);
            	}
            }
        });
		return aiMenu;
	}
	
	private JMenu createPlayerMenu() {
		JMenu playerMenu = new JMenu("Players");
		
		//Manage players
		JMenuItem manageItem = new JMenuItem("Edit...");
		manageItem.setAccelerator(KeyStroke.getKeyStroke('P', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
		playerMenu.add(manageItem);
		manageItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
            	peopleWindow.open(currentGame.getPlayers());
            }
        });
		JMenuItem herdVsMinimax = new JMenuItem("HerdAI vs MinimaxAI");
		playerMenu.add(herdVsMinimax);
		herdVsMinimax.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
            	currentGame.newGame(2);
            	List<Player> players = currentGame.getPlayers();
            	for(int i = 0; i < 2; i++) {
            		if (i == 0) players.get(i).setAIPlayer(RegisteredAI.giveInstance("HerdAI", players.get(i)));
            		else players.get(i).setAIPlayer(RegisteredAI.giveInstance("MinimaxAI", players.get(i)));
            	}
            	sideBar.updateAll();
            }
        });
		for(int i = 2; i <= 6; i+= 2) {
			JMenuItem item = new JMenuItem("Setup " + Integer.toString(i) + " players");
			playerMenu.add(item);
			final int final_i = i;
			item.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent ev) {
	            	//Make new game with i number of players
	            	currentGame.newGame(final_i);
	            	sideBar.updateAll();
	            }
	        });
		}
		return playerMenu;
	}
	
	private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");
        //New Game
        JMenuItem newItem = new JMenuItem("New Game");
        newItem.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
        fileMenu.add(newItem);
        newItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                    currentGame = new Game();
                    sideBar.setGame(currentGame);
                    boardPane.setGame(currentGame);
                    sideBar.updateAll();
            }
        });
        //Open Game
        JMenuItem openItem = new JMenuItem("Open Game...");
        openItem.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
        openItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
            	
            	final JFrame iFRAME = new JFrame();
            	iFRAME.setAlwaysOnTop(true);    // ****
            	iFRAME.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            	iFRAME.setLocationRelativeTo(null);
            	iFRAME.requestFocus();
            	
            	JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            	jfc.setCurrentDirectory(new File(System.getProperty("user.dir") + "/boards/"));
            	
            	FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files (.txt)", "txt");
            	jfc.setFileFilter(filter);
            	
            	int returnValue = jfc.showOpenDialog(iFRAME);
            	iFRAME.dispose();
            	if (returnValue == JFileChooser.APPROVE_OPTION) {
            		File selectedFile = jfc.getSelectedFile();
            		currentGame.load(selectedFile);
            		sideBar.setGame(currentGame);
            		boardPane.setGame(currentGame);
            		sideBar.updateAll();
            	}
            	
            }
        });
        fileMenu.add(openItem);
        //Save Game
        JMenuItem saveItem = new JMenuItem("Save Game As...");
        saveItem.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));

        fileMenu.add(saveItem);
        //Exit Program
        JMenuItem exitItem = new JMenuItem("Quit");
        exitItem.setAccelerator(KeyStroke.getKeyStroke('Q', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
        fileMenu.add(exitItem);
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                    System.exit(0);
            }
        });
        return fileMenu;
    }
	
	
}
