package sample.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import sample.entities.Group;
import sample.entities.Subject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainTable extends TableView<Subject> {


    private Group group;

    private Map<Integer, JsonObject> jsonToChange;
    private List<JsonArray> jsonToCreate;
    private Map<Integer, JsonObject> changedChanges;

    public MainTable() {

        jsonToChange = new HashMap<>();
        jsonToCreate = new ArrayList<>();
        changedChanges = new HashMap<>();

        TableColumn<Subject, String> subjectColumn = new TableColumn<>("Предмети");
        subjectColumn.setMinWidth(100);
        subjectColumn.setCellValueFactory(
                new PropertyValueFactory<>("subjectName")
        );
        TableColumn<Subject, String> professorColumn = new TableColumn<>("Викладач");
        professorColumn.setMinWidth(100);
        professorColumn.setCellValueFactory(
                new PropertyValueFactory<>("professorInitials")
        );
        professorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        professorColumn.setOnEditCommit(event -> {
            event.getTableView().getItems().get(
                    event.getTablePosition().getRow())
                    .setProfessorInitials(event.getNewValue());
        });
        TableColumn<Subject, Integer> totalHoursColumn = new TableColumn<>("К-ть годин");
        totalHoursColumn.setMinWidth(100);
        totalHoursColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getTotalHours())
        );
        TableColumn<Subject, Integer> changedHoursColumn = new TableColumn<>("Зняття годин");
        changedHoursColumn.setMinWidth(100);
        changedHoursColumn.setCellValueFactory(
                new PropertyValueFactory<>("changedHoursValue")
        );
        changedHoursColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>() {

            @Override
            public String toString(Integer object) {
                return object.toString();
            }

            @Override
            public Integer fromString(String string) {
                return Integer.parseInt(string);
            }

        }));
        changedHoursColumn.setOnEditCommit(event -> {
            Integer value = event.getNewValue();
            Subject subject = event.getTableView().getItems().get(
                    event.getTablePosition().getRow());
            subject.setChangedHoursValue(value);

            JsonObject obj = new JsonObject();
            obj.add("changes", new JsonPrimitive(value));
            obj.add("subject", new JsonPrimitive(subject.getId()));
            changedChanges.put(subject.getId(), obj);

            this.refresh();
        });
        TableColumn<Subject, String> totalHoursToFinishColumn = new TableColumn<>("К-ть годин до виконання");
        totalHoursToFinishColumn.setMinWidth(100);
        totalHoursToFinishColumn.setCellValueFactory(
                new PropertyValueFactory<>("hoursQuoteValue")
        );
        TableColumn<Subject, String> weeklyHoursColumn = new TableColumn<>("Тижневе навантаження");
        weeklyHoursColumn.setMinWidth(100);
        weeklyHoursColumn.setCellValueFactory(
                new PropertyValueFactory<>("weeklyHoursValue")
        );

        this.getColumns().addAll(
                subjectColumn,
                professorColumn,
                totalHoursColumn,
                changedHoursColumn,
                totalHoursToFinishColumn,
                weeklyHoursColumn);
    }

    public Group getGroup() {
        return group;
    }

    public void initGroup(Integer number) throws IOException {

        group = new Group(number);

        JsonArray jsonSubjects =
                (JsonArray) Conn.getJson(Conn.MAIN_URL + Conn.SUBJECT_GROUP_SUFFIX + number + "/");
        JsonArray jsonChanges =
                (JsonArray) Conn.getJson(Conn.MAIN_URL + Conn.CHANGES_GET_SUFFIX + number + "/");

        for (JsonElement jsonSubject : jsonSubjects) {

            JsonObject jsonObject = jsonSubject.getAsJsonObject();
            String subjectName = jsonObject.get("name").getAsString();
            String professorsInitials = jsonObject.get("professor").getAsString();
            Integer hoursQuoteValue = jsonObject.get("SEMESTERS_HOUR").getAsInt();
            Integer weeklyHours = jsonObject.get("week_load").getAsInt();
            Integer subjectId = jsonObject.get("id").getAsInt();

            Subject subject = new Subject(subjectName, professorsInitials, hoursQuoteValue);
            subject.setWeeklyHoursValue(weeklyHours);
            subject.setId(subjectId);

            for (JsonElement elem : jsonChanges) {
                if (elem.getAsJsonObject().get("subject").getAsInt() == subjectId)
                    subject.setChangedHoursValue(elem.getAsJsonObject().get("changes").getAsInt());
            }

            JsonArray jsonAdditionalValues =
                    (JsonArray) Conn.getJson(Conn.MAIN_URL + Conn.DATE_FOR_SUBJECT_SUFFIX + subjectId + "/");

            for (JsonElement jsonElement : jsonAdditionalValues) {
                JsonObject jsonValue = jsonElement.getAsJsonObject();
                String sDate = jsonValue.get("for_date").getAsString();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = df.parse(sDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Integer value = jsonValue.get("completed").getAsInt();
                Integer id = jsonValue.get("id").getAsInt();
                Integer valueLeft = jsonValue.get("left").getAsInt();
                subject.addCompletedHours(date, value, valueLeft, id);
            }
            group.addSubject(subject);
        }

        if (group != null) distributeGroupToTable();
        if (group != null) fillCompletedHoursColumns();

    }

    private void distributeGroupToTable() {

        ObservableList<Subject> subjects = FXCollections.observableArrayList();
        subjects.addAll(group.getSubjects());
        this.setItems(subjects);

    }

    public void fillCompletedHoursColumns() {
        if (group == null) return;
        if (group.getSubjects().size() == 0) return;

        this.getColumns().remove(6, this.getColumns().size() - 1);

        for (int i = 0; i < getGroup().getSubjects().get(0).getChData().size(); i++) {
            addCompletedHoursColumns(i);
        }

    }

    public void addCompletedHours(Date date) {
        for (Subject subject : group.getSubjects()) {
            if (subject.getChData().size() == 0) {
                Integer total = subject.getTotalHours();
                subject.addCompletedHours(date, subject.getWeeklyHoursValue(), total - subject.getWeeklyHoursValue());
            } else {
                Integer prevLeft = subject.getChData().get(subject.getChData().size() - 1).getLeftTo();
                subject.addCompletedHours(date, subject.getWeeklyHoursValue(), prevLeft - subject.getWeeklyHoursValue());
            }
        }
    }

    public void addCompletedHoursColumns(int pos) {
        if (group == null) return;

        String sDate = new SimpleDateFormat("dd.MM.yyyy").format(getGroup().getSubjects().get(0).getChData().get(pos).getDate());
        TableColumn<Subject, Integer> completedColumn = new TableColumn<>("В " + sDate);
        completedColumn.setMinWidth(100);

        //creating cell value on action
        completedColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>() {

            @Override
            public String toString(Integer object) {
                return object.toString();
            }

            @Override
            public Integer fromString(String string) {
                return Integer.parseInt(string);
            }

        }));
        completedColumn.setOnEditCommit(event -> {
            Integer value = event.getNewValue();
            int position = (event.getTablePosition().getColumn() - 6) / 2;
            Subject subject = event.getTableView().getItems().get(
                    event.getTablePosition().getRow());
            subject.setCompletedHours(value, position);
            subject.setVarValChanged(true);

            for (int i = position; i < subject.getChData().size(); i++) {
                JsonObject obj = new JsonObject();
                String lastDate = new SimpleDateFormat("yyyy-MM-dd").format(subject.getChData().get(i).getDate());
                obj.add("subject", new JsonPrimitive(subject.getId()));
                obj.add("for_date", new JsonPrimitive(lastDate));
                obj.add("completed", new JsonPrimitive(subject.getChData().get(i).getCompleted()));
                obj.add("left", new JsonPrimitive(subject.getChData().get(i).getLeftTo()));
                if (subject.getChData().get(i).getId() == null) {
                    int subPos = i - (subject.getChData().size() - jsonToCreate.size());
                    jsonToCreate.get(subPos).set(event.getTablePosition().getRow(), obj);
                } else {
                    jsonToChange.put(subject.getChData().get(i).getId(), obj);
                }
            }

            this.refresh();
        });

        //creating value factory for completed column
        completedColumn.setCellValueFactory(p -> {

            //getting subject into variable

            Subject subject = p.getValue();

            //getting dateKey for SSP()

            Integer value = subject.getChData().get(pos).getCompleted();

            //returning property
            return new SimpleObjectProperty<>(value);
        });

        //creating leftColumn
        TableColumn<Subject, Integer> leftColumn = new TableColumn<>("З " + sDate);
        leftColumn.setMinWidth(100);

        //creating valueFactory
        leftColumn.setCellValueFactory(param -> {

            //getting subject into variable
            Subject subject = param.getValue();

            Integer value = subject.getChData().get(pos).getLeftTo();
            //getting dateKey for SSP()

            //returning property
            return new SimpleObjectProperty<>(value);
        });

        //adding created columns to the table
        this.getColumns().addAll(
                completedColumn,
                leftColumn);
    }

    public Map<Integer, JsonObject> getCache() {
        return jsonToChange;
    }

    public List<JsonArray> getJsonToCreate() {
        return jsonToCreate;
    }

    public Map<Integer, JsonObject> getChangedChanges() {
        return changedChanges;
    }
}
