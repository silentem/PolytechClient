package sample.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class FindGroupStage extends Stage {

    private Integer groupNumber;

    public FindGroupStage() throws IOException {
        super();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/findGroupStage.fxml"));
        Scene scene = new Scene(root);

        TextField textField = ((TextField) scene.lookup("#groupNumber"));

        Button button = ((Button) scene.lookup("#find"));

        button.setOnAction(event -> {
            groupNumber = Integer.parseInt(textField.getText());
            close();
        });

        this.setScene(scene);
        this.initModality(Modality.WINDOW_MODAL);
        this.setTitle("Знайти группу");
        this.centerOnScreen();
        this.setResizable(false);
    }

    public Integer getGroupNumber(){
        return groupNumber;
    }

}
