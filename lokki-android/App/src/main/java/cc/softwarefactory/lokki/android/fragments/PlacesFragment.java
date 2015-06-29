/*
Copyright (c) 2014-2015 F-Secure
See LICENSE for details
*/
package cc.softwarefactory.lokki.android.fragments;

import android.support.v7.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.androidquery.AQuery;

import cc.softwarefactory.lokki.android.utilities.ServerApi;
import cc.softwarefactory.lokki.android.services.DataService;
import cc.softwarefactory.lokki.android.MainApplication;
import cc.softwarefactory.lokki.android.R;
import cc.softwarefactory.lokki.android.utilities.PreferenceUtils;

import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;


public class PlacesFragment extends Fragment {

    private static final String TAG = "PlacesFragment";
    private Context context;
    private ArrayList<String> placesList;
    private JSONObject peopleInsidePlace;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity().getApplicationContext();
        View rootView = inflater.inflate(R.layout.fragment_places, container, false);
        listView = (ListView) rootView.findViewById(R.id.listView1);
        registerForContextMenu(listView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        Log.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        DataService.getPlaces(context);
        showPlaces();
    }

    @Override
    public void onResume() {

        Log.d(TAG, "onResume");
        super.onResume();
        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver, new IntentFilter("PLACES-UPDATE"));
        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver, new IntentFilter("LOCATION-UPDATE"));
    }

    @Override
    public void onPause() {

        super.onPause();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(TAG, "BroadcastReceiver onReceive");
            showPlaces();
        }
    };

    private void setListAdapter() {

        Log.d(TAG, "setListAdapter");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.places_row_layout, placesList) {

            @Override
            public View getView(int position, View unusedView, ViewGroup parent) {

                View convertView = getActivity().getLayoutInflater().inflate(R.layout.places_row_layout, parent, false);
                AQuery aq = new AQuery(getActivity(), convertView);

                final String placeName = getItem(position);
                aq.id(R.id.place_name).text(placeName);

                aq.id(R.id.places_context_menu_button).clicked(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.showContextMenu();
                    }
                });

                Log.d(TAG, "Plane name: " + placeName);
                Log.d(TAG, "peopleInsidePlace? " + peopleInsidePlace.has(placeName));

                if (peopleInsidePlace.has(placeName)) { // People are inside this place
                    Log.d(TAG, "Inside loop");
                    try {
                        JSONArray people = peopleInsidePlace.getJSONArray(placeName);
                        LinearLayout avatarRow = (LinearLayout) convertView.findViewById(R.id.avatar_row);
                        avatarRow.removeAllViewsInLayout(); // Deletes old avatars, if any.

                        for (int i = 0; i < people.length(); i++) {

                            final String email = people.getString(i);
                            if (MainApplication.iDontWantToSee.has(email)) {
                                continue;
                            }
                            RoundedImageView image = createAvatar(email);

                            if (MainApplication.avatarCache.get(email) != null) {
                                image.setImageBitmap(MainApplication.avatarCache.get(email));
                            } else {
                                Log.d(TAG, "Avatar not in cache, email: " + email);
                                image.setImageResource(R.drawable.default_avatar);
                            }
                            image.setContentDescription(email);

                            avatarRow.addView(image);
                        }

                    } catch (Exception ex) {
                        Log.d(TAG, "Error in adding avatars");
                    }
                }

                return convertView;
            }
        };

        listView.setAdapter(adapter);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.places_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
        int position = info.position;
        String placeName = placesList.get(position);

        switch(item.getItemId()) {
            case R.id.places_context_menu_delete:
                deletePlaceDialog(placeName);
                return true;
            case R.id.places_context_menu_rename:
                renamePlaceDialog(placeName);
        }

        return super.onContextItemSelected(item);
    }

    private void deletePlaceDialog(final String name) {

        Log.d(TAG, "deletePlaceDialog");
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(context.getResources().getString(R.string.delete_place))
                .setMessage(name + " " + context.getResources().getString(R.string.will_be_deleted_from_places))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletePlace(name);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.show();

    }

    private void deletePlace(String name) {

        Log.d(TAG, "deletePlace");
        try {
            Iterator<String> keys = MainApplication.places.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject placeObj = MainApplication.places.getJSONObject(key);
                if (name.equals(placeObj.getString("name"))) {
                    Log.d(TAG, "Place ID to be deleted: " + key);
                    ServerApi.removePlace(context, key);
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void renamePlaceDialog(final String placeName) {

        Log.d(TAG, "renamePlaceDialog");
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        final EditText input = new EditText(getActivity());
        String titleFormat = getResources().getString(R.string.rename_place);
        String title = String.format(titleFormat, placeName);

        dialog.setTitle(title)
                .setMessage(R.string.write_place_name)
                .setView(input)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = input.getText().toString();
                        renamePlace(placeName, newName);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    private void renamePlace(String oldName, String newName) {

        Log.d(TAG, "renamePlace");
        try {
            Iterator<String> keys = MainApplication.places.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject placeObj = MainApplication.places.getJSONObject(key);
                if (oldName.equals(placeObj.getString("name"))) {
                    Log.d(TAG, "Place ID to be renamed: " + key);
                    ServerApi.renamePlace(context, key, newName);
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private RoundedImageView createAvatar(final String email) {

        int sizeInDip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) 65, getResources().getDisplayMetrics());
        RoundedImageView image = new RoundedImageView(getActivity());
        image.setTag(email);
        image.setCornerRadius(100f);
        image.setBorderWidth(0f);
        image.setPadding(20, 0, 0, 0);
        image.setLayoutParams(new LinearLayout.LayoutParams(sizeInDip, sizeInDip));
        image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                MainApplication.emailBeingTracked = email;
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent("LOCATION-UPDATE"));
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent("GO-TO-MAP"));
            }
        });

        return image;
    }

    private void showPlaces() {

        Log.d(TAG, "showPlaces");
        placesList = new ArrayList<>();
        peopleInsidePlace = new JSONObject();

        try {
            if (MainApplication.places == null) { // Read them from cache
                if (PreferenceUtils.getString(context, PreferenceUtils.KEY_PLACES).isEmpty()) {
                    return;
                }
                MainApplication.places = new JSONObject(PreferenceUtils.getString(context, PreferenceUtils.KEY_PLACES));
            }

            Log.d(TAG, "Places json: " + MainApplication.places);
            Iterator<String> keys = MainApplication.places.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject placeObj = MainApplication.places.getJSONObject(key);
                String placeName = placeObj.getString("name");
                placesList.add(placeName);

                calculatePeopleInside(placeObj);
            }

            Log.d(TAG, "peopleInsidePlace: " + peopleInsidePlace);
            Collections.sort(placesList);
            setListAdapter();

        } catch (Exception ex) {
            Log.d(TAG, "ERROR: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void calculatePeopleInside(JSONObject placeObj) {

        try {
            if (MainApplication.dashboard == null) {
                return;
            }

            JSONObject iCanSeeObj = MainApplication.dashboard.getJSONObject("icansee");
            JSONObject idMappingObj = MainApplication.dashboard.getJSONObject("idmapping");
            JSONArray peopleInThisPlace = new JSONArray();

            Location placeLocation = new Location(placeObj.getString("name"));
            placeLocation.setLatitude(placeObj.getDouble("lat"));
            placeLocation.setLongitude(placeObj.getDouble("lon"));
            placeLocation.setAccuracy(placeObj.getInt("rad"));

            // Check myself
            JSONObject userLocationObj = MainApplication.dashboard.getJSONObject("location");
            Location myLocation = new Location(MainApplication.userAccount);
            myLocation.setLatitude(userLocationObj.getDouble("lat"));
            myLocation.setLongitude(userLocationObj.getDouble("lon"));
            //Log.d(TAG, "userLocation: " + userLocation);

            // Compare location
            float myDistance = placeLocation.distanceTo(myLocation);
            if (myDistance < placeLocation.getAccuracy()) {
                //Log.d(TAG, email + " is in place: " + placeLocation.getProvider());
                peopleInThisPlace.put(MainApplication.userAccount);
            }

            // Check for my contacts
            Iterator<String> keys = iCanSeeObj.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                String email = idMappingObj.getString(key);

                JSONObject locationObj = iCanSeeObj.getJSONObject(key).getJSONObject("location");
                Location userLocation = new Location(email);

                if (!locationObj.has("lat") || !locationObj.has("lon")) {
                    continue;
                }

                userLocation.setLatitude(locationObj.getDouble("lat"));
                userLocation.setLongitude(locationObj.getDouble("lon"));
                //Log.d(TAG, "userLocation: " + userLocation);

                // Compare location
                float distance = placeLocation.distanceTo(userLocation);
                if (distance < placeLocation.getAccuracy()) {
                    //Log.d(TAG, email + " is in place: " + placeLocation.getProvider());
                    peopleInThisPlace.put(email);
                }
            }

            if (peopleInThisPlace.length() > 0) {
                //Log.d(TAG, "peopleInThisPlace: " + peopleInThisPlace);
                peopleInsidePlace.put(placeObj.getString("name"), peopleInThisPlace);
            }


        } catch (Exception ex) {
            Log.d(TAG, "Error");
            ex.printStackTrace();

        }
    }

}
