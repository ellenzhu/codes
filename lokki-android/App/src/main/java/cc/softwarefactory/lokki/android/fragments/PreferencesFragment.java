package cc.softwarefactory.lokki.android.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.preference.PreferenceFragment;
import android.preference.ListPreference;
import android.util.Log;

import cc.softwarefactory.lokki.android.MainApplication;
import cc.softwarefactory.lokki.android.R;
import cc.softwarefactory.lokki.android.utilities.PreferenceUtils;
import cc.softwarefactory.lokki.android.utilities.Utils;

public class PreferencesFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "PreferencesFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.e(TAG, "onSharedPreferenceChanged key: " + key);
        if (key.equals(PreferenceUtils.KEY_SETTING_MAP_MODE)) {
            int mapMode = Integer.parseInt(sharedPreferences.getString(PreferenceUtils.KEY_SETTING_MAP_MODE, "0"));
            ListPreference preference = (ListPreference) findPreference(key);
            preference.setSummary(preference.getEntry());
            MainApplication.mapType = mapMode;
        } else if (key.equals(PreferenceUtils.KEY_SETTING_VISIBILITY)) {
            boolean visible = sharedPreferences.getBoolean(PreferenceUtils.KEY_SETTING_VISIBILITY, true);
            Utils.setVisibility(visible, getActivity());
        }
    }
}