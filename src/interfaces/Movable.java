package interfaces;

import game.Board;
import model.Position;
import java.util.List;

public interface Movable {
    List<Position> getValidMoves(Board board);
    void move(Position to);
}
