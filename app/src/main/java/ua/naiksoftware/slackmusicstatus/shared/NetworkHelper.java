package ua.naiksoftware.slackmusicstatus.shared;

import android.os.AsyncTask;
import android.util.Pair;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by naik on 24.06.17.
 */

public class NetworkHelper {

    public static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    public static <T> AsyncTask<String, Void, T> getAsync(final Callback<T> callback, final Class<T> clazz) {
        return new AsyncTask<String, Void, T>() {
            @Override
            protected T doInBackground(String... params) {
                return NetworkHelper.get(clazz, params[0]);
            }

            @Override
            protected void onPostExecute(T result) {
                if (callback != null) callback.call(result);
            }
        };
    }

    public static  <T> T get(Class<T> clazz, String url) {
        BufferedReader reader = null;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
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

    public static <T> T post(Class<T> clazz, String url, List<Pair<String, String>> params) {
        BufferedReader reader = null;
        HttpURLConnection connection = null;
        try {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < params.size(); i++) {
                if (i != 0) sb.append('&');
                Pair<String, String> param = params.get(i);
                sb.append(URLEncoder.encode(param.first, "UTF-8"));
                sb.append('=');
                sb.append(URLEncoder.encode(param.second, "UTF-8"));
            }
            byte[] data = sb.toString().getBytes("UTF-8");

            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            connection.setDoOutput(true);
            connection.getOutputStream().write(data);

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            if (clazz == String.class) return (T) sb.toString();
            else return GSON.fromJson(sb.toString(), clazz);
        } catch (IOException e) {
            e.printStackTrace();
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
}
