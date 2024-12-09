package gui;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import java.util.Random;
import pieces.*;

public class GamblePane extends GridPane {
    
   
    public GamblePane() {
        // Set the style for the GamblePane
        this.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-background-radius: 10px;");
        
        // Set the alignment and spacing
        this.setAlignment(Pos.CENTER);
        this.setHgap(20);  // Horizontal gap between slots
        this.setVgap(10);  // Vertical gap between slots
        
        // Create 3 slots for displaying random pieces
        for (int i = 0; i < 3; i++) {
            // Get a random piece symbol
            String pieceSymbol = "h";
            
            // Create a Label to display the piece symbol
            Label pieceLabel = new Label(pieceSymbol);
            
            // Set the font size and color
            pieceLabel.setFont(new Font("Arial", 50));  // Set the size of the piece symbol
            pieceLabel.setTextFill(Color.WHITE);  // Set the text color to white
            
            // Add the label to the grid at column i
            this.add(pieceLabel, i, 0);  // Add the Label to row 0, column i
        }
    }
    
    
}