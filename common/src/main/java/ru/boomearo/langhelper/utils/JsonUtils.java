package ru.boomearo.langhelper.utils;

import lombok.experimental.UtilityClass;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@UtilityClass
public class JsonUtils {

    public static JSONObject connectNormal(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);

        connection.setRequestMethod("GET");
        connection.setRequestProperty("charset", "UTF-8");

        return connect(connection);
    }

    private static JSONObject connect(HttpURLConnection connection) throws IOException {
        try {
            connection.connect();

            if (connection.getResponseCode() != 200) {
                return null;
            }

            try (BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }

                connection.disconnect();

                Object jsonObject = JSONValue.parse(response.toString());

                if (!(jsonObject instanceof JSONObject)) {
                    return null;
                }

                return (JSONObject) jsonObject;
            }

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static JSONObject getJsonObject(Object obj) {
        if (obj == null) {
            return null;
        }
        if (!(obj instanceof JSONObject)) {
            return null;
        }
        return (JSONObject) obj;
    }


    public static String getStringObject(Object obj) {
        if (obj == null) {
            return null;
        }
        if (!(obj instanceof String)) {
            return null;
        }
        return (String) obj;
    }

    public static Long getLongObject(Object obj) {
        if (obj == null) {
            return null;
        }
        if (!(obj instanceof Number)) {
            return null;
        }
        return ((Number) obj).longValue();
    }

    public static Integer getIntegerObject(Object obj) {
        if (obj == null) {
            return null;
        }
        if (!(obj instanceof Number)) {
            return null;
        }
        return ((Number) obj).intValue();
    }

    public static JSONArray getJsonArrayObject(Object obj) {
        if (obj == null) {
            return null;
        }
        if (!(obj instanceof JSONArray)) {
            return null;
        }
        return (JSONArray) obj;
    }
}
