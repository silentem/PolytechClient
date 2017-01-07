package sample.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entity class of subject
 * with all needed info
 */
public class Subject {

    private SimpleStringProperty subjectName;
    private SimpleStringProperty professorInitials;
    private SimpleIntegerProperty changedHoursValue;
    private SimpleIntegerProperty hoursQuoteValue;
    private SimpleIntegerProperty weeklyHoursValue;
    private List<Date> dates;
    private List<Integer> completedHours;
    private List<Integer> leftToDoHours;

    public Subject(String subjectName, String professorInitials, int hoursQuoteValue) {
        this.setSubjectName(subjectName);
        this.setProfessorInitials(professorInitials);
        setChangedHoursValue(0);
        this.setHoursQuoteValue(hoursQuoteValue);
        setWeeklyHoursValue(0);
        dates = new ArrayList<>();
        completedHours = new ArrayList<>();
        leftToDoHours = new ArrayList<>();
    }

    public String getSubjectName() {
        return subjectName.get();
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = new SimpleStringProperty(subjectName);
    }

    public String getProfessorInitials() {
        return professorInitials.get();
    }

    public void setProfessorInitials(String professorInitials) {
        this.professorInitials = new SimpleStringProperty(professorInitials);
    }

    public Integer getTotalHours() {
        return hoursQuoteValue.get() + changedHoursValue.get();
    }

    public Integer getChangedHoursValue() {
        return changedHoursValue.get();
    }

    public void setChangedHoursValue(Integer changedHoursValue) {
        this.changedHoursValue = new SimpleIntegerProperty(changedHoursValue);
    }

    public int getWeeklyHoursValue() {
        return weeklyHoursValue.get();
    }

    public void setWeeklyHoursValue(int weeklyHoursValue) {
        this.weeklyHoursValue = new SimpleIntegerProperty(weeklyHoursValue);
    }

    public int getHoursQuoteValue() {
        return hoursQuoteValue.get();
    }

    public void setHoursQuoteValue(int hoursQuoteValue) {
        this.hoursQuoteValue = new SimpleIntegerProperty(hoursQuoteValue);
    }

    public List<Date> getDates() {
        return dates;
    }

    public List<Integer> getCompletedHours() {
        return completedHours;
    }

    public List<Integer> getLeftToDoHours() {
        return leftToDoHours;
    }

    private void setLeftToDoHours(Integer value, int pos) {
        leftToDoHours.set(pos, value);
    }

    private void addDate(Date date) {
        dates.add(date);
    }

    private void setDate(Date date, int pos) {
        dates.add(pos, date);
    }

    private void addLeftToDOHours(Integer value) {
        leftToDoHours.add(value);
    }

    public void addCompletedHours(Date date, Integer value) {
        addDate(date);
        completedHours.add(value);
        int size = getCompletedHours().size();
        if (size == 1) addLeftToDOHours(getTotalHours() - value);
        else addLeftToDOHours(getLeftToDoHours().get(size - 2) - value);

    }

    public void setCompletedHours(Integer value, int pos) {
        completedHours.set(pos, value);

        for (int i = pos; i < getLeftToDoHours().size(); i++) {
            if (i == 0) setLeftToDoHours(getTotalHours() - getCompletedHours().get(i), i);
            else setLeftToDoHours(getLeftToDoHours().get(i - 1) - getCompletedHours().get(i), i);
        }
    }

    public JsonObject getStaticValuesJSON() {
        JsonObject subjectJson = new JsonObject();
        subjectJson.add("name", new JsonPrimitive(getSubjectName()));
        subjectJson.add("professor", new JsonPrimitive(getProfessorInitials()));
        subjectJson.add("SEMESTERS_HOUR", new JsonPrimitive(getHoursQuoteValue()));
        subjectJson.add("week_load", new JsonPrimitive(getWeeklyHoursValue()));
        subjectJson.add("changes", new JsonPrimitive(getChangedHoursValue()));
        return subjectJson;
    }

    public JsonArray getVarValuesJSON() {
        JsonArray subjectJson = new JsonArray();
        for (int i = 0; i < getDates().size(); i++) {
            JsonObject json = new JsonObject();
            json.add("for_date", new JsonPrimitive(getDates().get(i).toString()));
            json.add("completed", new JsonPrimitive(getCompletedHours().get(i)));
            json.add("left", new JsonPrimitive(getLeftToDoHours().get(i)));
            subjectJson.add(json);
        }
        return subjectJson;
    }

}
