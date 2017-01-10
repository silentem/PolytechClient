package sample.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class AddColumnsStage extends Stage {

    private String date;

    public AddColumnsStage() throws IOException {
        super();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/addColumnStage.fxml"));
        Scene scene = new Scene(root);

        Button createButton = ((Button) scene.lookup("#createButton"));
        DatePicker datePicker = ((DatePicker) scene.lookup("#datePicker"));

        createButton.setOnAction(event -> {
            date = datePicker.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            close();
        });

        this.setScene(scene);
        this.initModality(Modality.WINDOW_MODAL);
        this.setTitle("Створення предмета");
        this.centerOnScreen();
        this.setResizable(false);
    }

    public String getDate() {
        return date;
    }
}
