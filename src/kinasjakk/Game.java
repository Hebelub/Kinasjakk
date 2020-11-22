package kinasjakk;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Game {
	Board board;

	public Game() {
		loadBoardFromDisk("default");
	}
	
	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public void loadBoardFromDisk(String boardName) {
		Board b = new Board();
		
		BufferedReader reader;
		try {
			//Read board file
			reader = new BufferedReader(new FileReader("boards/"+boardName+".txt"));
			//Read first life
			String line = reader.readLine();
			//Store the length of first row, which should also be length of all rows
			int rowLength = line.split("").length;
			//Initialize array of hexes for the previous readLine()
			//and fill with null
			ArrayList<Hex> lastLine = new ArrayList<Hex>(rowLength);
			for(int i = 0; i < rowLength; i++) lastLine.add(null);
			int y = 0;
			while(line != null) {
				Hex lastHex = null;
				String[] positions = line.split("");
				for(int x = 0; x < rowLength; x++) {
					String p = positions[x];
					//If part of board
					if (!p.equals(".")) {
						//Make new hex and set position in grid
						Hex hex = new Hex();
						hex.setX(x);
						hex.setY(y);
						//Find number at pos and set either empty
						//or piece with that player number
						int num = Integer.parseInt(p);
						if (num == 0) hex.setPiece(null);
						else hex.setPiece(new Piece(num));
						if (lastLine.get(x) != null) {
							hex.setNeighbour(Direction.TOP_RIGHT, lastLine.get(x));
						}else if (x-1 >= 0 && lastLine.get(x-1) != null) {
							hex.setNeighbour(Direction.TOP_LEFT, lastLine.get(x-1));
						}

						if(lastHex != null){
							hex.setNeighbour(Direction.RIGHT, lastHex);
						}
						//Update hex neighbors
						lastLine.set(x, hex);
						lastHex = hex;

						b.addHex(hex);
					}else {
						lastLine.set(x, null);
						lastHex = null;
					}
				}
				y++;
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.board = b;
	}
}
