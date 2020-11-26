package kinasjakk;

import java.util.ArrayList;
import java.util.List;

public class PointingHex {

    private Hex hex;

    private Direction direction;

    PointingHex(Hex hex, Direction direction) {
        this.hex = hex;
        this.direction = direction;
    }

    public Hex getNextHex() {
        return hex.getNeighbor(direction);
    }

    public PointingHex getNextPointingHex() {
        return new PointingHex(getNextHex(), direction);
    }

    public List<Hex> getHexLine() {
        List<Hex> line = new ArrayList<>();
        line.add(hex);

        while(true) {
            Hex nextHex = line.get(line.size() - 1).getNeighbor(direction);
            if(nextHex != null)
                line.add(nextHex);
            else break;
        }

        return line;
    }

    public Hex getHex() {
        return hex;
    }

    public void setHex(Hex hex) {
        this.hex = hex;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
