package gui;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import pieces.*;
import java.util.Random;

public class GamblePane extends GridPane {

    private String currentPlayerColor;  // To track the current player's color

    public GamblePane(String initialPlayerColor) {
        // Initialize with the given player's color
        this.currentPlayerColor = initialPlayerColor;
        
        // Set the style for the GamblePane
        this.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-background-radius: 10px;");
        
        // Set the alignment and spacing
        this.setAlignment(Pos.CENTER);
        this.setHgap(20);  // Horizontal gap between slots
        this.setVgap(10);  // Vertical gap between slots

        // Initialize the gamble with the current player's pieces
        updateGamble();
    }

    /**
     * Updates the GamblePane to show the pieces of the current player.
     */
    public void updateGamble() {
        // Clear any previous content from the grid
        this.getChildren().clear();
        
        // Create a random number generator
        Random random = new Random();
        
        // Define available pieces based on the current player's color
        Piece[] availablePieces = getAvailablePiecesForCurrentPlayer();
        
        // Create 3 slots for displaying random pieces
        for (int i = 0; i < 3; i++) {
            // Randomly select a piece from the available pieces
            Piece randomPiece = availablePieces[random.nextInt(availablePieces.length)];
            
            // Get the symbol of the selected piece
            String pieceSymbol = randomPiece.getSymbol();
            
            // Create a Label to display the piece symbol
            Label pieceLabel = new Label(pieceSymbol);
            
            // Set the font size and color
            pieceLabel.setFont(new Font("Arial", 50));  // Set the size of the piece symbol
            pieceLabel.setTextFill(Color.WHITE);  // Set the text color to white
            
            // Add the label to the grid at column i
            this.add(pieceLabel, i, 0);  // Add the Label to row 0, column i
        }
    }

    /**
     * Returns an array of available pieces for the current player.
     */
    public Piece[] getAvailablePiecesForCurrentPlayer() {
        // Select the pieces based on the current player's color
        if (currentPlayerColor.equals("white")) {
            return new Piece[] {
                new Queen("white", null),
                new Rook("white", null),
                new Rook("white", null),
                new Bishop("white", null),
                new Bishop("white", null),
                new Knight("white", null),
                new Knight("white", null),
                new Pawn("white",null),
                new Pawn("white",null),
                new Pawn("white",null),
                new Pawn("white",null),
                new Pawn("white",null),
                new Pawn("white",null),
                new Pawn("white",null),
                new Pawn("white",null)
            };
        } else {
            return new Piece[] {
            		new Queen("black", null),
                    new Rook("black", null),
                    new Rook("black", null),
                    new Bishop("black", null),
                    new Bishop("black", null),
                    new Knight("black", null),
                    new Knight("black", null),
                    new Pawn("black",null),
                    new Pawn("black",null),
                    new Pawn("black",null),
                    new Pawn("black",null),
                    new Pawn("black",null),
                    new Pawn("black",null),
                    new Pawn("black",null),
                    new Pawn("black",null)
            };
        }
    }

    /**
     * Sets the current player's color and updates the gamble pane.
     * This should be called when the player changes.
     */
    public void setCurrentPlayerColor(String color) {
        this.currentPlayerColor = color;
        updateGamble();
    }
}
