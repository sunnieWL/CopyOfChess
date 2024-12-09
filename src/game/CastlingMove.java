package game;

import pieces.King;
import pieces.Rook;
import model.Position;

public class CastlingMove extends Move {
    private Rook rook;
    private Position rookFrom;
    private Position rookTo;

    public CastlingMove(King king, Rook rook, Position kingFrom, Position kingTo, Position rookFrom, Position rookTo) {
        super(king, kingFrom, kingTo);
        this.rook = rook;
        this.rookFrom = rookFrom;
        this.rookTo = rookTo;
    }

    @Override
    public boolean execute(Board board) {
        // Move the king
        boolean kingMoved = board.movePiece(from, to);
        // Move the rook
        boolean rookMoved = board.movePiece(rookFrom, rookTo);
        return kingMoved && rookMoved;
    }
}
