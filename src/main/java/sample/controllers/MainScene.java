package sample.controllers;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainScene extends Scene {

    public MainScene(Parent root) {
        super(root);

        MainTable mainTable = new MainTable();
//        mainTable.initGroup(35);

        MenuBar menuBar = new MenuBar();

        Menu groupMenu = new Menu("Группа");
        MenuItem createGroup = new MenuItem("Створити групу");
        MenuItem deleteGroup = new MenuItem("Видалити группу");
        MenuItem openGroup = new MenuItem("Відкрити группу");
        MenuItem saveGroup = new MenuItem("Зберегти группу");
        groupMenu.getItems().addAll(createGroup, deleteGroup, openGroup, saveGroup);

        Menu subjectMenu = new Menu("Предмет");
        MenuItem createSubject = new MenuItem("Створити предмет");
        MenuItem deleteSubject = new MenuItem("Видалиит предмет");
        MenuItem changeSubject = new MenuItem("Змінити предмет");
        subjectMenu.getItems().addAll(createSubject, deleteSubject, changeSubject);

        Menu columnMenu = new Menu("Стовпець");
        MenuItem addColumn = new MenuItem("Додати стовпець");
        MenuItem deleteColumn = new MenuItem("Видалити стовпець");
        columnMenu.getItems().addAll(addColumn, deleteColumn);

        menuBar.getMenus().addAll(groupMenu, subjectMenu, columnMenu);

        mainTable.setEditable(true);

        VBox container = new VBox();
        container.getChildren().addAll(menuBar, mainTable);


        ((javafx.scene.Group) getRoot()).getChildren().addAll(container);


        //TODO create summary fields

        openGroup.setOnAction(event -> {

            FindGroupStage findGroupStage = null;
            try {
                findGroupStage = new FindGroupStage();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (findGroupStage != null)
                findGroupStage.showAndWait();

        });

        saveGroup.setOnAction(event -> {
            try {
                Connection.saveGroupToServer(mainTable.getGroup().getStaticValuesJSON());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        createSubject.setOnAction(event -> {
            SubjectCreationStage subjectCreationStage = new SubjectCreationStage();
            subjectCreationStage.initOwner(getWindow());
            subjectCreationStage.showAndWait();
            mainTable.addSubjectToGroup(subjectCreationStage.getNewSubject());
        });
        deleteSubject.setOnAction(event -> {
            Integer index = mainTable.getFocusModel().getFocusedIndex();
            mainTable.deleteSubjectFromGroup(index);
        });
        addColumn.setOnAction(event -> {
            AddColumnsStage window = null;
            try {
                window = new AddColumnsStage();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert window != null;
            window.showAndWait();
            if (window.getDate() != null) {

                String sDate = window.getDate();
                DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                Date date = null;
                try {
                    date = df.parse(sDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                mainTable.addCompletedHoursColumns(date);

                //TODO editable fields and save info in subject's connected arrays
                //TODO realize creating editing title window on double click
            }
        });

        deleteColumn.setOnAction(event -> {
            int size = mainTable.getColumns().size();
            if (size > 6) mainTable.getColumns().remove(size - 2, size);
        });
    }
}
