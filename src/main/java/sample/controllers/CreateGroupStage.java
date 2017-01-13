package sample.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import sample.entities.Subject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CreateGroupStage extends Stage {

    private Integer groupNumber;

    public CreateGroupStage() {

        TextField groupNumberField = new TextField();
        TableView<Subject> subjectTable = new TableView<>();
        subjectTable.getColumns().add(column("Назва предмету", "subjectName"));
        subjectTable.getColumns().add(column("ПІП викладача", "professorInitials"));
        subjectTable.getColumns().add(column("Кількість годин", "hoursQuoteValue"));
        subjectTable.getColumns().add(column("Тижнева к-ть", "weeklyHoursValue"));
        ObservableList<Subject> items = FXCollections.observableArrayList();
        subjectTable.setItems(items);
        Button addSubject = new Button("Додати предмет");
        Button createGroup = new Button("Створити групу");


        addSubject.setOnAction(e -> {
            SubjectCreationStage stage = new SubjectCreationStage();
            stage.showAndWait();
            Subject subject = stage.getNewSubject();
            items.add(subject);
        });

        createGroup.setOnAction(event -> {
            try {
                JsonArray numbers = (JsonArray) Conn.getJson(Conn.MAIN_URL + Conn.GROUPS_SUFFIX);
                List numList = new ArrayList();
                for (JsonElement elem : numbers)
                    numList.add(elem.getAsJsonObject().get("number").getAsInt());

                int number = Integer.parseInt(groupNumberField.getText());
                if (numList.contains(number)){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Помилка!");
                    alert.setHeaderText(null);
                    alert.setContentText("Така група вже існує!");

                    ButtonType cancelButton = new ButtonType("ОК", ButtonBar.ButtonData.CANCEL_CLOSE);

                    alert.getButtonTypes().setAll(cancelButton);
                    alert.showAndWait();
                }else {
                    groupNumber = number;
                    Conn.createGroup(number, items);
                    close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }catch (NumberFormatException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Помилка!");
                alert.setHeaderText(null);
                alert.setContentText("Ви не правильно ввели числові данні!");

                ButtonType cancelButton = new ButtonType("ОК", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(cancelButton);
                alert.showAndWait();
            }
        });

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(groupNumberField, 0, 0);
        grid.add(subjectTable, 0, 1);
        grid.add(addSubject, 0, 2);
        grid.add(createGroup, 1, 2);

        this.setScene(new Scene(new Group()));
        ((Group) getScene().getRoot()).getChildren().addAll(grid);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Створення группи");
        this.centerOnScreen();
        this.setResizable(false);
    }

    private  <S, T> TableColumn<S, T> column(String title,
                                                   String property) {

        TableColumn<S, T> col = new TableColumn<>(title);

        col.setCellValueFactory(new PropertyValueFactory<S, T>(property));
        return col;
    }

    public Integer getGroupNumber() {
        return groupNumber;
    }
}
