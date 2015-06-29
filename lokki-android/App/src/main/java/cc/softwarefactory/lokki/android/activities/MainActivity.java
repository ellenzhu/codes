/*
Copyright (c) 2014-2015 F-Secure
See LICENSE for details
*/
package cc.softwarefactory.lokki.android.activities;

import android.support.v7.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;

import cc.softwarefactory.lokki.android.MainApplication;
import cc.softwarefactory.lokki.android.R;
import cc.softwarefactory.lokki.android.datasources.contacts.ContactDataSource;
import cc.softwarefactory.lokki.android.datasources.contacts.DefaultContactDataSource;
import cc.softwarefactory.lokki.android.fragments.AboutFragment;
import cc.softwarefactory.lokki.android.fragments.AddContactsFragment;
import cc.softwarefactory.lokki.android.fragments.ContactsFragment;
import cc.softwarefactory.lokki.android.fragments.MapViewFragment;
import cc.softwarefactory.lokki.android.fragments.NavigationDrawerFragment;
import cc.softwarefactory.lokki.android.fragments.PlacesFragment;
import cc.softwarefactory.lokki.android.fragments.PreferencesFragment;
import cc.softwarefactory.lokki.android.services.DataService;
import cc.softwarefactory.lokki.android.services.LocationService;
import cc.softwarefactory.lokki.android.utilities.DialogUtils;
import cc.softwarefactory.lokki.android.utilities.PreferenceUtils;
import cc.softwarefactory.lokki.android.utilities.ServerApi;
import cc.softwarefactory.lokki.android.utilities.Utils;
import cc.softwarefactory.lokki.android.utilities.gcm.GcmHelper;



public class MainActivity extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final String TAG = "MainActivity";

    private static final int REQUEST_CODE_EMAIL = 1001;
    private static final int REQUEST_TERMS = 1002;

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    private int selectedOption = 0;

    private ContactDataSource mContactDataSource;

    // TODO: make non static, put in shared prefs
    public static Boolean firstTimeLaunch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.e(TAG, "onCreate");
        mContactDataSource = new DefaultContactDataSource();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = getTitle();

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout)); // Set up the drawer.

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(mTitle);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    protected void onStart() {

        super.onStart();
        Log.e(TAG, "onStart");

        if (firstTimeLaunch == null) {
            firstTimeLaunch = firstTimeLaunch();
        }

        if (firstTimeLaunch) {
            Log.e(TAG, "onStart - firstTimeLaunch, so showing terms.");
            startActivityForResult(new Intent(this, FirstTimeActivity.class), REQUEST_TERMS);
        } else {
            checkIfUserIsLoggedIn(); // Log user In
        }
    }

    private boolean firstTimeLaunch() {

        return PreferenceUtils.getString(this, PreferenceUtils.KEY_AUTH_TOKEN).isEmpty();
    }

    @Override
    protected void onResume() {

        super.onResume();
        Log.e(TAG, "onResume");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // WAKE_LOCK

        if (firstTimeLaunch || firstTimeLaunch()) {
            Log.e(TAG, "onResume - firstTimeLaunch, so avoiding launching services.");
            return;
        }


        Log.e(TAG, "onResume - NOT firstTimeLaunch, so launching services.");
        startServices();
        LocalBroadcastManager.getInstance(this).registerReceiver(exitMessageReceiver, new IntentFilter("EXIT"));
        LocalBroadcastManager.getInstance(this).registerReceiver(switchToMapReceiver, new IntentFilter("GO-TO-MAP"));


        Log.e(TAG, "onResume - check if dashboard is null");
        if (MainApplication.dashboard == null) {
            Log.e(TAG, "onResume - dashboard was null, get dashboard from server");
            ServerApi.getDashboard(getApplicationContext());
        }
    }


    private void startServices() {

        if (MainApplication.visible) {
            LocationService.start(this.getApplicationContext());
        }

        DataService.start(this.getApplicationContext());

        try {
            ServerApi.requestUpdates(this.getApplicationContext());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocationService.stop(this.getApplicationContext());
        DataService.stop(this.getApplicationContext());
        LocalBroadcastManager.getInstance(this).unregisterReceiver(switchToMapReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(exitMessageReceiver);
    }

    private void checkIfUserIsLoggedIn() {

        String userAccount = PreferenceUtils.getString(this, PreferenceUtils.KEY_USER_ACCOUNT);
        String userId = PreferenceUtils.getString(this, PreferenceUtils.KEY_USER_ID);
        String authorizationToken = PreferenceUtils.getString(this, PreferenceUtils.KEY_AUTH_TOKEN);
        boolean debug = false;

        if (debug || userId.isEmpty() || userAccount.isEmpty() || authorizationToken.isEmpty()) {
            try {
                startActivityForResult(new Intent(this, SignUpActivity.class), REQUEST_CODE_EMAIL);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, getResources().getString(R.string.general_error), Toast.LENGTH_LONG).show();
                finish();
            }
        } else { // User already logged-in
            MainApplication.userAccount = userAccount;
            MainApplication.userId = userId;
            GcmHelper.start(getApplicationContext()); // Register to GCM

            Log.e(TAG, "User email: " + userAccount);
            Log.e(TAG, "User id: " + userId);
            Log.e(TAG, "authorizationToken: " + authorizationToken);
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        String[] menuOptions = getResources().getStringArray(R.array.menuOptions);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mTitle = menuOptions[position];
        selectedOption = position;

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mTitle);
        }
        switch (position) {

            case 0: // Map
                fragmentManager.beginTransaction().replace(R.id.container, new MapViewFragment()).commit();
                break;

            case 1: // Places
                fragmentManager.beginTransaction().replace(R.id.container, new PlacesFragment()).commit();
                break;

            case 2: // People
                fragmentManager.beginTransaction().replace(R.id.container, new ContactsFragment()).commit();
                break;

            case 3: // Settings
                fragmentManager.beginTransaction().replace(R.id.container, new PreferencesFragment()).commit();
                break;

            case 4: // About
                fragmentManager.beginTransaction().replace(R.id.container, new AboutFragment()).commit();
                break;

            default:
                fragmentManager.beginTransaction().replace(R.id.container, new MapViewFragment()).commit();
                break;
        }
        supportInvalidateOptionsMenu();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.clear();
        if (mNavigationDrawerFragment != null && !mNavigationDrawerFragment.isDrawerOpen()) {
            if (selectedOption == 0) { // Map
                getMenuInflater().inflate(R.menu.map, menu);
                MenuItem menuItem = menu.findItem(R.id.action_visibility);
                if (menuItem != null) {
                    Log.e(TAG, "onPrepareOptionsMenu - Visible: " + MainApplication.visible);
                    if (MainApplication.visible) {
                        menuItem.setIcon(R.drawable.ic_visible);
                    } else {
                        menuItem.setIcon(R.drawable.ic_invisible);
                    }
                }
            } else if (selectedOption == 2) { // Contacts screen
                getMenuInflater().inflate(R.menu.contacts, menu);
            } else if (selectedOption == -10) { // Add contacts screen
                getMenuInflater().inflate(R.menu.add_contact, menu);
            }
            getMenuInflater().inflate(R.menu.main_activity_actions, menu);
            menu.findItem(R.id.action_logout).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {

            case R.id.add_people: // In Contacts (to add new ones)
                FragmentManager fragmentManager = getSupportFragmentManager();

                AddContactsFragment acf = new AddContactsFragment();
                acf.setContactUtils(mContactDataSource);

                fragmentManager.beginTransaction().replace(R.id.container, acf).commit();
                selectedOption = -10;
                supportInvalidateOptionsMenu();
                break;

            case R.id.allow_people: // In list of ALL contacts, when adding new ones.
                DialogUtils.addContact(this);
                break;

            case R.id.action_visibility:
                toggleVisibility();
                break;

            case R.id.action_logout:
                logout();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void logout(){

        PreferenceUtils pu = new PreferenceUtils();

        pu.setString(this, PreferenceUtils.KEY_USER_ACCOUNT, null);
        String uid = pu.getString(this, PreferenceUtils.KEY_USER_ACCOUNT);
        MainApplication.userId = uid;
        Log.e(TAG, "test: " + uid);
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }


    private void toggleVisibility() {

        Utils.setVisibility(!MainApplication.visible, MainActivity.this);
        PreferenceUtils.setBoolean(getApplicationContext(),PreferenceUtils.KEY_SETTING_VISIBILITY, MainApplication.visible);

        if (MainApplication.visible) {
            Toast.makeText(this, getResources().getString(R.string.you_are_visible), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, getResources().getString(R.string.you_are_invisible), Toast.LENGTH_LONG).show();
        }

        supportInvalidateOptionsMenu();
    }


    @Override
    public boolean onKeyUp(int keycode, KeyEvent e) {
        switch (keycode) {
            case KeyEvent.KEYCODE_MENU:
                mNavigationDrawerFragment.toggleDrawer();
                return true;

            case KeyEvent.KEYCODE_BACK:
                if (selectedOption == 0) {
                    Log.e(TAG, "Exiting app because requested by user.");
                    finish();
                } else if (selectedOption == -10) { // -10 is add contacts screen
                    mNavigationDrawerFragment.selectItem(3);    // 3 is contacts screen
                    return true;
                } else {
                    mNavigationDrawerFragment.selectItem(1);
                    return true;
                }
        }
        return super.onKeyUp(keycode, e);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        Log.e(TAG, "onActivityResult");

        if (requestCode == REQUEST_CODE_EMAIL) {
            if (resultCode == RESULT_OK) {
                Log.e(TAG, "Returned from sign up. Now we will show the map.");
                startServices();
                mNavigationDrawerFragment.setUserInfo();
                GcmHelper.start(getApplicationContext()); // Register to GCM

            } else {
                Log.e(TAG, "Returned from sign up. Exiting app on request.");
                finish();
            }

        } else if (requestCode == REQUEST_TERMS && resultCode == RESULT_OK) {
            Log.e(TAG, "Returned from terms. Now we will show sign up form.");
            // Terms shown and accepted.

        } else {
            Log.e(TAG, "Got - request Code: " + requestCode + ", result: " + resultCode);
            finish();
        }
    }

    public void showUserInMap(View view) { // Used in Contacts

        if (view == null) {
            return;
        }
        ImageView image = (ImageView) view;
        String email = (String) image.getTag();
        showUserInMap(email);
    }

    public void showUserInMap(String email) { // Used in Contacts

        Log.e(TAG, "showUserInMap: " + email);
        MainApplication.emailBeingTracked = email;
        MainApplication.showPlaces = false;
        mNavigationDrawerFragment.selectItem(0);
    }

    public void toggleIDontWantToSee(View view) {

        if (view == null) {
            return;
        }
        CheckBox checkBox = (CheckBox) view;
        Boolean allow = checkBox.isChecked();
        String email = (String) checkBox.getTag();
        Log.e(TAG, "toggleIDontWantToSee: " + email + ", Checkbox is: " + allow);
        if (!allow) {
            try {
                MainApplication.iDontWantToSee.put(email, 1);
                PreferenceUtils.setString(this, PreferenceUtils.KEY_I_DONT_WANT_TO_SEE, MainApplication.iDontWantToSee.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (MainApplication.iDontWantToSee.has(email)) {
            MainApplication.iDontWantToSee.remove(email);
            PreferenceUtils.setString(this, PreferenceUtils.KEY_I_DONT_WANT_TO_SEE, MainApplication.iDontWantToSee.toString());
        }
    }

    public void toggleUserCanSeeMe(View view) { // Used in Contacts

        if (view != null) {
            CheckBox checkBox = (CheckBox) view;
            Boolean allow = checkBox.isChecked();
            String email = (String) checkBox.getTag();
            Log.e(TAG, "toggleUserCanSeeMe: " + email + ", Checkbox is: " + allow);
            if (!allow) {
                try {
                    ServerApi.disallowUser(this, email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    ServerApi.allowPeople(this, email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private BroadcastReceiver exitMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "exitMessageReceiver onReceive");

            LocationService.stop(MainActivity.this.getApplicationContext());
            DataService.stop(MainActivity.this.getApplicationContext());

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle(context.getResources().getString(R.string.app_name));
            String message = context.getResources().getString(R.string.security_sign_up, MainApplication.userAccount);
            alertDialog.setMessage(message)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .setCancelable(false);
            alertDialog.show();
        }
    };

    private BroadcastReceiver switchToMapReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, new MapViewFragment()).commit();
            mNavigationDrawerFragment.selectItem(1);    // Index 1 because index 0 is the list view header...
        }
    };

    // For dependency injection
    public void setContactUtils(ContactDataSource contactDataSource) {
        this.mContactDataSource = contactDataSource;
    }

}
