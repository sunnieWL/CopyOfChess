package pieces;

import game.Board;
import model.Position;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    public Pawn(String color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> getValidMoves(Board board) {
        List<Position> validMoves = new ArrayList<>();
        int direction = color.equals("white") ? -1 : 1;
        int startRow = color.equals("white") ? 6 : 1;

        // Move 
        int newX = position.getX() + direction;
        int newY = position.getY();
        if (board.isValidPosition(newX, newY) && board.getPieceAt(newX, newY) == null) {
            validMoves.add(new Position(newX, newY));
            // Move two squares from starting position
            if (position.getX() == startRow) {
                newX += direction;
                if (board.isValidPosition(newX, newY) && board.getPieceAt(newX, newY) == null) {
                    validMoves.add(new Position(newX, newY));
                }
            }
        }

        // Captur
        int[] dy = { -1, 1 };
        newX = position.getX() + direction;
        for (int deltaY : dy) {
            newY = position.getY() + deltaY;
            if (board.isValidPosition(newX, newY)) {
                Piece target = board.getPieceAt(newX, newY);
                if (target != null && !target.getColor().equals(this.color)) {
                    validMoves.add(new Position(newX, newY));
                }
            }
        }

        return validMoves;
    }
    
    @Override
    public Pawn clone() {
        Pawn clonedPawn = new Pawn(this.color, new Position(this.position.getX(), this.position.getY()));
        clonedPawn.setHasMoved(this.hasMoved);
        return clonedPawn;
    }

    @Override
    public String getSymbol() {
        return color.equals("white") ? "♙" : "♟︎";
    }
}
