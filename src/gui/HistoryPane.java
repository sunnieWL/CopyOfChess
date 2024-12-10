package gui;

import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Position;
import pieces.Piece;
import game.Game;
import game.Move;

public class HistoryPane extends VBox {

    private static GridPane historyGrid; // GridPane to hold the move history
    private ScrollPane scrollPane;      // ScrollPane to make the GridPane scrollable

    public HistoryPane() {
        // Set the style for the HistoryPane
        this.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-background-radius: 10px;");
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10); // Vertical spacing between elements

        // Add a title for the history section
        //Text title = new Text("Move History");
        //title.setStyle("-fx-font-size: 16px; -fx-fill: white;");
        //this.getChildren().add(title);

        // Create a GridPane to hold the history items
        historyGrid = new GridPane();
        historyGrid.setHgap(10);  // Horizontal gap between columns
        historyGrid.setVgap(5);   // Vertical gap between rows
        historyGrid.setAlignment(Pos.CENTER);

        // Wrap the GridPane inside a ScrollPane to make it scrollable
        scrollPane = new ScrollPane(historyGrid);
        scrollPane.setFitToWidth(true);  // Ensure it fits the width of the pane
        scrollPane.setFitToHeight(true);

        // Set the preferred size of the ScrollPane
        scrollPane.setPrefWidth(400);  // Adjust width as needed
        scrollPane.setPrefHeight(500); // Adjust height as needed
        
        scrollPane.setStyle("-fx-padding: 10px; -fx-border-radius: 10px; -fx-background-radius: 10px;");


        this.getChildren().add(scrollPane);
    }

    public static void updateHistory() {
        // Clear previous history and add the updated moves
        historyGrid.getChildren().clear();

        // Add column headers

        Text whiteHeader = new Text("White");
        Text blackHeader = new Text("Black");

        whiteHeader.setStyle("-fx-font-weight: bold; -fx-fill: Black;");
        blackHeader.setStyle("-fx-font-weight: bold; -fx-fill: Black;");

        historyGrid.add(whiteHeader, 1, 0);
        historyGrid.add(blackHeader, 2, 0);

        // Populate the grid with move history
        int row = 1; // Start from row 1 (row 0 is for headers)
        int moveNumber = 1;

        for (int i = 0; i < Game.getMoveHistory().size(); i++) {
            Move move = Game.getMoveHistory().get(i);
            Piece piece = move.getPiece();
            String pieceSymbol = piece.getSymbol();

            // Get the move from and to in chess notation
            String fromPosition = positionToNotation(move.getFrom());
            String toPosition = positionToNotation(move.getTo());
            String moveString = pieceSymbol + " " + fromPosition + toPosition;

            if (i % 2 == 0) { // White move
                historyGrid.add(new Text(String.valueOf(moveNumber)), 0, row); // Add move number
                historyGrid.add(new Text(moveString), 1, row); // Add white move
            } else { // Black move
                historyGrid.add(new Text(moveString), 2, row); // Add black move
                row++; // Increment row after a black move
                moveNumber++; // Increment move number
            }
        }

        // If the last move was a white move, increment the row
        if (Game.getMoveHistory().size() % 2 != 0) {
            row++;
        }
    }

    private static String positionToNotation(Position position) {
        int row = 8 - position.getX(); // Correct row calculation
        char column = (char) ('a' + position.getY()); // Convert Y-coordinate to letter (a-h)
        return "" + column + row; // Return chess notation like "a1", "f4", etc.
    }
}