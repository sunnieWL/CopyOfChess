package pieces;

import game.Board;
import model.Position;
import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    public Knight(String color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> getValidMoves(Board board) {
        List<Position> validMoves = new ArrayList<>();
        int[][] moves = {
            {2, 1}, {1, 2}, {-1, 2}, {-2, 1},
            {-2, -1}, {-1, -2}, {1, -2}, {2, -1}
        };
        for (int[] move : moves) {
            int newX = position.getX() + move[0];
            int newY = position.getY() + move[1];
            if (board.isValidPosition(newX, newY)) {
                Piece target = board.getPieceAt(newX, newY);
                if (target == null || !target.getColor().equals(this.color)) {
                    validMoves.add(new Position(newX, newY));
                }
            }
        }
        return validMoves;
    }

    @Override
    public String getSymbol() {
        return color.equals("white") ? "♘" : "♞";
    }
    
    @Override
    public Knight clone() {
        Knight clonedKnight = new Knight(this.color, new Position(this.position.getX(), this.position.getY()));
        clonedKnight.setHasMoved(this.hasMoved);
        return clonedKnight;
    }
}
