package gui;

import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Position;
import pieces.Piece;
import game.Game;
import game.Move;

public class HistoryPane extends VBox {
    
    private static VBox historyVBox;  // VBox to hold the move history
    private ScrollPane scrollPane;  // ScrollPane to make the VBox scrollable

    public HistoryPane() {
        // Set the style for the HistoryPane
        this.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-background-radius: 10px;");
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);  // Vertical spacing between elements

        // Create a VBox to hold the history items
        historyVBox = new VBox();
        historyVBox.setSpacing(5);  // Spacing between the individual history items

        // Wrap the VBox inside a ScrollPane to make it scrollable when the history grows
        scrollPane = new ScrollPane(historyVBox);
        scrollPane.setFitToWidth(true);  // Ensure it fits the width of the pane
        scrollPane.setFitToHeight(true); 

        this.getChildren().add(scrollPane);

        // Add a title for the history section
        Text title = new Text("Move History");
        this.getChildren().add(title);
    }

    public static void updateHistory() {
        // Clear previous history and add the updated moves
        historyVBox.getChildren().clear();

        // Loop through the move history and add each move as a Text node to the VBox
        for (Move move : Game.getMoveHistory()) {
            Piece piece = move.getPiece();
            String pieceSymbol = piece.getSymbol();

            // Get the move from and to in chess notation
            String fromPosition = positionToNotation(move.getFrom());
            String toPosition = positionToNotation(move.getTo());

            // Format the move as "symbol a2a3"
            String moveString = pieceSymbol + " " + fromPosition + toPosition;
            
            // Add the move string as a Text node to the VBox
            historyVBox.getChildren().add(new Text(moveString));
        }
    }

    private static String positionToNotation(Position position) {
        int row = 8 - position.getX();  // Correct row calculation
        char column = (char) ('a' + position.getY());  // Convert Y-coordinate to letter (a-h)
        return "" + column + row;  // Return chess notation like "a1", "f4", etc.
    }
    
}