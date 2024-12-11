package game;

import pieces.Piece;
import model.Position;

public abstract class Move {
    private Piece piece;
    private Position from;
    private Position to;

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
