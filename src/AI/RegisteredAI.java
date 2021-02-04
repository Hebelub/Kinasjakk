package AI;

import kinasjakk.Player;

public class RegisteredAI {
	
	public static String[] getNames() {
		String[] names = {
			"RandomAI",
			"GreedyAI",
			"HerdAI",	
			"MinimaxAI",
			"MinimaxAI_Dist_Goal",
			"MinimaxAI_2deep",
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
		case "MinimaxAI_Dist_Goal":
			return new MinimaxAI_Dist_Goal(p);
		case "MinimaxAI_2deep":
			return new MinimaxAI_2deep(p);
		default:
			return new RandomAI(p);
		}
	}
	
}
