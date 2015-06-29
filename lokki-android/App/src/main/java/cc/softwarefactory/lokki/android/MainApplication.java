/*
Copyright (c) 2014-2015 F-Secure
See LICENSE for details
*/
package cc.softwarefactory.lokki.android;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.util.LruCache;
import android.util.Log;


import cc.softwarefactory.lokki.android.services.DataService;
import cc.softwarefactory.lokki.android.services.LocationService;
import cc.softwarefactory.lokki.android.utilities.PreferenceUtils;
import cc.softwarefactory.lokki.android.utilities.ServerApi;
import cc.softwarefactory.lokki.android.utilities.Utils;
import com.google.android.gms.maps.GoogleMap;

import org.json.JSONException;
import org.json.JSONObject;

public class MainApplication extends Application {

    private static final boolean DEVELOPER_MODE = true;

    private static final String TAG = "MainApplication";


    public static int[] mapTypes = {GoogleMap.MAP_TYPE_NORMAL, GoogleMap.MAP_TYPE_SATELLITE, GoogleMap.MAP_TYPE_HYBRID};
    public static int mapType = 0;
    public static Boolean showPlaces = false;
    public static String emailBeingTracked;
    public static JSONObject dashboard = null;
    public static String userId; // Id for REST requests
    public static String userAccount; // Email
    public static JSONObject contacts;
    public static JSONObject mapping;
    public static JSONObject iDontWantToSee;
    public static Boolean visible = true;
    public static LruCache<String, Bitmap> avatarCache;
    public static JSONObject places;
    public static boolean locationDisabledPromptShown;


    @Override
    public void onCreate() {

        Log.e(TAG, "Lokki started component");

        loadSetting();

        locationDisabledPromptShown = false;

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024); // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;
        avatarCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than number of items.
                return (bitmap.getRowBytes() * bitmap.getHeight()) / 1024;
            }
        };

        String iDontWantToSeeString = PreferenceUtils.getString(this, PreferenceUtils.KEY_I_DONT_WANT_TO_SEE);
        if (!iDontWantToSeeString.isEmpty()) {
            try {
                MainApplication.iDontWantToSee = new JSONObject(iDontWantToSeeString);
            } catch (JSONException e) {
                MainApplication.iDontWantToSee = null;
                Log.e(TAG, e.getMessage());
            }
        } else {
            MainApplication.iDontWantToSee = new JSONObject();
        }
        Log.e(TAG, "MainApplication.iDontWantToSee: " + MainApplication.iDontWantToSee);

        if (DEVELOPER_MODE) {

            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());

            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                            //.detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }

        super.onCreate();
    }

    private void loadSetting() {
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        visible = PreferenceUtils.getBoolean(getApplicationContext(), PreferenceUtils.KEY_SETTING_VISIBILITY);
        Log.e(TAG, "Visible: " + visible);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.e(TAG, "---------------------------------------------------------");
        Log.e(TAG, "onLowMemory");
        Log.e(TAG, "---------------------------------------------------------");
    }

    class ErrorHandler implements Thread.UncaughtExceptionHandler {

        public ErrorHandler() {

            Log.e(TAG, "ErrorHandler created");
        }

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {

            Log.e(TAG, "---------------------------------------------------------");
            Log.e(TAG, "uncaughtException: " + ex.getMessage());
            Log.e(TAG, "---------------------------------------------------------");

            LocationService.stop(MainApplication.this);
            DataService.stop(MainApplication.this);

            try {
                String osType = "Android " + Build.VERSION.SDK_INT;
                String appVersion = Utils.getAppVersion(MainApplication.this);
                String reportData = ex.getMessage();
                String reportTitle = ex.getCause().getMessage();
                Log.e(TAG, "CRASH - OS: " + osType + ", appVersion: " + appVersion + ", Title: " + reportTitle + ", Data: " + reportData);

                ServerApi.reportCrash(MainApplication.this, osType, appVersion, reportTitle, reportData);
                Log.e(TAG, "Data sent to server");

            } catch (Exception exception) {
                Log.e(TAG, "Exception during the error reporting: " + exception.getMessage());
                exception.printStackTrace();
            }

            LocationService.stop(MainApplication.this);
            DataService.stop(MainApplication.this);
        }
    }
}
