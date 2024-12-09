package pieces;

import game.Board;
import model.Position;
import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    public Bishop(String color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> getValidMoves(Board board) {
        List<Position> validMoves = new ArrayList<>();
        int[] directions = { -1, 1 };
        // Diagonal movements
        for (int dx : directions) {
            for (int dy : directions) {
                int newX = position.getX() + dx;
                int newY = position.getY() + dy;
                while (board.isValidPosition(newX, newY)) {
                    Piece target = board.getPieceAt(newX, newY);
                    if (target == null) {
                        validMoves.add(new Position(newX, newY));
                    } else {
                        if (!target.getColor().equals(this.color)) {
                            validMoves.add(new Position(newX, newY));
                        }
                        break;
                    }
                    newX += dx;
                    newY += dy;
                }
            }
        }
        return validMoves;
    }

    @Override
    public String getSymbol() {
        return color.equals("white") ? "♗" : "♝";
    }
    
    @Override
    public Bishop clone() {
        Bishop clonedBishop = new Bishop(this.color, new Position(this.position.getX(), this.position.getY()));
        clonedBishop.setHasMoved(this.hasMoved);
        return clonedBishop;
    }
}
