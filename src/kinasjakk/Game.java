package kinasjakk;

import javax.print.attribute.standard.NumberOfDocuments;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Game {
	Board board;

	int numberOfPlayers;

	private Player[] players;

	public AI ai;

	public Game(int numberOfPlayers) {
		ai = new AI();

		this.numberOfPlayers = numberOfPlayers;
		players = new Player[numberOfPlayers];
		for (int i = 0; i < numberOfPlayers; i++) {
			players[i] = new Player();

			// Setting opposite players
			if(i >= 3) {
				players[i].setOppositePlayer(players[i - 3]);
				players[i-3].setOppositePlayer(players[i]);
			}
		}
		loadBoardFromDisk("default");
		board.setPlayers(players);

	}
	
	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	private BufferedReader ReadBoardFile(String boardName) throws FileNotFoundException {
		return new BufferedReader(new FileReader("boards/"+boardName+".txt"));
	}
	private int getLineLength(String line) {
		return line.split("").length;
	}

	HashMap<Integer, Hex> hexes;

	public void loadBoardFromDisk(String boardName) {
		Board b = new Board();
		
		BufferedReader reader;
		try {
			reader = ReadBoardFile(boardName);
			//Read first line
			String line = reader.readLine();
			int rowLength = getLineLength(line);

			//Initialize array of hexes for the previous readLine()
			//and fill with null
			ArrayList<Hex> lastLine = new ArrayList<>(rowLength);
			for(int i = 0; i < rowLength; i++) lastLine.add(null);
			int y = 0;
			while(line != null) {
				Hex lastHex = null;
				String[] positions = line.split("");
				ArrayList<Hex> currentLine = new ArrayList<>(rowLength);
				for(int x = 0; x < rowLength; x++) {
					String p = positions[x];
					//If part of board
					if (!p.equals(".")) {
						//Make new hex and set position in grid
						Hex hex = new Hex(b);
						hex.setX(x);
						hex.setY(y);
						//Find number at pos and set either empty
						//or piece with that player number
						int num = Integer.parseInt(p);
						if (num == 0 || num > numberOfPlayers) hex.setPiece(null);
						else {
							Piece piece = new Piece(players[num - 1], hex);
							hex.setPiece(piece);

							players[num - 1].addPiece(piece);
							// players[num - 1].addStartHex(piece.getHex());

						}
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

						b.addHex(hex);
					} else {
						currentLine.add(null);
						lastHex = null;
					}
				}
				lastLine = currentLine;
				y++;
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.board = b;
	}
}
