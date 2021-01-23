package AI;

import kinasjakk.Player;

public class RegisteredAI {
	
	public static String[] getNames() {
		String[] names = {
			"RandomAI",
			"GreedyAI",
			"HerdAI",	
			"MinimaxAI",
		};
		return names;
	}
	
	public static AI giveInstance(String name, Player p) {
		switch(name) {
		case "RandomAI":
			return new RandomAI(p);
		case "GreedyAI": 
			return new GreedyAI(p);
		case "HerdAI":
			return new HerdAI(p);
		case "MinimaxAI":
			return new MinimaxAI(p);
		default:
			return new RandomAI(p);
		}
	}
	
}
