package gui;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import pieces.Piece;
import model.Player;
import java.util.List;
import java.util.Random;

public class GamblePane extends GridPane {

    private String currentPlayerColor;  // To track the current player's color
    private List<Piece> availablePieces;  // List of available pieces for the current player

    public GamblePane(String initialPlayerColor) {
        this.currentPlayerColor = initialPlayerColor;
        this.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-background-radius: 10px;");
        this.setAlignment(Pos.CENTER);
        this.setHgap(20);
        this.setVgap(10);
        updateGamble();  // Update the GamblePane on initialization
    }

    /**
     * Updates the GamblePane to show the pieces of the current player.
     */
    public void updateGamble() {
        this.getChildren().clear();  // Clear any previous content

        if (availablePieces == null || availablePieces.isEmpty()) {
            System.out.println("No available pieces to display.");
            return;
        }

        // Create a random number generator
        Random random = new Random();

        // Create 3 slots for displaying random pieces
        for (int i = 0; i < 3; i++) {
            // Randomly select a piece from the available pieces
            Piece randomPiece = availablePieces.get(random.nextInt(availablePieces.size()));

            // Get the symbol of the selected piece
            String pieceSymbol = randomPiece.getSymbol();

            // Create a Label to display the piece symbol
            Label pieceLabel = new Label(pieceSymbol);
            pieceLabel.setFont(new Font("Arial", 50));  // Set the size of the piece symbol
            pieceLabel.setTextFill(Color.WHITE);  // Set the text color to white

            // Add the label to the grid at column i
            this.add(pieceLabel, i, 0);  // Add the Label to row 0, column i
        }
    }

    /**
     * Sets the available pieces for the current player and updates the gamble display.
     */
    public void setAvailablePieces(List<Piece> availablePieces) {
        this.availablePieces = availablePieces;
        updateGamble();  // Update the gamble display with new pieces
    }

    /**
     * Sets the color of the current player and updates the available pieces for that player.
     */
    public void setCurrentPlayerColor(String color) {
        this.currentPlayerColor = color;
        updateGamble();  // Update the GamblePane when the player color changes
    }

    /**
     * Returns the available pieces for the current player.
     */
    public List<Piece> getAvailablePiecesForCurrentPlayer() {
        return availablePieces;
    }
}
