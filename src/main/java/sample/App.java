package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.stage.Stage;
import sample.controllers.MainScene;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("College Project Client (alpha 1.3)");

        MainScene scene = new MainScene(new Group());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
