/*
Copyright (c) 2014-2015 F-Secure
See LICENSE for details
*/
package cc.softwarefactory.lokki.android.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import cc.softwarefactory.lokki.android.MainApplication;
import cc.softwarefactory.lokki.android.R;
import cc.softwarefactory.lokki.android.activities.FirstTimeActivity;
import cc.softwarefactory.lokki.android.utilities.DialogUtils;
import cc.softwarefactory.lokki.android.utilities.Utils;
import cc.softwarefactory.lokki.android.utilities.map.MapUserTypes;
import cc.softwarefactory.lokki.android.utilities.map.MapUtils;


public class MapViewFragment extends Fragment {

    private static final String TAG = "MapViewFragment";
    private SupportMapFragment fragment;
    private GoogleMap map;
    private HashMap<String, Marker> markerMap;
    private AQuery aq;
    private static Boolean cancelAsyncTasks = false;
    private Context context;
    private Boolean firstTimeZoom = true;
    private ArrayList<Circle> placesOverlay;
    private double radiusMultiplier = 0.9;  // Dont want to fill the screen from edge to edge...

    public MapViewFragment() {
        markerMap = new HashMap<>();
        placesOverlay = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_map, container, false);
        aq = new AQuery(getActivity(), rootView);
        context = getActivity().getApplicationContext();
        return rootView;

    }

    @Override
    public void onDestroyView() {
        // Trying to clean up properties (not to hold anything coming from the map (and avoid mem leaks).
        super.onDestroyView();
        fragment = null;
        map = null;
        aq = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) { // This method guarantees that the fragment is loaded in the parent activity!

        Log.e(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            //fragment = SupportMapFragment.newInstance(new GoogleMapOptions().useViewLifecycleInFragment(true)); // The map is destroyed when fragment is destroyed. Releasing memory
            fm.beginTransaction().replace(R.id.map, fragment).commit();
        }

        //setHasOptionsMenu(true);
    }


    @Override
    public void onResume() { // onResume is called after onActivityCreated, when the fragment is loaded 100%

        Log.e(TAG, "onResume");
        super.onResume();
        if (map == null) {
            Log.e(TAG, "Map null. creating it.");
            setUpMap();
            setupAddPlacesOverlay();
        } else {
            Log.e(TAG, "Map already exists. Nothing to do.");
        }

        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver, new IntentFilter("LOCATION-UPDATE"));
        LocalBroadcastManager.getInstance(context).registerReceiver(placesUpdateReceiver, new IntentFilter("PLACES-UPDATE"));

        checkLocationServiceStatus();

        new UpdateMap().execute(MapUserTypes.All);
        cancelAsyncTasks = false;
        if (MainApplication.places != null) {
            updatePlaces();
        }
    }

    private void checkLocationServiceStatus() {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!gps && !network && !MainApplication.locationDisabledPromptShown) {
            promptLocationService();
            MainApplication.locationDisabledPromptShown = true;
        }
    }

    private void promptLocationService() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.location_services_disabled)
                .setMessage(R.string.gps_disabled)
                .setCancelable(true)
                .setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(R.string.ignore, null)
                .show();
    }

    private void setUpMap() {

        map = fragment.getMap();

        if (map == null) {
            return;
        }

        removeMarkers();

        map.setMapType(MainApplication.mapTypes[MainApplication.mapType]);
        map.setInfoWindowAdapter(new MyInfoWindowAdapter()); // Set the windowInfo view for each marker
        map.setMyLocationEnabled(true);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(false);

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (!marker.isInfoWindowShown()) {
                    marker.showInfoWindow();
                    MainApplication.emailBeingTracked = marker.getTitle();

                }
                return true;
            }
        });

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MainApplication.emailBeingTracked = null;
            }
        });

        // Set long click to add a place
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                setAddPlacesVisible(true);
            }
        });

        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                MainApplication.locationDisabledPromptShown = false;
                checkLocationServiceStatus();
                return false;
            }
        });
    }

    private void setupAddPlacesOverlay() {
        // todo these should probably be initialized once...
        Button cancelButton = (Button) getView().findViewById(R.id.cancel_add_place_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAddPlacesVisible(false);
            }
        });

        Button addPlaceButton = (Button) getView().findViewById(R.id.add_place_button);
        addPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mapWidth = fragment.getView().getWidth();
                int mapHeight = fragment.getView().getHeight() - getView().findViewById(R.id.add_place_buttons).getHeight();

                Location middleSideLocation;
                if (mapWidth > mapHeight) {
                    middleSideLocation = MapUtils.convertToLocation(map.getProjection().fromScreenLocation(new Point(mapWidth / 2, 0)), "middleSide");
                } else {
                    middleSideLocation = MapUtils.convertToLocation(map.getProjection().fromScreenLocation(new Point(0, mapHeight / 2)), "middleSide");
                }

                LatLng centerLatLng = map.getProjection().fromScreenLocation(getAddPlaceCircleCenter());
                int radius = (int) middleSideLocation.distanceTo(MapUtils.convertToLocation(centerLatLng, "center"));
                DialogUtils.addPlace(getActivity(), centerLatLng, (int) (radius * radiusMultiplier));
            }
        });
    }

    public void setAddPlacesVisible(boolean visible) {
        if (visible) {
            ((ImageView) getView().findViewById(R.id.addPlaceCircle)).setImageDrawable(new AddPlaceCircleDrawable());
            showAddPlaceButtons();
        } else {
            ((ImageView) getView().findViewById(R.id.addPlaceCircle)).setImageDrawable(null);
            hideAddPlaceButtons();
        }
    }

    private void showAddPlaceButtons() {
        Animation slideUp = AnimationUtils.loadAnimation(this.getActivity().getApplicationContext(), R.anim.add_place_buttons_show);
        getView().findViewById(R.id.add_place_buttons).startAnimation(slideUp);
        getView().findViewById(R.id.add_place_overlay).setVisibility(View.VISIBLE);
    }

    private void hideAddPlaceButtons() {
        Animation slideDown = AnimationUtils.loadAnimation(this.getActivity().getApplicationContext(), R.anim.add_place_buttons_hide);
        slideDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getView().findViewById(R.id.add_place_overlay).setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        getView().findViewById(R.id.add_place_buttons).startAnimation(slideDown);
    }

    private Point getAddPlaceCircleCenter() {
        int mapCenterX = fragment.getView().getWidth() / 2;
        int mapCenterY = (fragment.getView().getHeight() - getView().findViewById(R.id.add_place_buttons).getHeight()) / 2;

        return new Point(mapCenterX, mapCenterY);
    }

    private void removeMarkers() {

        Log.e(TAG, "removeMarkers");
        for (Marker m : markerMap.values()) {
            m.remove();
        }
        markerMap.clear();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
        LocalBroadcastManager.getInstance(context).unregisterReceiver(placesUpdateReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "BroadcastReceiver onReceive");
            Bundle extras = intent.getExtras();
            if (extras == null || !extras.containsKey("current-location")) {
                new UpdateMap().execute(MapUserTypes.All);
            }
        }
    };

    private BroadcastReceiver placesUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "placesUpdateReceiver onReceive");
            updatePlaces();
        }
    };

    private void updatePlaces() {

        Log.e(TAG, "updatePlaces");
        if (map == null) {
            return;
        }

        removePlaces();

        try {
            Iterator<String> keys = MainApplication.places.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject placeObj = MainApplication.places.getJSONObject(key);
                Circle circle = map.addCircle(new CircleOptions()
                        .center(new LatLng(placeObj.getDouble("lat"), placeObj.getDouble("lon")))
                        .radius(placeObj.getInt("rad"))
                        .strokeWidth(0)
                        .fillColor(getResources().getColor(R.color.place_circle)));
                placesOverlay.add(circle);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void removePlaces() {

        Log.e(TAG, "removePlaces");
        for (Circle circle : placesOverlay) {
            circle.remove();
        }
        placesOverlay.clear();
    }

    class UpdateMap extends AsyncTask<MapUserTypes, Void, HashMap<String, Location>> {

        @Override
        protected HashMap<String, Location> doInBackground(MapUserTypes... params) {

            if (MainApplication.dashboard == null) {
                return null;
            }

            MapUserTypes who = params[0];
            Log.e(TAG, "UpdateMap update for all users: " + who);

            try {
                JSONObject iCanSee = MainApplication.dashboard.getJSONObject("icansee");
                JSONObject idMapping = MainApplication.dashboard.getJSONObject("idmapping");
                HashMap<String, Location> markerData = new HashMap<>();

                if (who == MapUserTypes.User || who == MapUserTypes.All) {
                    markerData.put(MainApplication.userAccount, convertToLocation(MainApplication.dashboard.getJSONObject("location"))); // User himself
                }

                if (who == MapUserTypes.Others || who == MapUserTypes.All) {
                    Iterator keys = iCanSee.keys();
                    while (keys.hasNext()) {
                        String key = (String) keys.next();
                        JSONObject data = iCanSee.getJSONObject(key);
                        JSONObject location = data.getJSONObject("location");
                        String email = (String) idMapping.get(key);
                        Log.e(TAG, "I can see: " + email + " => " + data);

                        if (MainApplication.iDontWantToSee != null && MainApplication.iDontWantToSee.has(email)) {
                            Log.e(TAG, "I dont want to see: " + email);
                        } else {
                            Location loc = convertToLocation(location);
                            if (loc == null) {
                                Log.e(TAG, "No location could be parsed for: " + email);
                            }
                            markerData.put(email, loc);
                        }
                    }
                }
                return markerData;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(HashMap<String, Location> markerDataResult) {

            Log.e(TAG, "cancelAsyncTasks: " + cancelAsyncTasks);
            super.onPostExecute(markerDataResult);
            if (markerDataResult != null && !cancelAsyncTasks && isAdded()) {
                for (String email : markerDataResult.keySet()) {
                    Log.e(TAG, "marker to update: " + email);
                    if (markerDataResult.get(email) != null) {
                        new LoadMarkerAsync(markerDataResult.get(email), email).execute();
                    }
                }
            }
        }
    }

    private Location convertToLocation(JSONObject locationObj) {

        Location myLocation = new Location("fused");
        try {
            if (locationObj.length() == 0) {
                return null;
            }
            double lat = locationObj.getDouble("lat");
            double lon = locationObj.getDouble("lon");
            float acc = (float) locationObj.getDouble("acc");
            Long time = locationObj.getLong("time");
            myLocation.setLatitude(lat);
            myLocation.setLongitude(lon);
            myLocation.setAccuracy(acc);
            myLocation.setTime(time);
            return myLocation;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {

            if (!aq.isExist() || cancelAsyncTasks || !isAdded()) {
                return null;
            }

            View myContentsView = getActivity().getLayoutInflater().inflate(R.layout.map_info_window, null);
            AQuery aq = new AQuery(myContentsView);

            String name = Utils.getNameFromEmail(context, marker.getTitle());
            aq.id(R.id.contact_name).text(name);
            aq.id(R.id.timestamp).text(Utils.timestampText(marker.getSnippet()));

            return myContentsView;
        }
    }

    public Bitmap getMarkerBitmap(String email, Boolean accurate, Boolean recent) {

        Log.e(TAG, "getMarkerBitmap");

        // Add cache checking logic
        Bitmap markerImage = MainApplication.avatarCache.get(email + ":" + accurate + ":" + recent);
        if (markerImage != null) {
            Log.e(TAG, "Marker IN cache: " + email + ":" + accurate + ":" + recent);
            return markerImage;
        } else {
            Log.e(TAG, "Marker NOT in cache. Processing: " + email + ":" + accurate + ":" + recent);
        }

        Log.e(TAG, "AvatarLoader not in cache. Fetching it. Email: " + email);
        // Get avatars
        Bitmap userImage = Utils.getPhotoFromEmail(context, email);
        if (userImage == null) {
            userImage = BitmapFactory.decodeResource(getResources(), R.drawable.default_avatar);
        } else {
            userImage = Utils.getRoundedCornerBitmap(userImage, 50);
        }

        // Marker colors, etc.
        Log.e(TAG, "userImage size: " + userImage);
        View markerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_marker, null);

        aq = new AQuery(markerView);
        aq.id(R.id.user_image).image(userImage);
        Log.e(TAG, "aq in place");

        if (email.equals(MainApplication.userAccount)) {
            aq.id(R.id.marker_frame).image(R.drawable.pointers_android_pointer_green);
        } else if (!recent || !accurate) {
            aq.id(R.id.marker_frame).image(R.drawable.pointers_android_pointer_orange);
        }

        Log.e(TAG, "Image set. Calling createDrawableFromView");

        markerImage = createDrawableFromView(markerView);
        MainApplication.avatarCache.put(email + ":" + accurate + ":" + recent, markerImage);
        return markerImage;
    }

    // Convert a view to bitmap
    public Bitmap createDrawableFromView(View view) {

        Log.e(TAG, "createDrawableFromView");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }


    class LoadMarkerAsync extends AsyncTask<Void, Void, Bitmap> {

        Location position;
        LatLng latLng;
        String email;
        String time;
        Boolean accurate;
        Boolean recent;

        public LoadMarkerAsync(Location position, String email) {

            this.email = email;
            this.position = position;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {

            if (position == null || email == null) {
                return null;
            }
            Log.e(TAG, "LoadMarkerAsync - Email: " + email + ", Position: " + position);
            latLng = new LatLng(position.getLatitude(), position.getLongitude());
            time = String.valueOf(position.getTime());
            accurate = Math.round(position.getAccuracy()) < 100;
            recent = (System.currentTimeMillis() - position.getTime()) < 60 * 60 * 1000;

            try {
                return getMarkerBitmap(email, accurate, recent);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmapResult) {

            super.onPostExecute(bitmapResult);
            if (bitmapResult == null || cancelAsyncTasks || !isAdded() || map == null) {
                return;
            }
            Marker marker = markerMap.get(email);
            Boolean isNew = false;
            if (marker != null) {
                Log.e(TAG, "onPostExecute - updating marker: " + email);
                marker.setPosition(latLng);
                marker.setSnippet(time);
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmapResult));

            } else {
                Log.e(TAG, "onPostExecute - creating marker: " + email);
                marker = map.addMarker(new MarkerOptions().position(latLng).title(email).snippet(time).icon(BitmapDescriptorFactory.fromBitmap(bitmapResult)));
                Log.e(TAG, "onPostExecute - marker created");
                markerMap.put(email, marker);
                Log.e(TAG, "onPostExecute - marker in map stored. markerMap: " + markerMap.size());
                isNew = true;
            }

            if (marker.getTitle().equals(MainApplication.emailBeingTracked)) {
                marker.showInfoWindow();
                Log.e(TAG, "onPostExecute - showInfoWindow open");
                if (isNew) {
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 16));
                } else {
                    map.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                }
            } else if (firstTimeZoom && MainApplication.emailBeingTracked == null && MainApplication.userAccount != null && marker.getTitle().equals(MainApplication.userAccount)) {
                firstTimeZoom = false;
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 16));
            }
        }

    }

    @Override
    public void onDestroy() {

        // TODO: Cancel ALL Async tasks
        cancelAsyncTasks = true;
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.e(TAG, "---------------------------------------------------------");
        Log.e(TAG, "onLowMemory");
        Log.e(TAG, "---------------------------------------------------------");
        startActivityForResult(new Intent(context, FirstTimeActivity.class), -1);
    }

    private class AddPlaceCircleDrawable extends Drawable {

        public static final int STROKE_WIDTH = 12;
        public final float[] DASH_INTERVALS = new float[]{49, 36};

        @Override
        public void draw(Canvas canvas) {
            Point mapCenter = getAddPlaceCircleCenter();
            int radius = Math.min(mapCenter.x, mapCenter.y);

            Paint circlePaint = new Paint();
            circlePaint.setColor(getResources().getColor(R.color.add_place_circle));
            circlePaint.setAntiAlias(true);
            circlePaint.setStrokeWidth(STROKE_WIDTH);
            DashPathEffect dashPath = new DashPathEffect(DASH_INTERVALS, 1.0f);
            circlePaint.setPathEffect(dashPath);
            circlePaint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(mapCenter.x, mapCenter.y, (int) (radius * radiusMultiplier - STROKE_WIDTH), circlePaint);
        }

        @Override
        public void setAlpha(int alpha) {

        }

        @Override
        public void setColorFilter(ColorFilter cf) {

        }

        @Override
        public int getOpacity() {
            return 0;
        }
    }
}
