package kinasjakk;

import UI.MainFrame;

public class Main {

	public static void main(String[] args) {
		
		Hex mainHex = new Hex();
		System.out.println(mainHex);
		mainHex.setNeighbour(Direction.TOP_LEFT, new Hex());
		System.out.println(mainHex);		
		
		MainFrame frame = new MainFrame();

	}

}
