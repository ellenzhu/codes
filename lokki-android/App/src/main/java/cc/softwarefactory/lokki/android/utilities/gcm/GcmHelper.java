/*
Copyright (c) 2014-2015 F-Secure
See LICENSE for details
*/
package cc.softwarefactory.lokki.android.utilities.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;

import java.io.IOException;

import cc.softwarefactory.lokki.android.utilities.ServerApi;
import cc.softwarefactory.lokki.android.utilities.Utils;

public class GcmHelper {

    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    private static String SENDER_ID = "229584256615";
    private static final String TAG = "GcmHelper";
    private static GoogleCloudMessaging gcm;
    private static String regid;

    public static void start(Context context) {

        Log.e(TAG, "start");
        // Check device for Play Services APK. If check succeeds, proceed with GCM registration.
        if (Utils.checkGooglePlayServices(context)) {
            gcm = GoogleCloudMessaging.getInstance(context);
            regid = getRegistrationId(context);

            if (regid.isEmpty()) {
                registerInBackground(context);

            } else {
                sendRegistrationIdToBackend(context);
            }
        }
    }

    private static String getRegistrationId(Context context) {

        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.e(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID since the existing regID is not guaranteed to work with the new app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion == currentVersion) {
            return registrationId;
        }
        Log.i(TAG, "App version changed.");
        return "";
    }

    private static SharedPreferences getGCMPreferences(Context context) {

        return context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {

        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private static void registerInBackground(final Context context) {

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    sendRegistrationIdToBackend(context);
                    storeRegistrationId(context, regid);
                    return "Device registered, registration ID = " + regid;

                } catch (IOException ex) {
                    return "Error :" + ex.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String msg) {

                Log.e(TAG, msg);
            }

        }.execute(null, null, null);

    }

    private static void sendRegistrationIdToBackend(Context context) {

        Log.e(TAG, "sendRegistrationIdToBackend");
        try {
            ServerApi.sendGCMToken(context, regid);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private static void storeRegistrationId(Context context, String regId) {

        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.apply();
    }

}
