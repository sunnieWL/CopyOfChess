package game;

import pieces.Piece;
import model.Position;

public abstract class Move {
    protected Piece piece;
    protected Position from;
    protected Position to;

    public Move(Piece piece, Position from, Position to) {
        this.piece = piece;
        this.from = from;
        this.to = to;
    }

    public Piece getPiece() { return piece; }
    public Position getFrom() { return from; }
    public Position getTo() { return to; }

    public abstract boolean execute(Board board);
        
}
