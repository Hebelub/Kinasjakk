package kinasjakk;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {

    private String name;
    private boolean isHuman;
    private Color color;

    private List<Hex> startHexes = new ArrayList<>();
    private List<Hex> winHexes = new ArrayList<>();
    private List<Piece> pieces = new ArrayList<>();

    public Player() {
        name = "New Player";
        isHuman = true;
        Random r = new Random();
        color = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHuman() {
        return isHuman;
    }

    public void setHuman(boolean human) {
        isHuman = human;
    }
}
