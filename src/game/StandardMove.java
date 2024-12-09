package game;

import pieces.Piece;
import game.Board;
import model.Position;

public class StandardMove extends Move {
    public StandardMove(Piece piece, Position from, Position to) {
        super(piece, from, to);
    }

    @Override
    public boolean execute(Board board) {
        return board.movePiece(from, to);
    }
}
