package pieces;

import game.Board;
import model.Position;
import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    public King(String color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> getValidMoves(Board board) {
        List<Position> validMoves = new ArrayList<>();
        int[] directions = {-1, 0, 1};
        for (int dx : directions) {
            for (int dy : directions) {
                if (dx == 0 && dy == 0) continue;
                int newX = position.getX() + dx;
                int newY = position.getY() + dy;
                if (board.isValidPosition(newX, newY)) {
                    Piece target = board.getPieceAt(newX, newY);
                    if (target == null || !target.getColor().equals(this.color)) {
                        validMoves.add(new Position(newX, newY));
                    }
                }
            }
        }
        if (!this.hasMoved) {
            if (canCastleKingside(board)) {
                validMoves.add(new Position(position.getX(), position.getY() + 2));
            }
            if (canCastleQueenside(board)) {
                validMoves.add(new Position(position.getX(), position.getY() - 2));
            }
        }

        return validMoves;
    }
    
    private boolean canCastleKingside(Board board) {
        int row = position.getX();
        Piece rookPiece = board.getPieceAt(row, 7);
        if (!(rookPiece instanceof Rook) || rookPiece.hasMoved()) {
            return false;
        }
        for (int y = position.getY() + 1; y < 7; y++) {
            if (board.getPieceAt(row, y) != null) {
                return false;
            }
        }
        for (int y = position.getY(); y <= position.getY() + 2; y++) {
            Position pos = new Position(row, y);
            if (board.isSquareUnderAttack(pos, this.color, true)) {
                return false;
            }
        }
        return true;
    }

    private boolean canCastleQueenside(Board board) {
        int row = position.getX();
        Piece rookPiece = board.getPieceAt(row, 0);
        if (!(rookPiece instanceof Rook) || rookPiece.hasMoved()) {
            return false;
        }
        for (int y = position.getY() - 1; y > 0; y--) {
            if (board.getPieceAt(row, y) != null) {
                return false;
            }
        }
        // check attacking square
        for (int y = position.getY(); y >= position.getY() - 2; y--) {
            Position pos = new Position(row, y);
            if (board.isSquareUnderAttack(pos, this.color, true)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getSymbol() {
        return color.equals("white") ? "♔" : "♚";
    }
    
    @Override
    public King clone() {
        King clonedKing = new King(this.color, new Position(this.position.getX(), this.position.getY()));
        clonedKing.setHasMoved(this.hasMoved);
        return clonedKing;
    }
}
