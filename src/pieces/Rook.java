package pieces;

import game.Board;
import model.Position;
import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {
    public Rook(String color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> getValidMoves(Board board) {
        List<Position> validMoves = new ArrayList<>();
        int[] directions = { -1, 1 };
    
        for (int dx : directions) {
            int newX = position.getX() + dx;
            int newY = position.getY();
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
            }
        }

        for (int dy : directions) {
            int newX = position.getX();
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
                newY += dy;
            }
        }
        return validMoves;
    }

    @Override
    public String getSymbol() {
        return color.equals("white") ? "♖" : "♜";
    }
    
    @Override
    public Rook clone() {
        Rook clonedRook = new Rook(this.color, new Position(this.position.getX(), this.position.getY()));
        clonedRook.setHasMoved(this.hasMoved);
        return clonedRook;
    }
}
