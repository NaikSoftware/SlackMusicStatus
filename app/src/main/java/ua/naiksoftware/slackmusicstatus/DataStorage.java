package ua.naiksoftware.slackmusicstatus;

import android.content.Context;
import android.content.SharedPreferences;

import ua.naiksoftware.slackmusicstatus.model.User;

/**
 * Created by naik on 23.06.17.
 */

public class DataStorage {

    private static final String KEY_USER = "KEY_USER";
    private static final String KEY_ENABLED = "KEY_ENABLED";

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
