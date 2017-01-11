package sample.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateGroupStage extends Stage{

    private int groupNumber;

    public CreateGroupStage(){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/createStageGroup.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);

        TextField textField = ((TextField) scene.lookup("#groupNumber"));
        Button createButton = ((Button) scene.lookup("#create"));

        createButton.setOnAction(event -> {
            groupNumber = Integer.parseInt(textField.getText());
            close();
        });

        this.setScene(scene);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Створення группи");
        this.centerOnScreen();
        this.setResizable(false);
    }

    public Integer getGroupNumber() {
        return groupNumber;
    }
}
