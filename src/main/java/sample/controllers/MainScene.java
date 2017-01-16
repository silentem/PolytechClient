package sample.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import sample.entities.Subject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainScene extends Scene {

    private MainTable mainTable;
    private List<Integer> jsonToDelete;

    public MainScene(Parent root) {
        super(root);


        jsonToDelete = new ArrayList<>();

        mainTable = new MainTable();

        //TODO summary fields, table size, css
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
        });

        deleteGroup.setOnAction(event -> {
            if (mainTable.getGroup() != null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Увага!");
                alert.setHeaderText(null);
                alert.setContentText("Чи ви дійсно бажаєте ВИДАЛИТИ группу?");

                ButtonType yesButton = new ButtonType("Так");
                ButtonType cancelButton = new ButtonType("Відміна", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(yesButton, cancelButton);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == yesButton) {
                    Conn.send(Conn.MAIN_URL + Conn.GROUP_DESTROY_SUFFIX + mainTable.getGroup().getGroupNumber(), null, "DELETE");
                }
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
            Integer groupNumber = null;
            if (list.size() != 0) {
                ChoiceDialog<Integer> dialog = new ChoiceDialog<>(list.get(0), list);
                dialog.setTitle("Відкриття групи");
                dialog.setHeaderText(null);
                dialog.setContentText("Виберіть группу:");
                Optional<Integer> value = dialog.showAndWait();
                groupNumber = null;
                if (value.isPresent()) {
                    groupNumber = value.get();
                }
            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Увага!");
                alert.setHeaderText(null);
                alert.setContentText("Ви не створили жодної группи!");

                ButtonType cancelButton = new ButtonType("ОК", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(cancelButton);
                alert.showAndWait();
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
                        saveGroup();
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
        });

        changeGroup.setOnAction(event -> {
            if(mainTable.getGroup() != null){
                ChangeGroupStage stage = new ChangeGroupStage(mainTable.getGroup());
                stage.initOwner(getWindow());
                stage.showAndWait();
                try {
                    mainTable.initGroup(stage.getGroupNum());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Помилка!");
                alert.setHeaderText(null);
                alert.setContentText("Ви не відкрили групу!");

                ButtonType cancelButton = new ButtonType("ОК", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(cancelButton);
                alert.showAndWait();
            }
        });

        saveGroup.setOnAction(event -> {
            saveGroup();
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
                mainTable.addCompletedHoursColumns(mainTable.getGroup().getSubjects().get(0).getChData().size() - 1);
                JsonArray array = new JsonArray();
                for (Subject s : mainTable.getGroup().getSubjects()) {
                    JsonObject obj = new JsonObject();
                    int last = s.getChData().size() - 1;
                    String lastDate = new SimpleDateFormat("yyyy-MM-dd").format(s.getChData().get(last).getDate());
                    obj.add("for_date", new JsonPrimitive(lastDate));
                    obj.add("completed", new JsonPrimitive(s.getChData().get(last).getCompleted()));
                    obj.add("left", new JsonPrimitive(s.getChData().get(last).getLeftTo()));
                    obj.add("changes", new JsonPrimitive(s.getChangedHoursValue()));
                    obj.add("subject", new JsonPrimitive(s.getId()));
                    array.add(obj);
                }
                mainTable.getJsonToCreate().add(array);

                //TODO realize creating editing title window on double click
            }
        });

        deleteColumn.setOnAction(event -> {
            if (mainTable.getJsonToCreate().size() != 0) mainTable.getJsonToCreate().remove(mainTable.getJsonToCreate().size() - 1);
            else {
                for (Subject s : mainTable.getGroup().getSubjects()) {
                    int last = s.getChData().size() - 1;
                    jsonToDelete.add(s.getChData().get(last).getId());
                }
            }
            int size = mainTable.getColumns().size();
            if (size > 6) {
                mainTable.getColumns().remove(size - 2, size);
                mainTable.getGroup().getSubjects().forEach(Subject::removeCompletedHours);
            }

        });

    }
    private void saveGroup(){
        for (JsonArray arr : mainTable.getJsonToCreate()) {
            Conn.send(Conn.MAIN_URL + Conn.DATE_SUFFIX, arr, "POST");
        }
        mainTable.getJsonToCreate().clear();
        mainTable.getCache().forEach((integer, jsonObject) -> {
            Conn.send(Conn.MAIN_URL + Conn.DATE_UPDATE_SUFFIX + integer + "/", jsonObject, "PUT");
        });
        mainTable.getCache().clear();
        mainTable.getChangedChanges().forEach((integer, jsonObject) -> {
            Conn.send(Conn.MAIN_URL + Conn.CHANGES_UPDATE_SUFFIX + integer + "/", jsonObject, "PUT");
        });
        mainTable.getChangedChanges().clear();
        jsonToDelete.forEach((integer) -> {
            Conn.send(Conn.MAIN_URL + Conn.DATE_DESTROY_SUFFIX + integer + "/", null, "DELETE");
        });
        jsonToDelete.clear();
    }
}
