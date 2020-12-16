package kinasjakk;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {

    private String name;
    private boolean isHuman;
    private Color color;

    public Board board;

//    private List<Hex> startHexes = new ArrayList<>();
//    private List<Hex> goalHexes = new ArrayList<>();
    private List<Piece> pieces = new ArrayList<>();

    private static int nextId = 0;
    public int id;

    public Player() {
        assignId();

        setHuman(false);
        if(id == 0) setHuman(true);

        name = "New Player";
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

    public boolean hasFinished = false;
    public void finish() {
        hasFinished = true;
    }
    public boolean hasFinished() {
        for (Piece piece : getPieces()) {
            if(!getGoalHexes().contains(piece.getHex()))
                return false;
        }
        return true;
    }

    public List<Piece> getPiecesOnStartHexes() {
        List<Piece> startPieces = new ArrayList<>();
        for (Piece piece : getPieces()) {
            if(piece.isAtStart()) {
                startPieces.add(piece);
            }
        }
        return startPieces;
    }
    public List<Piece> getPiecesOnGoalHexes() {
        List<Piece> goalPieces = new ArrayList<>();
        for (Piece piece : getPieces()) {
            if(piece.isAtGoal()) {
                goalPieces.add(piece);
            }
        }
        return goalPieces;
    }
    public List<Piece> getPiecesInMiddle() {
        List<Piece> middlePieces = new ArrayList<>();
        for (Piece piece : getPieces()) {
            if(piece.isInMiddle()) {
                middlePieces.add(piece);
            }
        }
        return middlePieces;
    }
    public int getNumberOfEmptyGoalHexes() {
        return getPieces().size() - getPiecesOnGoalHexes().size();
    }

    public List<Hex> getGoalHexes() {
        return getOppositePlayer().getStartHexes();
    }
    public List<Hex> getStartHexes() {
        return getPieces().get(0).getHex().board.getStartHexesOfPlayer(this);
    }

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
