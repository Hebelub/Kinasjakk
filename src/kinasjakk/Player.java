package kinasjakk;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {

    private String name;
    private boolean isHuman;
    private Color color;

//    private List<Hex> startHexes = new ArrayList<>();
//    private List<Hex> goalHexes = new ArrayList<>();
    private List<Piece> pieces = new ArrayList<>();

    private static int nextId = 0;
    public int id;

    public Player() {
        assignId();
        name = "New Player";
        isHuman = true;
        Random r = new Random();
        color = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
    }

    private void assignId() {
        id = nextId++;
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

    public void addPiece(Piece piece) {
        pieces.add(piece);
    }
//   public void addWinHex(Hex winHex) {
//       goalHexes.add(winHex);
//   }

//   public void addStartHex(Hex startHex) {
//       startHexes.add(startHex);
//   }

//    public List<Hex> getStartHexes() {
//        return startHexes;
//    }
//
//    public List<Hex> getGoalHexes() {
//        return goalHexes;
//    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public int getId() {
        return id;
    }

    public Player oppositePlayer;
    public Player getOppositePlayer() {
        return oppositePlayer;
    }
    public void setOppositePlayer(Player oppositePlayer) {
        this.oppositePlayer = oppositePlayer;
    }
}
