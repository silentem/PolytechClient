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

    public SubjectCreationStage(Subject subject){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/subjectCreationStage.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);

        TextField subjectNameField = ((TextField) scene.lookup("#subjectName"));
        subjectNameField.setText(subject.getSubjectName());
        TextField professorPIPField = ((TextField) scene.lookup("#professorPIP"));
        professorPIPField.setText(subject.getProfessorInitials());
        TextField hoursQuoteField = ((TextField) scene.lookup("#hoursQuoteValue"));
        hoursQuoteField.setText(String.valueOf(subject.getHoursQuoteValue()));
        TextField weeklyHoursField = ((TextField) scene.lookup("#weeklyHoursValue"));
        weeklyHoursField.setText(String.valueOf(subject.getTotalHours()));

        Button createButton = ((Button) scene.lookup("#create"));

        createButton.setOnAction(event -> {

            String subjectName = subjectNameField.getText();
            String professorName = professorPIPField.getText();

            int totalHoursToFinish = Integer.parseInt(hoursQuoteField.getText());
            int weeklyHours = Integer.parseInt(weeklyHoursField.getText());

            resultSubject = new Subject(subjectName, professorName, totalHoursToFinish);
            resultSubject.setWeeklyHoursValue(weeklyHours);

            close();

        });
        this.setScene(scene);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Створення предмета");
        this.centerOnScreen();
        this.setResizable(false);
    }

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
        TextField totalHoursToFinishField = ((TextField) scene.lookup("#hoursQuoteValue"));
        TextField weeklyHoursField = ((TextField) scene.lookup("#weeklyHoursValue"));

        Button createButton = ((Button) scene.lookup("#create"));

        createButton.setOnAction(event -> {

            String subjectName = subjectNameField.getText();
            String professorName = professorPIPField.getText();

            int totalHoursToFinish = Integer.parseInt(totalHoursToFinishField.getText());
            int weeklyHours = Integer.parseInt(weeklyHoursField.getText());

            resultSubject = new Subject(subjectName, professorName, totalHoursToFinish);
            resultSubject.setWeeklyHoursValue(weeklyHours);

            close();

        });

        this.setScene(scene);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Створення предмета");
        this.centerOnScreen();
        this.setResizable(false);
    }

    private void init(){

    }

    public Subject getNewSubject() {
        return resultSubject;
    }
}
