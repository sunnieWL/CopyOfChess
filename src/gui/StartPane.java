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

public class StartPane extends VBox {

    public StartPane(Stage primaryStage) {
        // Set a background color for the pane
        this.setStyle("-fx-background-color: #f0f8ff;");  
        this.setSpacing(20);  
        this.setAlignment(Pos.CENTER);  
        this.setPadding(new javafx.geometry.Insets(60)); 

        this.setPrefWidth(800);  
        this.setPrefHeight(600);  

        this.setFillWidth(true); 

        // Title 
        Text title = new Text("Welcome to Chess v2!");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 36));  
        title.setFill(Color.DARKBLUE);

        // ComboBox for selecting game mode
        ComboBox<String> modeComboBox = new ComboBox<>();
        modeComboBox.getItems().addAll("Classic" , "Gamble");
        modeComboBox.setPromptText("Select Game Mode");
        modeComboBox.setStyle("-fx-font-size: 20px; -fx-padding: 15px; -fx-background-color: #ffffff;");
        modeComboBox.setPrefWidth(300); 

        // Start Button
        Button startButton = new Button("Start");
        startButton.setStyle("-fx-font-size: 20px; -fx-padding: 15px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        startButton.setMinWidth(300);  
        startButton.setStyle("-fx-background-radius: 5px; -fx-border-radius: 5px;");  

        // Set the action
        startButton.setOnAction(e -> {
            String selectedMode = modeComboBox.getValue();
            if (selectedMode != null) {
                System.out.println("Starting the game in " + selectedMode + " mode.");

                boolean isGamble = selectedMode.equals("Gamble");
                
                
                Player white = new Player("White", "white");
                Player black = new Player("Black", "black");
                
                ControlPane controlPane = new ControlPane(primaryStage);
                    Game game = new Game(white, black,isGamble,controlPane);  
                    Board board = game.getBoard();
                    
                    ChessBoardView boardView = new ChessBoardView(board, game);
                    
                    HBox root = new HBox(20);
                    root.getChildren().addAll(boardView, controlPane);

                    Scene gameScene = new Scene(root, 960, 660);
                    primaryStage.setScene(gameScene);
                    
                    // Debugging
                    System.out.println("Game is Started");

            } else {
            	// notify player
                System.out.println("Please select a mode.");
            }
        });
        
        Button quitButton = new Button("Quit");
        quitButton.setStyle("-fx-font-size: 20px; -fx-padding: 15px; -fx-background-color: #FF5555; -fx-text-fill: white;");
        quitButton.setMinWidth(300);  
        quitButton.setStyle("-fx-background-radius: 5px; -fx-border-radius: 5px;");  
        quitButton.setOnAction(e -> {
            System.out.println("Exiting the game.");
            primaryStage.close();  
        });
        
        this.getChildren().addAll(title, modeComboBox, startButton,quitButton);
    }
}