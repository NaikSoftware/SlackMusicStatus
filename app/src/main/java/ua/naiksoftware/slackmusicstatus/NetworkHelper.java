package ua.naiksoftware.slackmusicstatus;

import android.os.AsyncTask;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by naik on 24.06.17.
 */

public class NetworkHelper {

    public static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    public static <T> AsyncTask<String, Void, T> get(final Callback<T> callback, final Class<T> clazz) {
        return new AsyncTask<String, Void, T>() {
            @Override
            protected T doInBackground(String... params) {
                BufferedReader reader = null;
                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) new URL(params[0]).openConnection();
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    StringBuilder sb = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    return GSON.fromJson(sb.toString(), clazz);
                } catch (IOException e) {
                    return null;
                } finally {
                    if (reader != null) try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (connection != null) connection.disconnect();
                }
            }

            @Override
            protected void onPostExecute(T result) {
                if (callback != null) callback.call(result);
            }
        };
    }
}
