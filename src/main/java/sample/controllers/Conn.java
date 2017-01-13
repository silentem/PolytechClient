package sample.controllers;

import com.google.gson.*;
import javafx.collections.ObservableList;
import sample.entities.Subject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Conn {

    public static JsonObject groupJson = new JsonObject();

    public static final String MAIN_URL = "http://abe39b42.ngrok.io";
    public static final String GROUPS_SUFFIX = "/Groups/";
    public static final String CREATE_GROUP_SUFFIX = "/create/group/";
    public static final String SUBJECTS_SUFFIX = "/Subjects/";
    public static final String DATE_SUFFIX = "/Date/";
    public static final String GROUP_UPDATE_SUFFIX = "/group/update/";
    public static final String GROUP_DESTROY_SUFFIX = "/group/destroy/";
    public static final String SUBJECT_UPDATE_SUFFIX = "/subject/update/";
    public static final String SUBJECT_DESTROY_SUFFIX = "/subject/destroy/";
    public static final String SUBJECT_GROUP_SUFFIX = "/subject/group/";
    public static final String SUBJECT_GROUP_NAME_SUFFIX = "/subject/group_name/";
    public static final String DATE_UPDATE_SUFFIX = "/date/update/";
    public static final String DATE_DESTROY_SUFFIX = "/date/destroy/";
    public static final String DATE_FOR_SUBJECT_SUFFIX = "/date/for_subject/";
    public static final String DATE_FOR_DATE_SUFFIX = "/date/for_date/";

    public static void createGroup(int number, ObservableList<Subject> subjects) throws IOException {
        JsonObject json = new JsonObject();
        json.add("number", new JsonPrimitive(number));
        JsonArray arr = new JsonArray();
        for (Subject s : subjects) {
            JsonObject obj = new JsonObject();
            obj.add("name", new JsonPrimitive(s.getSubjectName()));
            obj.add("professor", new JsonPrimitive(s.getProfessorInitials()));
            obj.add("SEMESTERS_HOUR", new JsonPrimitive(s.getHoursQuoteValue()));
            obj.add("week_load", new JsonPrimitive(s.getWeeklyHoursValue()));
            arr.add(obj);
        }
        json.add("subjects", arr);
        send(Conn.MAIN_URL + Conn.CREATE_GROUP_SUFFIX, json, "POST");
    }

    public static JsonElement getJson(String sUrl) throws IOException {
        HttpURLConnection request = null;
        try {
            URL url = new URL(sUrl);
            request = (HttpURLConnection) url.openConnection();
            request.connect();
        } finally {
            if (request != null) {
                request.disconnect();
            }
        }

        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));

        return root.getAsJsonArray();
    }

    public static void send(String sUrl, JsonElement json, String method) {
        try {
            URL url = new URL(sUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", "application/json");


            if (json != null) {
                String input = json.toString();
                System.out.println(json);
                OutputStream os = conn.getOutputStream();
                os.write(input.getBytes());
                os.flush();
            }
            conn.getInputStream();
            conn.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
