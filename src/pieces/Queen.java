package pieces;

import game.Board;
import model.Position;
import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {
    public Queen(String color, Position position) {
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
        return color.equals("white") ? "♕" : "♛";
    }
    
    @Override
    public Queen clone() {
        Queen clonedQueen = new Queen(this.color, new Position(this.position.getX(), this.position.getY()));
        clonedQueen.setHasMoved(this.hasMoved);
        return clonedQueen;
    }
}
