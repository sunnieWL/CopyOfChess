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
        return validMoves;
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
