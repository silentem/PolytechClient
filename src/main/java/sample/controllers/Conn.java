package sample.controllers;

import com.google.gson.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Conn {

    public static final String MAIN_URL = "http://c8622abe.ngrok.io";
    public static final String GROUPS_SUFFIX = "/Groups/";
    public static final String SUBJECTS_SUFFIX = "/Subjects/";
    public static final String COMPLETE_DATE_SUFFIX = "/CompleteDate/";
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
    public static final String JSON_SUFFIX = "?format=json";

    public static void createGroup(int number) throws IOException {
        URL url = new URL(Conn.MAIN_URL + Conn.GROUPS_SUFFIX);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestMethod("POST");

        OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
        JsonObject json = new JsonObject();
        json.add("number", new JsonPrimitive(number));
        wr.write(json.toString());
        wr.flush();
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

    public static void sendPUT(String sUrl, JsonElement json) throws IOException {
        URL url = new URL(sUrl);
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("PUT");
        OutputStreamWriter out = new OutputStreamWriter(
                httpCon.getOutputStream());
        out.write(json.toString());
        out.close();
        httpCon.getInputStream();
    }

}
