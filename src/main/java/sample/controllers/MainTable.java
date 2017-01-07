package sample.controllers;

import com.google.gson.*;
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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainTable extends TableView<Subject> {


    private Group group = new Group(13);

    public MainTable() {

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
        TableColumn<sample.entities.Subject, Integer> changedHoursColumn = new TableColumn<>("Зняття годин");
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
            event.getTableView().getItems().get(
                    event.getTablePosition().getRow())
                    .setChangedHoursValue(value);
            this.refresh();
        });
        //TODO convert subjects to JSON
        //TODO set editable cells
        TableColumn<sample.entities.Subject, String> totalHoursToFinishColumn = new TableColumn<>("К-ть годин до виконання");
        totalHoursToFinishColumn.setMinWidth(100);
        totalHoursToFinishColumn.setCellValueFactory(
                new PropertyValueFactory<>("hoursQuoteValue")
        );
        TableColumn<sample.entities.Subject, String> weeklyHoursColumn = new TableColumn<>("Тижневе навантаження");
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
        if (group == null) throw new NullPointerException();
        return group;
    }

    public void initGroup(Integer number) throws IOException {

        JsonArray jsonSubjects =
                Connection.getJson(Connection.MAIN_URL + Connection.SUBJECTS_UPLOAD_SUFFIX + number + Connection.JSON_SUFFIX);

        group = new Group(number);

        for (JsonElement jsonSubject : jsonSubjects) {

            JsonObject jsonObject = jsonSubject.getAsJsonObject();
            String subjectName = jsonObject.get("name").getAsString();
            String professorsInitials = jsonObject.get("professor").getAsString();
            Integer hoursQuoteValue = jsonObject.get("SEMESTERS_HOUR").getAsInt();
            Integer changesHoursValue = jsonObject.get("changes").getAsInt();
            Integer weeklyHours = jsonObject.get("week_load").getAsInt();
            Integer subjectId = jsonObject.get("id").getAsInt();

            Subject subject = new Subject(subjectName, professorsInitials, hoursQuoteValue);
            subject.setWeeklyHoursValue(weeklyHours);
            subject.setChangedHoursValue(changesHoursValue);

            JsonArray jsonAdditionalValues =
                    Connection.getJson(Connection.MAIN_URL + Connection.CHANGES_SUFFIX + subjectId + Connection.JSON_SUFFIX);

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
                subject.addCompletedHours(date, value);
            }

            group.addSubject(subject);
        }

        if (group != null) fillCompletedHoursColumns();
        if (group != null) distributeGroupToTable();

    }

    private void distributeGroupToTable() {

        ObservableList<Subject> subjects = FXCollections.observableArrayList();
        subjects.addAll(group.getSubjects());
        this.setItems(subjects);

    }

    public void deleteSubjectFromGroup(Integer index) {
        if (group != null) return;
        if (group.getSubjects().size() > index)
            group.getSubjects().remove(index);
    }

    public void addSubjectToGroup(Subject subject) {

        if (group == null) return;
        group.addSubject(subject);
        distributeGroupToTable();
    }

    public void fillCompletedHoursColumns() {
        List<Date> dates;
        if (group == null) return;
        if (group.getSubjects().size() == 0) return;

        dates = group.getSubjects().get(0).getDates();

        for (int i = 0; i < dates.size(); i++) {
            //getting date from array
            Date date = ((Date) dates.toArray()[i]);

            addCompletedHoursColumns(date);
        }
    }

    public void addCompletedHoursColumns(Date date) {
        if (group == null) return;
        for (Subject subject : group.getSubjects()) {
            subject.addCompletedHours(date, subject.getWeeklyHoursValue());
        }

        String sDate = new SimpleDateFormat("dd.MM.yyyy").format(date);
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
            int pos = event.getTablePosition().getColumn();
            event.getTableView().getItems().get(
                    event.getTablePosition().getRow()).setCompletedHours(value, (pos - 6)/2);

            this.refresh();
        });

        int last = group.getSubjects().get(0).getCompletedHours().size() - 1;

        //creating value factory for completed column
        completedColumn.setCellValueFactory(p -> {

            //getting subject into variable
            Subject subject = p.getValue();

            //getting dateKey for SSP()
            Integer value = subject.getCompletedHours().get(last);

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

            //getting dateKey for SSP()
            Integer value = subject.getLeftToDoHours().get(last);

            //returning property
            return new SimpleObjectProperty<>(value);
        });

        //adding created columns to the table
        this.getColumns().addAll(
                completedColumn,
                leftColumn);
//    distributeGroupToTable();

    }

}
