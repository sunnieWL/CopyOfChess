package pieces;

import interfaces.Movable;
import model.Position;
import game.Board;
import java.util.List;

public abstract class Piece implements Movable,Cloneable {
    protected String color; 
    protected Position position;
    protected boolean hasMoved;

    public Piece(String color, Position position) {
        this.color = color;
        this.position = position;
        this.hasMoved = false;
    }

    public String getColor() { return color; }
    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }
    public boolean hasMoved() { return hasMoved; }
    public void setHasMoved(boolean hasMoved) { this.hasMoved = hasMoved; }

    @Override
    public abstract List<Position> getValidMoves(Board board);

    @Override
    public void move(Position to) {
        this.position = to;
        this.hasMoved = true;
    }

    public abstract String getSymbol();

    @Override
    public abstract Piece clone();
}
