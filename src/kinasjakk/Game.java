package kinasjakk;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import AI.AI;
import AI.GreedyAI;
import AI.RegisteredAI;

public class Game {
	private List<Player> players;
	private Board board;
	private List<HexMove> history;
	private int currentMove = -1;
	private Player whoseTurn;
	private AI findBestMoveAI;
	private boolean gameFinished = false;
	
	private List<Integer> turnIndexes;
	
	File lastGameLoaded;
	static final String saveFileExtension = ".txt";

	public Game() {
		players = new ArrayList<Player>();
		history = new ArrayList<HexMove>();
		findBestMoveAI = RegisteredAI.giveInstance(RegisteredAI.getNames()[0], null);
		turnIndexes = new ArrayList<>();
		turnIndexes.add(0);
		load("default");
	}
	
	public int getMoveCounter() {
		return currentMove+1;
	}
	
	public int getTotalMoves() {
		return history.size();
	}
	
	public Player getWhoseTurn() {
		return whoseTurn;
	}
	public boolean hasTurn(Player player) {
		return player.getID() == getWhoseTurn().getID();
	}
	
	public void goForward() {
		if (currentMove < history.size()-1) {
			applyNextMove();
			currentMove++;
			nextPlayer();
		}
	}
	
	private void nextPlayer() {
		whoseTurn = players.get(turnIndexes.get(currentMove+1));
	}
	
	public boolean allFinished() {
		for(Player p : players) {
			if (!p.hasFinished()) return false;
		}
		return true;
	}
	
	public void goBackward() {
		if (currentMove > -1) {
			undoCurrentMove();
			currentMove--;
			int newIndex = turnIndexes.get(currentMove + 1);
			whoseTurn = players.get(newIndex);
		}
	}
	
	private void undoCurrentMove() {
		HexMove move = history.get(currentMove);
		board.undoMove(move);
	}
	
	private void applyNextMove() {
		HexMove move = history.get(currentMove + 1);
		board.makeMove(move);
	}
	
	public void setPreferredAI(AI ai) {
		this.findBestMoveAI = ai;
	}
	
	public AI getPreferredAI() {
		return findBestMoveAI;
	}
	
	public void doAIMove() {
		if (allFinished()) return;
		Player whoseTurn = getWhoseTurn();
		if (whoseTurn.isHumanPlayer()) {
			findBestMoveAI.setPlayer(whoseTurn);
		} else {
			findBestMoveAI = whoseTurn.getAI();
		}
		doMove(findBestMoveAI.nextMove(board));
	}
	
	public void doMove(HexMove hexMove) {
		if (allFinished()) return;
		applyMove(hexMove);
		// If next player is an AI, make move automatically
		if (allPlayersAreAI() == false) makeMoveIfAI();
	}
	
	private void applyMove(HexMove move) {
		// If we make a new move while not at end of history,
		// discard history done after this point and apply new move
		if (currentMove < history.size() - 1) {
			List<HexMove> newHistory = new ArrayList<HexMove>();
			for(int i = 0; i <= currentMove; i++) {
				newHistory.add(history.get(i));
			}
			history = newHistory;
		}
		// Add move to history
		history.add(move);
		// Add turn index to list
		addTurnIndex();
		// Go forward in history
		goForward();
	}
	
	public void addTurnIndex() {
		int index = players.indexOf(whoseTurn);
		int newIndex = index+1;
		if (newIndex >= players.size()) newIndex = 0;
		whoseTurn = players.get(newIndex);
		if (whoseTurn.hasFinished() && !allFinished()) {
			addTurnIndex();
		}else {
			turnIndexes.add(newIndex);
		}
	}
	
	public void makeMoveIfAI() {
		Player p = getWhoseTurn();
		// If AI and not finished game, do move!		
		if (!p.isHumanPlayer()) {
			AI ai = p.getAI();
			HexMove nextMove = ai.nextMove(board);
			// Can continues to automatically do AI moves if a human player
			// exits, as it ends in human's turn and not infinite loop
			applyMove(nextMove);
			if (!allPlayersAreAI()) makeMoveIfAI();
		}
	}
	
	public boolean allPlayersAreAI() {
		for(Player p : players) {
			if (p.isHumanPlayer()) return false;
		}
		return true;
	}
	
	public List<Hex> getValidEnds(Hex hex) {
		return board.getPossibleHexesFrom(hex);
	}
	
	public int getNumberOfPlayers() {
		return players.size();
	}
	
	public List<Player> getPlayers() {
		return this.players;
	}
	
	public int getPlayerCount() {
		return this.players.size();
	}
	
	public Player getPlayer(int id) {
		for(Player player : this.players) {
			if (player.getID() == id) return player;
		}
		return null;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public void newGame(int numPlayers) {
		load(lastGameLoaded);
		// Remove players if necessary,
		// but keep them in list as they still own hexes
		// that others can't land on
		ListIterator<Player> iter = players.listIterator();
		while(iter.hasNext()) {
			Player p = iter.next();
			if (p.getID() > numPlayers) {
				//Remove pieces
				List<Hex> hexes = p.getPieceHexes();
				for(Hex hex : hexes) {
					hex.setPiece(null);
				}
				//Remove player
				iter.remove();
			}
		}
	}
	
	//TODO
	public void save(String gameName) {
		BufferedWriter writer = null;
		try {
		File file = new File("board/"+gameName+saveFileExtension);
		if (!file.exists()) {
				file.createNewFile();
		}
		FileWriter fw = new FileWriter(file);
		writer = new BufferedWriter(fw);
		
		//Write game info
		writer.write("[GAME]\n");
		writer.write("Players=" + Integer.toString(this.players.size()) + "\n");
		
		//Write board
		writer.write("[BOARD]\n");
		for(Hex hex : board.getHexes()) {
			
		}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{ 
		   try{
		      if(writer != null) writer.close();
		   }catch(Exception ex){
		       System.out.println("Error in closing the BufferedWriter"+ex);
		    }
		}
	}

	public void load(String boardName) {
		File file = new File("boards/"+boardName+".txt");
		load(file);
	}
	
	public void load(File file) {
		lastGameLoaded = file;
		
		BufferedReader reader;
		try {
			//Read board file
			reader = new BufferedReader(new FileReader(file));
			//Read first line
			String line = reader.readLine();
			List<String> lines = new ArrayList<String>();
			while(line != null) {
				if (line.equals("[GAME]")) {
					line = loadGameData(reader);
				}else if (line.equals("[BOARD]")) {
					line = loadBoardData(reader);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String loadGameData(BufferedReader reader) throws IOException {
		String line = reader.readLine();
		while (line != null && !line.startsWith("[")) {
			if (line.contains("=")) {
				String[] keyValue = line.split("=");
				String key = keyValue[0];
				String value = keyValue[1];
				if (key.equals("Players")) {
					int numPlayers = Integer.parseInt(value);
					players = new ArrayList<>();
					//TODO extract player colors into savefile
					/*Color[] playerColors = { 
							Color.GREEN,
							Color.BLUE,
							Color.WHITE,
							Color.BLACK,
							Color.RED,
							Color.YELLOW,
					};*/
					Color[] playerColors = {
							Color.YELLOW, // 1
							Color.RED, // 2
							Color.BLUE, // 3
							Color.GREEN, // 4
							Color.WHITE, // 5
							Color.BLACK, // 6
					};
					for (int i = 0; i < numPlayers; i++) {
						Player player = new Player(i+1);
						player.setColor(playerColors[i]);
						this.players.add(player);
						
					}
					players.get(0).setOpponent(players.get(1));
					players.get(2).setOpponent(players.get(3));
					players.get(4).setOpponent(players.get(5));
					
					// TODO get current player from savefile
					whoseTurn = this.players.get(0);
				}
			}
			line = reader.readLine();
		}
		return line;
	}
	
	private String loadBoardData(BufferedReader reader) throws IOException {
		List<Hex> hexes = new ArrayList<Hex>();
		// Read first line of board data
		String line = reader.readLine();
		//Store the length of first row, which should also be length of all rows
		int rowLength = line.split("").length;
		//Initialize array of hexes for the previous readLine()
		//and fill with null
		ArrayList<Hex> lastLine = new ArrayList<>(rowLength);
		for(int i = 0; i < rowLength; i++) lastLine.add(null);
		int y = 0;
		int hexId = 0;
		while(line != null && !line.startsWith("[")) {
			Hex lastHex = null;
			String[] positions = line.split("");
			ArrayList<Hex> currentLine = new ArrayList<>(rowLength);
			for(int x = 0; x < rowLength; x++) {
				String p = positions[x];
				//If part of board
				if (!p.equals(".")) {
					//Make new hex and set position in grid
					Hex hex = new Hex(hexId);
					hexId++;
					hex.setX(x);
					hex.setY(y);
					//Find number at pos and set either empty
					//or piece with that player number
					int num = Integer.parseInt(p);
					//If number if less than or equal number of players
					if (num == 0) hex.setPiece(null);
					else if (num <= this.players.size()) {
						Player player = this.players.get(num - 1);
						hex.setPiece(new Piece(player));
						hex.setOwner(player);
					}
					//Set neighbouring pieces
					if (lastLine.get(x) != null) {
						hex.setNeighbour(Direction.TOP_RIGHT, lastLine.get(x));
					}
					if (x-1 >= 0 && lastLine.get(x-1) != null) {
						hex.setNeighbour(Direction.TOP_LEFT, lastLine.get(x-1));
					}

					if(lastHex != null){
						hex.setNeighbour(Direction.LEFT, lastHex);
					}
					//Update hex neighbors
					currentLine.add(hex);
					lastHex = hex;

					hexes.add(hex);
				}else {
					currentLine.add(null);
					lastHex = null;
				}
			}
			lastLine = currentLine;
			y++;
			line = reader.readLine();
		}
		this.board = new Board(hexes);
		return line;
	}
}
