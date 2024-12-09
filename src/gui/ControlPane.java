package gui;


import game.Timer;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pieces.Bishop;
import pieces.Knight;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

public class ControlPane extends VBox {
    private static TimerPane whiteTimerPane;
    private static TimerPane blackTimerPane;
    private Text gameStatusText;
    private static Piece promotedPiece;

    public ControlPane() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
   
        whiteTimerPane = new TimerPane(0);
        blackTimerPane = new TimerPane(1);
        gameStatusText = new Text("Game Started!");

        this.getChildren().addAll(whiteTimerPane, blackTimerPane, gameStatusText);

        VBox promotionButtons = createPromotionButtons();
        this.getChildren().add(promotionButtons);
    }
    
    private VBox createPromotionButtons() {
        VBox buttonBox = new VBox(5);
        buttonBox.setAlignment(Pos.CENTER);

        Button queenButton = new Button("Promote to Queen");
        Button rookButton = new Button("Promote to Rook");
        Button bishopButton = new Button("Promote to Bishop");
        Button knightButton = new Button("Promote to Knight");

        queenButton.setOnAction(e -> setPromotionPiece(new Queen("white", null)));  
        rookButton.setOnAction(e -> setPromotionPiece(new Rook("white", null)));
        bishopButton.setOnAction(e -> setPromotionPiece(new Bishop("white", null)));
        knightButton.setOnAction(e -> setPromotionPiece(new Knight("white", null)));

        buttonBox.getChildren().addAll(queenButton, rookButton, bishopButton, knightButton);
        return buttonBox;
    }
    
    public static void updateTimer(int playerIndex, Timer timer) {
        if (playerIndex == 0) {
            whiteTimerPane.setTimer(timer);
        } else {
            blackTimerPane.setTimer(timer);
        }
    }

    public void updateGameText(String message) {
        gameStatusText.setText(message);
    }
    
    private void setPromotionPiece(Piece piece) {
        this.promotedPiece = piece;
        System.out.println("Selected promotion: " + piece.getClass().getSimpleName());
    }

    public static Piece getPromotedPiece() {
        return promotedPiece;
    }
}
