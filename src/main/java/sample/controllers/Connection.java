package sample.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Connection {

    public static final String MAIN_URL = "http://1aed4d2a.ngrok.io";
    public static final String GROUPS_SUFFIX = "/groups/";
    public static final String SUBJECT_SAVE_SUFFIX = "/subjects/";
    public static final String SUBJECTS_UPLOAD_SUFFIX = "/subject/group/";
    public static final String CHANGES_SUFFIX = "/complete/get/id/";
    public static final String JSON_SUFFIX = "/?format=json";

    public static JsonArray getJson(String sUrl) throws IOException {
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

    public static void saveGroupToServer(JsonElement json) throws IOException {

        URL url = new URL(Connection.MAIN_URL + Connection.SUBJECT_SAVE_SUFFIX);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestMethod("POST");

        OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
        wr.write(json.toString());
        wr.flush();

    }


}
