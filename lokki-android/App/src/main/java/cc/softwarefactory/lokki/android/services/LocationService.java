/*
Copyright (c) 2014-2015 F-Secure
See LICENSE for details
*/
package cc.softwarefactory.lokki.android.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import cc.softwarefactory.lokki.android.MainApplication;
import cc.softwarefactory.lokki.android.R;
import cc.softwarefactory.lokki.android.utilities.ServerApi;
import cc.softwarefactory.lokki.android.activities.MainActivity;
import cc.softwarefactory.lokki.android.utilities.PreferenceUtils;
import cc.softwarefactory.lokki.android.utilities.map.MapUtils;
import cc.softwarefactory.lokki.android.utilities.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;

public class LocationService extends Service implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {

    // INTERVALS
    private static final long INTERVAL_10_MS = 10;
    private static final long INTERVAL_30_SECS = 30 * 1000;
    private static final long INTERVAL_1_MIN = 60 * 1000;

    // - SERVICE
    private static final int NOTIFICATION_SERVICE = 100;

    // OTHER
    private static final String TAG = "LocationService";
    private static final String RUN_1_MIN = "RUN_1_MIN";
    private static final String ALARM_TIMER = "ALARM_TIMER";

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest locationRequest;
    private static Boolean serviceRunning = false;
    private static Location lastLocation = null;

    public static void start(Context context) {

        Log.e(TAG, "start Service called");

        if (serviceRunning || !MainApplication.visible) { // If service is running, no need to start it again.
            Log.e(TAG, "Service already running...");
            return;
        }
        context.startService(new Intent(context, LocationService.class));
    }

    public static void stop(Context context) {

        Log.e(TAG, "stop Service called");
        context.stopService(new Intent(context, LocationService.class));
    }


    public static void run1min(Context context) {

        if (serviceRunning || !MainApplication.visible) {
            return; // If service is running, stop
        }
        Log.e(TAG, "run1min called");
        Intent intent = new Intent(context, LocationService.class);
        intent.putExtra(RUN_1_MIN, 1);
        context.startService(intent);
    }

    @Override
    public void onCreate() {

        Log.e(TAG, "onCreate");
        super.onCreate();

        if (PreferenceUtils.getString(this, PreferenceUtils.KEY_AUTH_TOKEN).isEmpty()) {
            Log.e(TAG, "User disabled reporting in App. Service not started.");
            stopSelf();
        } else if (Utils.checkGooglePlayServices(this)) {
            Log.e(TAG, "Starting Service..");
            setLocationClient();
            setNotificationAndForeground();
            serviceRunning = true;
        } else {
            Log.e(TAG, "Google Play Services Are NOT installed.");
            stopSelf();
        }
    }

    private void setTemporalTimer() {
        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, LocationService.class);
        alarmIntent.putExtra(ALARM_TIMER, 1);
        PendingIntent alarmCallback = PendingIntent.getService(this, 0, alarmIntent, 0);
        alarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + INTERVAL_1_MIN, alarmCallback);
        Log.e(TAG, "Time created.");
    }

    private void setLocationClient() {

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(INTERVAL_10_MS);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        Log.e(TAG, "Location Client created.");
    }

    private void setNotificationAndForeground() {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle("Lokki");
        notificationBuilder.setContentText("Running...");
        notificationBuilder.setSmallIcon(R.drawable.ic_stat_notify);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        notificationBuilder.setContentIntent(contentIntent);
        startForeground(NOTIFICATION_SERVICE, notificationBuilder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e(TAG, "onStartCommand invoked");

        if (intent == null) {
            return START_STICKY;
        }

        Bundle extras = intent.getExtras();

        if (extras == null) {
            return START_STICKY;
        }

        if (extras.containsKey(RUN_1_MIN)) {
            Log.e(TAG, "onStartCommand RUN_1_MIN");
            setTemporalTimer();
        } else if (extras.containsKey(ALARM_TIMER)) {
            Log.e(TAG, "onStartCommand ALARM_TIMER");
            stopSelf();
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.e(TAG, "locationClient connected");
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            updateLokkiLocation(mLastLocation);
        } else {
            Log.e(TAG, "Location is null?! Check location service?!");    // todo add prompt for checking that location services are enabled maybe?
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e(TAG, String.format("onLocationChanged - Location: %s", location));
        if (serviceRunning && mGoogleApiClient.isConnected() && location != null) {
            updateLokkiLocation(location);
        } else {
            this.stopSelf();
            onDestroy();
        }
    }

    private void updateLokkiLocation(Location location) {

        if (!MapUtils.useNewLocation(location, lastLocation, INTERVAL_30_SECS)) {
            Log.e(TAG, "New location discarded.");
            return;
        }

        Log.e(TAG, "New location taken into use.");
        lastLocation = location;
        DataService.updateDashboard(location);
        Intent intent = new Intent("LOCATION-UPDATE");
        intent.putExtra("current-location", 1);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        if (MainApplication.visible) {
            try {
                ServerApi.sendLocation(this, location);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "locationClient onConnectionFailed");
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy called");
        stopForeground(true);
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
            Log.e(TAG, "Location Updates removed.");

        } else {
            Log.e(TAG, "locationClient didn't exist.");
        }
        serviceRunning = false;
        super.onDestroy();
    }
}
