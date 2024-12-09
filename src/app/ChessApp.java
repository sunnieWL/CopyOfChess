package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import gui.StartPane;  


public class ChessApp extends Application {

    @Override
    public void start(Stage primaryStage) {

        StartPane startPane = new StartPane(primaryStage);  


        Scene startScene = new Scene(startPane, 800, 600);
        

        primaryStage.setTitle("Chess Game");
        primaryStage.setScene(startScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}