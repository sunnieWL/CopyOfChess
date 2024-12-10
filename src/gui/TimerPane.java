package gui;

import game.Timer;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;

public class TimerPane extends VBox {
    private Text timerText;
    
    public TimerPane(int playerIndex) {
    	this.getChildren().clear();
    	
        timerText = new Text("00:05:00");
        
        timerText.setFont(new Font("Arial", 20)); 
        timerText.setFill(Color.LIGHTGRAY); 

        this.getChildren().add(timerText);

        this.setStyle("-fx-padding: 10px; -fx-alignment: center;");
    }

    public void setTimer(Timer timer) {
        timerText.setText(timer.toString());
    }
}