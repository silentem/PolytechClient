package sample.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.entities.Subject;

import java.io.IOException;

public class SubjectCreationStage extends Stage {

    private Subject resultSubject;

    public SubjectCreationStage() {
        super();

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/subjectCreationStage.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);

        TextField subjectNameField = ((TextField) scene.lookup("#subjectName"));
        TextField professorPIPField = ((TextField) scene.lookup("#professorPIP"));
        TextField changedHoursField = ((TextField) scene.lookup("#changedHoursValue"));
        TextField totalHoursToFinishField = ((TextField) scene.lookup("#hoursQuoteValue"));
        TextField weeklyHoursField = ((TextField) scene.lookup("#weeklyHoursValue"));

        Button createButton = ((Button) scene.lookup("#create"));

        createButton.setOnAction(event -> {

            String subjectName = subjectNameField.getText();
            String professorName = professorPIPField.getText();

            int changedHours = Integer.parseInt(changedHoursField.getText());
            int totalHoursToFinish = Integer.parseInt(totalHoursToFinishField.getText());
            int weeklyHours = Integer.parseInt(weeklyHoursField.getText());

            resultSubject = new Subject(subjectName, professorName, totalHoursToFinish);
            resultSubject.setChangedHoursValue(changedHours);
            resultSubject.setWeeklyHoursValue(weeklyHours);

            close();

        });

        this.setScene(scene);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Створення предмета");
        this.centerOnScreen();
        this.setResizable(false);
    }

    public Subject getNewSubject() {
        return resultSubject;
    }
}
