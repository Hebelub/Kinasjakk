package kinasjakk;

import java.util.List;

public class HexMove {

    private Hex from;
    private Hex to;

    public Hex getFrom() {
        return from;
    }
    public Hex getTo() {
        return to;
    }

    HexMove(Hex from, Hex to) {
        this.from = from;
        this.to = to;
    }

    public boolean isCrossing() {
        return to.isAtGoal(getFrom().getPiece().getPlayer(), getTo())
                && !from.isAtGoal(getFrom().getPiece().getPlayer(), getFrom());
    }
    public boolean movesFromGoal() {
        return from.isAtGoal(getFrom().getPiece().getPlayer(), getFrom())
                && !to.isAtGoal(getFrom().getPiece().getPlayer(), getTo());
    }

    public void move() {
        from.board.makeMove(this);
    }
}
