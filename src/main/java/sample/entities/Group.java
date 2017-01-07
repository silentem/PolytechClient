package sample.entities;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import sample.exceptions.NoSuchSubjectException;

import java.util.ArrayList;

public class Group {
    private ArrayList<Subject> subjects;
    private int groupNumber;

    public Group(int groupNumber) {
        this.groupNumber = groupNumber;
        subjects = new ArrayList<>();
    }

    public void addSubject(Subject subject) {
        subjects.add(subject);
    }

    public Subject getSubject(String name) {
        for (Subject s : subjects) {
            if (s.getSubjectName().equals(name)) return s;
        }
        throw new NoSuchSubjectException("No such subject!");
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public ArrayList<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(ArrayList<Subject> subjects) {
        this.subjects = subjects;
    }

    public JsonArray getStaticValuesJSON() {
        JsonArray group = new JsonArray();
        JsonObject number = new JsonObject();
        number.add("group", new JsonPrimitive(getGroupNumber()));
        group.add(number);
        for (Subject subject : getSubjects())
            group.add(subject.getStaticValuesJSON());
        return group;
    }

    public JsonArray getJson() {
        JsonArray group = new JsonArray();
        JsonObject number = new JsonObject();
        number.add("group", new JsonPrimitive(getGroupNumber()));
        group.add(number);
        for (Subject subject : getSubjects())
            group.add(subject.getVarValuesJSON());
        return group;
    }
}
