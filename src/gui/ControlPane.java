package gui;

import game.Timer;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ControlPane extends VBox {

    private TimerPane whiteTimerPane;
    private TimerPane blackTimerPane;
    private Label gameStatusLabel;  // Label to show game status

    public ControlPane() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);

        whiteTimerPane = new TimerPane(0);
        blackTimerPane = new TimerPane(1);

        // Initialize the label for game status
        gameStatusLabel = new Label("Game Started!");  // Initial game status message

        // Add the timer panes and game status label to the control pane
        this.getChildren().addAll(whiteTimerPane, blackTimerPane, gameStatusLabel);
    }

    // Method to update the game status text
    public void updateGameText(String message) {
        gameStatusLabel.setText(message);  // Set the label text to the passed message
    }

    // Method to update the timer for the given player
    public void updateTimer(int player, Timer newTimer) {
        if (player == 0) {
            whiteTimerPane.setTimer(newTimer);
        } else {
            blackTimerPane.setTimer(newTimer);
        }
    }
}