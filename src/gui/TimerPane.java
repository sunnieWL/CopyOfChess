package gui;

import game.Timer;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class TimerPane extends VBox {
    private Text timerText;
    private int playerIndex;
    
    public TimerPane(int playerIndex) {
        this.playerIndex = playerIndex;
        timerText = new Text("00:00:00");
        this.getChildren().add(timerText);
    }

    public void setTimer(Timer timer) {
        timerText.setText(timer.toString());
    }
}

