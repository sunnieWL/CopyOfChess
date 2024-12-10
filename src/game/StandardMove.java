package game;

import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import gui.ControlPane;
import model.Position;

public class StandardMove extends Move {
    public StandardMove(Piece piece, Position from, Position to) {
        super(piece, from, to);
    }

    @Override
    public boolean execute(Board board) {

        boolean moved = board.movePiece(from, to);

        if (!moved) {
            return false; 
        }

        if (piece instanceof Pawn) {
            Pawn pawn = (Pawn) piece;
            if (pawn.canPromote(to)) {
                handlePawnPromotion(board, pawn, to);
            }
          
        }
        return true;
    }
        
    
    private void handlePawnPromotion(Board board, Pawn pawn, Position position) {

        Piece promotedPiece = ControlPane.getPromotedPiece();


        if (promotedPiece == null) {
            promotedPiece = new Queen(pawn.getColor(), position);
        } else {
           
            promotedPiece.setPosition(position);
            promotedPiece.setColor(pawn.getColor());  
        }

        // Update the board with the promoted piece
        board.setPieceAt(position, promotedPiece);
        System.out.println("Pawn promoted to " + promotedPiece.getClass().getSimpleName() + "!");
    }
}
