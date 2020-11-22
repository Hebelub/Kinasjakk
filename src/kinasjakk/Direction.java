package kinasjakk;

public enum Direction {
	LEFT,
	RIGHT,
	TOP_LEFT,
	TOP_RIGHT,
	BOTTOM_LEFT,
	BOTTOM_RIGHT;

	static Direction opposite (Direction d) {
		switch (d) {
			case LEFT:
				return RIGHT;
			case RIGHT:
				return LEFT;
			case TOP_LEFT:
				return BOTTOM_RIGHT;
			case BOTTOM_RIGHT:
				return TOP_LEFT;
			case TOP_RIGHT:
				return BOTTOM_LEFT;
			case BOTTOM_LEFT:
				return TOP_RIGHT;
			default:
				return d;
		}
	}

}
