package sample.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.entities.Subject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeGroupStage extends Stage {

    private List<Integer> idToDelete;
    private Map<Integer, JsonObject> jsonToChange;
    private List<JsonObject> jsonToAdd;

    private int groupNum;

    public ChangeGroupStage(sample.entities.Group group) {

        idToDelete = new ArrayList<>();
        jsonToChange = new HashMap<>();
        jsonToAdd = new ArrayList<>();
        groupNum = group.getGroupNumber();

        TextField groupNumberField = new TextField();
        groupNumberField.setText(String.valueOf(group.getGroupNumber()));
        groupNumberField.setEditable(false);
        TableView<Subject> subjectTable = new TableView<>();
        subjectTable.getColumns().add(column("Назва предмету", "subjectName"));
        subjectTable.getColumns().add(column("ПІП викладача", "professorInitials"));
        subjectTable.getColumns().add(column("Кількість годин", "hoursQuoteValue"));
        subjectTable.getColumns().add(column("Тижнева к-ть", "weeklyHoursValue"));
        ObservableList<Subject> items = FXCollections.observableArrayList();
        items.addAll(group.getSubjects());
        subjectTable.setItems(items);
        Button addSubject = new Button("Додати предмет");
        Button deleteSubject = new Button("Видалити предмет");
        Button changeSubject = new Button("Змінити предмет");
        Button changeGroup = new Button("Змінити групу");


        addSubject.setOnAction(e -> {
            SubjectCreationStage stage = new SubjectCreationStage();
            stage.showAndWait();
            Subject subject = stage.getNewSubject();
            if (subject != null){
                items.add(subject);
                JsonObject obj = new JsonObject();
                obj.add("name", new JsonPrimitive(subject.getSubjectName()));
                obj.add("professor", new JsonPrimitive(subject.getProfessorInitials()));
                obj.add("SEMESTERS_HOUR", new JsonPrimitive(subject.getHoursQuoteValue()));
                obj.add("week_load", new JsonPrimitive(subject.getWeeklyHoursValue()));
                obj.add("group", new JsonPrimitive(group.getGroupNumber()));
                jsonToAdd.add(obj);
            }
        });

        deleteSubject.setOnAction(event -> {
            Subject currentSubject = subjectTable.getSelectionModel().selectedItemProperty().get();
            if (currentSubject != null) {
                int id = currentSubject.getId();
                items.remove(currentSubject);
                idToDelete.add(id);
            }
        });

        changeSubject.setOnAction(event -> {
            Subject currentSubject = subjectTable.getSelectionModel().selectedItemProperty().get();
            if (currentSubject != null) {
                SubjectCreationStage stage = new SubjectCreationStage(currentSubject);
                stage.showAndWait();
                if (stage.getNewSubject() != null) {
                    Subject changedSubject = stage.getNewSubject();
                    if (!(changedSubject.getSubjectName().equals(currentSubject.getSubjectName())) ||
                            !(changedSubject.getProfessorInitials().equals(changedSubject.getProfessorInitials())) ||
                            changedSubject.getHoursQuoteValue() != currentSubject.getHoursQuoteValue() ||
                            changedSubject.getWeeklyHoursValue() != currentSubject.getWeeklyHoursValue()) {

                        JsonObject obj = new JsonObject();
                        obj.add("name", new JsonPrimitive(changedSubject.getSubjectName()));
                        obj.add("professor", new JsonPrimitive(changedSubject.getProfessorInitials()));
                        obj.add("SEMESTERS_HOUR", new JsonPrimitive(changedSubject.getHoursQuoteValue()));
                        obj.add("week_load", new JsonPrimitive(changedSubject.getWeeklyHoursValue()));
                        obj.add("group", new JsonPrimitive(group.getGroupNumber()));
                        jsonToChange.put(currentSubject.getId(), obj);

                        currentSubject.setSubjectName(changedSubject.getSubjectName());
                        currentSubject.setProfessorInitials(changedSubject.getProfessorInitials());
                        currentSubject.setHoursQuoteValue(changedSubject.getHoursQuoteValue());
                        currentSubject.setWeeklyHoursValue(changedSubject.getWeeklyHoursValue());
                        subjectTable.refresh();
                        System.out.println(jsonToChange.get(0));
                    }
                }
            }
        });

        changeGroup.setOnAction(event -> {
            for (int id : idToDelete)
                Conn.send(Conn.MAIN_URL + Conn.SUBJECT_DESTROY_SUFFIX + id + "/", null, "DELETE");
            jsonToChange.forEach( (integer, jsonObject) -> {
                Conn.send(Conn.MAIN_URL + Conn.SUBJECT_UPDATE_SUFFIX + integer +"/", jsonObject, "PUT");
            });
            jsonToAdd.forEach((jsonObject) -> {
                Conn.send(Conn.MAIN_URL + Conn.SUBJECTS_SUFFIX, jsonObject, "POST");
            });
            close();
        });

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(groupNumberField, 0, 0);
        grid.add(subjectTable, 0, 1);
        grid.add(addSubject, 0, 2);
        grid.add(changeGroup, 1, 2);
        grid.add(changeSubject, 0, 3);
        grid.add(deleteSubject, 1, 3);

        this.setScene(new Scene(new Group()));
        ((Group) getScene().getRoot()).getChildren().addAll(grid);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Створення группи");
        this.centerOnScreen();
        this.setResizable(false);
    }

    private <S, T> TableColumn<S, T> column(String title,
                                            String property) {

        TableColumn<S, T> col = new TableColumn<>(title);

        col.setCellValueFactory(new PropertyValueFactory<S, T>(property));
        return col;
    }

    public int getGroupNum() {
        return groupNum;
    }
}
