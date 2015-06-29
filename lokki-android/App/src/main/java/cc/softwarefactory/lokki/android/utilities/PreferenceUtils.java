package cc.softwarefactory.lokki.android.utilities;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtils {

    public static final String KEY_AUTH_TOKEN = "authorizationToken";
    public static final String KEY_USER_ACCOUNT = "userAccount";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_CONTACTS = "contacts";
    public static final String KEY_SETTING_VISIBILITY = "settingVisibility";
    public static final String KEY_I_DONT_WANT_TO_SEE = "iDontWantToSee";
    public static final String KEY_DEVICE_ID = "deviceId";
    public static final String KEY_DASHBOARD = "dashboard";
    public static final String KEY_PLACES = "places";
    public static final String KEY_SETTING_MAP_MODE = "settingMapMode";
    private static final String TAG = "PreferenceUtils";

    /**
     * Get a string value from default shared preferences
     * @param context Application context
     * @param key Key to find value from preferences
     * @return Key value or empty string if key not found
     */
    public static String getString(Context context, String key) {

        if (context == null) {
            return null;
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(key, "");
    }

    /**
     * Get a boolean value from default shared preferences
     * @param context Application context
     * @param key Key to find value from preferences
     * @return Key value or false if key not found
     */
    public static Boolean getBoolean(Context context, String key) {

        if (context == null) {
            return null;
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(key, false);
    }





    public static void setString(Context context, String key, String value) {

        if (context == null) {
            return;
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(key, value).apply();
    }

    public static void setBoolean(Context context, String key, boolean value) {

        if (context == null) {
            return;
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(key, value).apply();
    }
}
