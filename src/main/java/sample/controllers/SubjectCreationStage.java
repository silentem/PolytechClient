package sample.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.entities.Subject;

import java.io.IOException;
import java.util.Optional;

public class SubjectCreationStage extends Stage {

    private Subject resultSubject;

    public SubjectCreationStage(Subject subject) {
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
        this.setTitle("Зміна предмета");
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
                    if (subjectNameField.getText().equals("") ||
                            professorPIPField.getText().equals("") ||
                            totalHoursToFinishField.getText().equals("") ||
                            weeklyHoursField.getText().equals("")) {

                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Помилка!");
                        alert.setHeaderText(null);
                        alert.setContentText("Всі поля повинні бути заповнені!");

                        ButtonType cancelButton = new ButtonType("ОК", ButtonBar.ButtonData.CANCEL_CLOSE);

                        alert.getButtonTypes().setAll(cancelButton);
                        alert.showAndWait();
                    } else {
                        String subjectName = subjectNameField.getText();
                        String professorName = professorPIPField.getText();

                        int totalHoursToFinish = 0;
                        int weeklyHours = 0;
                        try {
                            totalHoursToFinish = Integer.parseInt(totalHoursToFinishField.getText());
                            weeklyHours = Integer.parseInt(weeklyHoursField.getText());
                            resultSubject = new Subject(subjectName, professorName, totalHoursToFinish);
                            resultSubject.setWeeklyHoursValue(weeklyHours);
                            close();
                        } catch (NumberFormatException e) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Помилка!");
                            alert.setHeaderText(null);
                            alert.setContentText("Ви не правильно ввели числові данні!");

                            ButtonType cancelButton = new ButtonType("ОК", ButtonBar.ButtonData.CANCEL_CLOSE);

                            alert.getButtonTypes().setAll(cancelButton);
                            alert.showAndWait();
                        }
                    }
                }
        );
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
