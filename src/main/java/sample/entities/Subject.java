package sample.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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

    private Map<Date, Integer> completedHoursValue;
    private Map<Date, Integer> leftToDoHoursValue;

    public Subject(String subjectName, String professorInitials, int hoursQuoteValue) {
        this.setSubjectName(subjectName);
        this.setProfessorInitials(professorInitials);
        setChangedHoursValue(0);
        this.setHoursQuoteValue(hoursQuoteValue);
        setWeeklyHoursValue(0);
        completedHoursValue = new LinkedHashMap<>();
        leftToDoHoursValue = new LinkedHashMap<>();
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

    public Map<Date, Integer> getCompletedHoursValue() {
        return completedHoursValue;
    }

    public void addCompletedHours(Date date, Integer value){
        getCompletedHoursValue().put(date, value);
        if (getCompletedHoursValue().size() == 1) setLeftToDoHoursValue(date, getTotalHours() - value);
        else {
            Integer indexPrevValue = getLeftToDoHoursValue().values().size() - 1;
            Integer prevValue = ((Integer) getLeftToDoHoursValue().values().toArray()[indexPrevValue]);
            setLeftToDoHoursValue(date, prevValue - value);
        }
    }

    private void setLeftToDoHoursValue(Date date, Integer value){
            leftToDoHoursValue.put(date, value);
    }

    public Map<Date, Integer> getLeftToDoHoursValue(){
        return leftToDoHoursValue;
    }

    public JsonObject getStaticValuesJSON(){
        JsonObject subjectJson = new JsonObject();
        subjectJson.add("name", new JsonPrimitive(getSubjectName()));
        subjectJson.add("professor", new JsonPrimitive(getProfessorInitials()));
        subjectJson.add("SEMESTERS_HOUR", new JsonPrimitive(getHoursQuoteValue()));
        subjectJson.add("week_load", new JsonPrimitive(getWeeklyHoursValue()));
        subjectJson.add("changes", new JsonPrimitive(getChangedHoursValue()));
        return subjectJson;
    }

}
