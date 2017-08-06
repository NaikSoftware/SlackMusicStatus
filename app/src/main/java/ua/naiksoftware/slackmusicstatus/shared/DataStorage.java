package ua.naiksoftware.slackmusicstatus.shared;

import android.content.Context;
import android.content.SharedPreferences;

import ua.naiksoftware.slackmusicstatus.BuildConfig;
import ua.naiksoftware.slackmusicstatus.model.User;

/**
 * Created by naik on 23.06.17.
 */

public class DataStorage {

    private static final String KEY_USER = "KEY_USER";
    private static final String KEY_ENABLED = "KEY_ENABLED";
    private static final String KEY_LAST_STATUS = "KEY_LAST_STATUS";

    private static SharedPreferences sSharedPreferences;
    private static User sUser;

    public static User getUser(Context context) {
        if (sUser == null) {
            SharedPreferences prefs = getPrefs(context);
            String userStr = prefs.getString(KEY_USER, null);
            if (userStr == null) return null;
            sUser = NetworkHelper.GSON.fromJson(userStr, User.class);
        }
        return sUser;
    }

    public static void storeUser(Context context, User user) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(KEY_USER, NetworkHelper.GSON.toJson(user));
        editor.apply();
        sUser = null;
    }

    public static boolean getEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_ENABLED, true);
    }

    public static void setEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_ENABLED, enabled).apply();
    }

    public static String getDefaultStatusString() {
        return "";
    }

    public static String getDefaultStatusIcon() {
        return ":whale:";
    }

    public static String getLastStatus(Context context) {
        return getPrefs(context).getString(KEY_LAST_STATUS, null);
    }

    public static void updateStatus(Context context, String status) {
        getPrefs(context).edit().putString(KEY_LAST_STATUS, status).apply();
    }

    public static void clear(Context context) {
        getPrefs(context).edit().clear().apply();
    }

    private static SharedPreferences getPrefs(Context context) {
        if (sSharedPreferences == null) {
            sSharedPreferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        }
        return sSharedPreferences;
    }
}
