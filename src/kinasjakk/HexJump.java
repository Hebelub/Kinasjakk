package kinasjakk;

import java.util.ArrayList;
import java.util.List;

public class HexJump {

    private HexJump jumpFrom;
    private Hex hex;

    HexJump (HexJump jumpFrom, Hex hex) {
        this.hex = hex;
        this.jumpFrom = jumpFrom;
    }

    public List<Hex> getHexPathFromRoot() {

        List<Hex> path = new ArrayList<>();
        HexJump previous = jumpFrom;

        while (true) {

            path.add(previous.getHex());
            previous = previous.jumpFrom;

            if(previous == null) {
                break;
            }
        }

        return path;
    }

    public HexJump getJumpFrom() {
        return jumpFrom;
    }

    public void setJumpFrom(HexJump jumpFrom) {
        this.jumpFrom = jumpFrom;
    }

    public Hex getHex() {
        return hex;
    }

    public void setHex(Hex hex) {
        this.hex = hex;
    }
}
