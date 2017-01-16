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

    private Integer id;
    private SimpleStringProperty subjectName;
    private SimpleStringProperty professorInitials;
    private SimpleIntegerProperty changedHoursValue;
    private SimpleIntegerProperty hoursQuoteValue;
    private SimpleIntegerProperty weeklyHoursValue;
    private List<ChData> chDatas;
    private boolean isStaticValChanged;
    private boolean isVarValChanged;

    public Subject(String subjectName, String professorInitials, int hoursQuoteValue) {
        this.setSubjectName(subjectName);
        this.setProfessorInitials(professorInitials);
        setChangedHoursValue(0);
        this.setHoursQuoteValue(hoursQuoteValue);
        setWeeklyHoursValue(0);
        chDatas = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public boolean isStaticValChanged() {
        return isStaticValChanged;
    }

    public void setStaticValChanged(boolean staticValChanged) {
        isStaticValChanged = staticValChanged;
    }

    public boolean isVarValChanged() {
        return isVarValChanged;
    }

    public void setVarValChanged(boolean varValChanged) {
        isVarValChanged = varValChanged;
    }

    public List<ChData> getChData() {
        return chDatas;
    }

    public void addCompletedHours(Date date, Integer value, Integer leftValue) {
        getChData().add(new ChData(date, value, leftValue));
    }
    public void addCompletedHours(Date date, Integer value, Integer leftValue, Integer id) {
        getChData().add(new ChData(date, value, leftValue));
        getChData().get(getChData().size() - 1).setId(id);
    }

    public void setCompletedHours(Integer value, int pos) {
        Integer oldValue = getChData().get(pos).getCompleted();
        Integer diff = oldValue - value;
        getChData().get(pos).setCompleted(value);
        for (int i = pos; i < getChData().size(); i++) {
            getChData().get(i).setLeftTo(getChData().get(i).getLeftTo() + diff);
        }

    }


    public void removeCompletedHours() {
        getChData().remove(getChData().size() - 1);
    }
}
