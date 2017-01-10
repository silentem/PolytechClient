package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.control.Control;
import javafx.stage.Stage;
import sample.controllers.MainScene;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("College Project Client (alpha 1.4)");

        MainScene scene = new MainScene(new Group());

        primaryStage.setWidth(1900);
        primaryStage.setHeight(1000);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
