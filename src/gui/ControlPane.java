package gui;

import game.Game;
import game.Timer;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pieces.Bishop;
import pieces.Knight;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

public class ControlPane extends VBox {
    private static TimerPane whiteTimerPane;
    private static TimerPane blackTimerPane;
    private Text gameStatusText;
    private static Text turnIndicatorText; 
    private static Piece promotedPiece;
    private static final int WHITE_TIMER_INDEX = 0;
    private static final int BLACK_TIMER_INDEX = 1;
    
    public ControlPane(Stage stage) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
        this.setStyle("-fx-background-color: #2C2C2C; -fx-padding: 20px; -fx-border-color: #444; -fx-border-width: 2px;");
        
        // history pane
        HistoryPane historyPane = new HistoryPane();
        historyPane.setPrefWidth(400);
        historyPane.setPrefHeight(2000);
        historyPane.setStyle("-fx-padding: 10px; -fx-background-color: #1E1E1E; -fx-border-radius: 10px; -fx-background-radius: 10px;");
        this.getChildren().add(historyPane);
        
        whiteTimerPane = new TimerPane(WHITE_TIMER_INDEX);
        blackTimerPane = new TimerPane(BLACK_TIMER_INDEX);
        
        // white and black text label
        Text whiteTimeLabel = new Text("White Time");
        whiteTimeLabel.setFont(new Font("Arial", 18));
        whiteTimeLabel.setFill(Color.WHITE);

        Text blackTimeLabel = new Text("Black Time");
        blackTimeLabel.setFont(new Font("Arial", 18));
        blackTimeLabel.setFill(Color.WHITE);
        
        // GameStatus Text
        gameStatusText = new Text("Game Started!");
        gameStatusText.setFont(new Font("Arial", 24));
        gameStatusText.setFill(Color.LIGHTGREEN);

        turnIndicatorText = new Text("White's Turn");
        turnIndicatorText.setFont(new Font("Arial", 20));
        turnIndicatorText.setFill(Color.LIGHTBLUE);

        this.getChildren().addAll(
            whiteTimeLabel, whiteTimerPane, 
            blackTimeLabel, blackTimerPane, 
            gameStatusText, turnIndicatorText
        );

        VBox promotionButtons = createPromotionButtons();
        this.getChildren().add(promotionButtons);

        Button backButton = new Button("Back to Main Page");
        backButton.setStyle(
            "-fx-font-size: 16px; " +
            "-fx-text-fill: white; " +
            "-fx-background-color: #444; " +
            "-fx-border-radius: 5px; " +
            "-fx-background-radius: 5px; " +
            "-fx-padding: 10px;"
        );

        backButton.setOnAction(e -> {
        	Game.endGame("Back to the main Page");
        	Game.getWhiteTimer().stop();
        	Game.getBlackTimer().stop();
            StartPane startPane = new StartPane(stage);  
            Scene startScene = new Scene(startPane, 800, 600);  
            stage.setScene(startScene);
        });

        this.getChildren().add(backButton);
    }

    private VBox createPromotionButtons() {
        VBox buttonBox = new VBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setStyle("-fx-padding: 10px; -fx-background-color: #1E1E1E; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        Text promotionText = new Text("Pawn Promotion");
        promotionText.setFont(new Font("Arial", 20));
        promotionText.setFill(Color.YELLOW);

        Button queenButton = createStyledButton("Promote to Queen");
        Button rookButton = createStyledButton("Promote to Rook");
        Button bishopButton = createStyledButton("Promote to Bishop");
        Button knightButton = createStyledButton("Promote to Knight");

        highlightButton(queenButton, rookButton, bishopButton, knightButton);

        queenButton.setOnAction(e -> {
            setPromotionPiece(new Queen("white", null));
            highlightButton(queenButton, rookButton, bishopButton, knightButton);
        });
        rookButton.setOnAction(e -> {
            setPromotionPiece(new Rook("white", null));
            highlightButton(rookButton, queenButton, bishopButton, knightButton);
        });
        bishopButton.setOnAction(e -> {
            setPromotionPiece(new Bishop("white", null));
            highlightButton(bishopButton, queenButton, rookButton, knightButton);
        });
        knightButton.setOnAction(e -> {
            setPromotionPiece(new Knight("white", null));
            highlightButton(knightButton, queenButton, rookButton, bishopButton);
        });

        buttonBox.getChildren().addAll(promotionText, queenButton, rookButton, bishopButton, knightButton);
        return buttonBox;
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle(
            "-fx-font-size: 16px; " +
            "-fx-text-fill: white; " +
            "-fx-background-color: #555; " +
            "-fx-border-radius: 5px; " +
            "-fx-background-radius: 5px; " +
            "-fx-padding: 10px;"
        );

        // Set hover for not Green
        button.setOnMouseEntered(e -> {
            if (!button.getStyle().contains("#00AA00")) { // Check Green Button
                button.setStyle(
                    "-fx-font-size: 16px; " +
                    "-fx-text-fill: white; " +
                    "-fx-background-color: #666; " +
                    "-fx-border-radius: 5px; " +
                    "-fx-background-radius: 5px; " +
                    "-fx-padding: 10px;"
                );
            }
        });

        button.setOnMouseExited(e -> {
            if (!button.getStyle().contains("#00AA00")) {  // Check Green Button
                button.setStyle(
                    "-fx-font-size: 16px; " +
                    "-fx-text-fill: white; " +
                    "-fx-background-color: #555; " +
                    "-fx-border-radius: 5px; " +
                    "-fx-background-radius: 5px; " +
                    "-fx-padding: 10px;"
                );
            }
        });

        return button;
    }

    private void highlightButton(Button selected, Button... others) {
        // Highlight button
        selected.setStyle(
            "-fx-font-size: 16px; " +
            "-fx-text-fill: white; " +
            "-fx-background-color: #00AA00; " + 
            "-fx-border-radius: 5px; " +
            "-fx-background-radius: 5px; " +
            "-fx-padding: 10px;"
        );

        // Reset styles 
        for (Button button : others) {
            button.setStyle(
                "-fx-font-size: 16px; " +
                "-fx-text-fill: white; " +
                "-fx-background-color: #555; " +
                "-fx-border-radius: 5px; " +
                "-fx-background-radius: 5px; " +
                "-fx-padding: 10px;"
            );
        }
    }

    public static void updateTimer(int playerIndex, Timer timer) {
        if (playerIndex == WHITE_TIMER_INDEX) {
            whiteTimerPane.setTimer(timer);
        } else {
            blackTimerPane.setTimer(timer);
        }
    }

    public void updateGameText(String message) {
        gameStatusText.setText(message);
    }

    public static void updateTurnIndicator(String playerTurn) {
        turnIndicatorText.setText(playerTurn + "'s Turn");
        turnIndicatorText.setFill(playerTurn.equalsIgnoreCase("white") ? Color.LIGHTBLUE : Color.LIGHTCORAL);
    }
    
    public void updateGameStatusText(String message) {
        gameStatusText.setText(message);
        if(message != "Moved.") {
         gameStatusText.setFill(Color.RED);
        }
    }
    
    private void setPromotionPiece(Piece piece) {
        promotedPiece = piece;
        System.out.println("Selected promotion: " + piece.getClass().getSimpleName());
    }

    public static Piece getPromotedPiece() {
        return promotedPiece;
    }
}