package gui;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import game.Game;
import game.Board;

import model.Player;
import gui.ChessBoardView;

public class StartPane extends VBox {

    public StartPane(Stage primaryStage) {
        // Set a background color for the pane
        this.setStyle("-fx-background-color: #f0f8ff;");  // Light blue background
        this.setSpacing(20);  // Set spacing between components
        this.setAlignment(Pos.CENTER);  // Center the components in the VBox
        this.setPadding(new javafx.geometry.Insets(60)); // Padding around the pane

        // Set preferred width and height for the StartPane
        this.setPrefWidth(800);  // Increase width for a larger StartPane
        this.setPrefHeight(600);  // Increase height for a larger StartPane

        // Make VBox fill the entire width and height of its container
        this.setFillWidth(true);  // Ensures VBox stretches to fill the width of the parent

        // Title text
        Text title = new Text("Welcome to Chess v2!");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 36));  // Increase title font size
        title.setFill(Color.DARKBLUE);

        // ComboBox for selecting game mode
        ComboBox<String> modeComboBox = new ComboBox<>();
        modeComboBox.getItems().addAll("Classic" , "Gamble"); // Example modes
        modeComboBox.setPromptText("Select Game Mode");
        modeComboBox.setStyle("-fx-font-size: 20px; -fx-padding: 15px; -fx-background-color: #ffffff;");
        modeComboBox.setPrefWidth(300);  // Set preferred width for ComboBox

        // Button to start the game
        Button startButton = new Button("Start");
        startButton.setStyle("-fx-font-size: 20px; -fx-padding: 15px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        startButton.setMinWidth(300);  // Set a minimum width for the button
        startButton.setStyle("-fx-background-radius: 5px; -fx-border-radius: 5px;");  // Rounded corners for the button

        // Set the action for the start button
        startButton.setOnAction(e -> {
            String selectedMode = modeComboBox.getValue();
            if (selectedMode != null) {
                System.out.println("Starting the game in " + selectedMode + " mode.");

                boolean isGamble = selectedMode == "Gamble" ? true: false ;
                
                
                Player white = new Player("White", "white", null);
                Player black = new Player("Black", "black", null);
                
               
                    Game game = new Game(white, black);  
                    Board board = game.getBoard();
                    ChessBoardView boardView = new ChessBoardView(board, game);
                    ControlPane controlPane = new ControlPane(isGamble);
                    HBox root = new HBox(20);
                    root.getChildren().addAll(boardView, controlPane);

                    // Wrap ChessBoardView and ControlPane in a Scene and set it to the primary stage
                    Scene gameScene = new Scene(root, 960, 640);  // Increase the width here (960px for both components)
                    primaryStage.setScene(gameScene);
                    
                    // Ensure the game starts
                    System.out.println("Calling GameLogic.newGame()");
                




            } else {
                System.out.println("Please select a mode.");
            }
        });

        // Add the components to the start pane
        this.getChildren().addAll(title, modeComboBox, startButton);
    }
}