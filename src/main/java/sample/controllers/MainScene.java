package sample.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import sample.entities.Group;
import sample.entities.Subject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class MainScene extends Scene {

    public MainScene(Parent root) {
        super(root);

        MainTable mainTable = new MainTable();

        //TODO summary fields, table size, css
        //TODO create attention windows
        //TODO rework some dialog windows
        //TODO CLEAR CODE

        MenuBar menuBar = new MenuBar();

        Menu groupMenu = new Menu("Группа");
        MenuItem createGroup = new MenuItem("Створити групу");
        MenuItem deleteGroup = new MenuItem("Видалити группу");
        MenuItem openGroup = new MenuItem("Відкрити группу");
        MenuItem changeGroup = new MenuItem("Змінити группу");
        MenuItem saveGroup = new MenuItem("Зберегти группу");
        groupMenu.getItems().addAll(createGroup, deleteGroup, openGroup, changeGroup, saveGroup);

        Menu columnMenu = new Menu("Стовпець");
        MenuItem addColumn = new MenuItem("Додати стовпець");
        MenuItem deleteColumn = new MenuItem("Видалити стовпець");
        columnMenu.getItems().addAll(addColumn, deleteColumn);

        menuBar.getMenus().addAll(groupMenu, columnMenu);

        mainTable.setEditable(true);

        VBox container = new VBox();
        container.getChildren().addAll(menuBar, mainTable);


        ((javafx.scene.Group) getRoot()).getChildren().addAll(container);

        createGroup.setOnAction(event -> {
            CreateGroupStage groupCreation = new CreateGroupStage();
            groupCreation.initOwner(getWindow());
            groupCreation.showAndWait();
            try {
                if (groupCreation.getGroupNumber() != null)
                    mainTable.initGroup(groupCreation.getGroupNumber());
            } catch (IOException e) {
                e.printStackTrace();
            }
            //TODO create method to create group
        });

        deleteGroup.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Увага!");
            alert.setHeaderText(null);
            alert.setContentText("Чи ви дійсно бажаєте ВИДАЛИТИ группу?");

            ButtonType yesButton = new ButtonType("Так");
            ButtonType cancelButton = new ButtonType("Відміна", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(yesButton, cancelButton);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == yesButton) {
                //TODO delete group
            }
        });

        openGroup.setOnAction(event -> {

            JsonArray numbers = null;
            try {
                numbers = (JsonArray) Conn.getJson(Conn.MAIN_URL + Conn.GROUPS_SUFFIX);
            } catch (IOException e) {
                e.printStackTrace();
            }


            List<Integer> list = new ArrayList<>();
            for (JsonElement element : numbers) {
                list.add(element.getAsJsonObject().get("number").getAsInt());
            }
            ChoiceDialog<Integer> dialog = new ChoiceDialog<>(list.get(0), list);
            dialog.setTitle("Відкриття групи");
            dialog.setHeaderText(null);
            dialog.setContentText("Виберіть группу:");
            Optional<Integer> value = dialog.showAndWait();
            Integer groupNumber = null;
            if (value.isPresent()) {
                groupNumber = value.get();
            }
            if (groupNumber != null) {
                if (mainTable.getGroup() != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Увага!");
                    alert.setHeaderText(null);
                    alert.setContentText("Чи зберегти теперішню группу?");

                    ButtonType yesButton = new ButtonType("Так");
                    ButtonType noButton = new ButtonType("Ні");
                    ButtonType cancelButton = new ButtonType("Відміна", ButtonBar.ButtonData.CANCEL_CLOSE);

                    alert.getButtonTypes().setAll(yesButton, noButton, cancelButton);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == yesButton) {
                        //TODO save old group and open new
                    } else if (result.get() == noButton) {
                        try {
                            mainTable.initGroup(groupNumber);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                } else try {
                    mainTable.initGroup(groupNumber);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //TODO add getting subjects list from server and choosing logic

        });

        changeGroup.setOnAction(event -> {

        });

        saveGroup.setOnAction(event -> {
            //TODO add saving variable values
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

                mainTable.addCompletedHours(date);
                mainTable.addCompletedHoursColumns(date);

                //TODO realize creating editing title window on double click
            }
        });

        deleteColumn.setOnAction(event -> {
            int size = mainTable.getColumns().size();
            if (size > 6) mainTable.getColumns().remove(size - 2, size);
        });
    }

    public void saveGroup(Group group) {
        for (Subject subject : group.getSubjects()) {
            if (subject.getId() == null) {

//                Conn.sendPOST(Conn.MAIN_URL + Conn.SUBJECTS_SUFFIX, );
            }
        }
    }
}
