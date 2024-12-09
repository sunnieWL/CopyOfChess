package game;

import java.util.List;

import gui.GamblePane;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import model.Player;
import model.Position;
import pieces.Piece;
import pieces.King;

public class GambleGame extends Game {
    private GamblePane gamblePane;

    public GambleGame(Player whitePlayer, Player blackPlayer) {
        super(whitePlayer, blackPlayer);
        this.gamblePane = new GamblePane("White");
    }

    public GamblePane getGamblePane() {
        return gamblePane;
    }

    /**
     * Called when a player selects a piece from the GamblePane and decides to make a move.
     */
    public void onPieceSelected(Position from, Position to) {
        try {
            // Get the piece from GamblePane
            Piece piece = getPieceFromGamblePane();

            // Validate the move with Game's logic
            if (piece != null && isValidMove(piece, from, to)) {
                makeMove(from, to);
            } else {
                System.out.println("Invalid move!");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Returns the piece selected from the GamblePane.
     * This method simulates selecting a piece from the available ones.
     */
    private Piece getPieceFromGamblePane() {
        // Logic to retrieve the piece based on the selected label from GamblePane
        // For this example, let's assume the first available piece is selected.
        String currentPlayerColor = getCurrentPlayer().getColor();
        Piece[] availablePieces = gamblePane.getAvailablePiecesForCurrentPlayer();

        // You could modify this to select pieces in a more dynamic way based on user interaction
        return availablePieces[0]; // For simplicity, let's assume the first piece is selected.
    }

    /**
     * Validate the move with the existing rules of the Game.
     */
    private boolean isValidMove(Piece piece, Position from, Position to) {
        // Validate the move based on the piece's possible moves and game rules
        if (piece != null) {
            // Use the existing game logic to validate the move
            List<Position> validMoves = piece.getValidMoves(getBoard());
            return validMoves.contains(to);
        }
        return false;
    }

    @Override
    public void makeMove(Position from, Position to) {
        super.makeMove(from, to); // Use the same logic from Game.java for making a move
        gamblePane.updateGamble(); // Update the GamblePane after making a move
    }
}
