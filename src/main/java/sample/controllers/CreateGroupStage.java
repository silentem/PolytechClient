package sample.controllers;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import sample.entities.Subject;

import java.io.IOException;
import java.util.function.Function;

public class CreateGroupStage extends Stage{

    private int groupNumber;

    public CreateGroupStage(){

        TextField groupNumber = new TextField();
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

        createGroup.setOnAction(e -> {
            try {
                Conn.createGroup(Integer.parseInt(groupNumber.getText()), items);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            close();
        });

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(groupNumber, 0, 0);
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

    private static <S, T> TableColumn<S, T> column(String title,
                                                   String property) {

        TableColumn<S, T> col = new TableColumn<>(title);

        col.setCellValueFactory(new PropertyValueFactory<S, T>(property));
        return col;
    }

    public Integer getGroupNumber() {
        return groupNumber;
    }
}
