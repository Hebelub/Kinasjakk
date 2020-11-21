package kinasjakk;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Game {
	Board board;
	
	public Game() {
		board = loadBoardFromDisk("default");
	}
	
	public Board loadBoardFromDisk(String boardName) {
		Board b = new Board();
		
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("boards/"+boardName+".txt"));
			String line = reader.readLine();
			int rowLength = line.split("").length;
			ArrayList<Hex> lastLine = new ArrayList<Hex>(rowLength);
			for(int i = 0; i < rowLength; i++) {
				lastLine.add(null);
			}
			System.out.println("RowLength: " + rowLength);
			while(line != null) {
				Hex lastHex = null;
				String[] positions = line.split("");
				for(int i = 0; i < rowLength; i++) {
					String p = positions[i];
					System.out.print(p);
					if (!p.equals(".")) {
						Hex hex = new Hex();
						lastLine.set(i, hex);
						lastHex = hex;
						if (lastLine.get(i) != null) {
							hex.setNeighbour(Direction.TOP_LEFT, lastLine.get(i));
						}else if (i+1 < lastLine.size() && lastLine.get(i+1) != null) {
							hex.setNeighbour(Direction.TOP_RIGHT, lastLine.get(i+1));
						}
						b.addHex(hex);
					}else {
						System.out.println(" is a period!");
						System.out.println(lastLine.size());
						lastLine.set(i, null);
						lastHex = null;
					}
				}
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return b;
		
	}
}
