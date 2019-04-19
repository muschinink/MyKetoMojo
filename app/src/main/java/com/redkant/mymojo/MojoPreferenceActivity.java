package com.redkant.mymojo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import androidx.annotation.Nullable;

public class MojoPreferenceActivity extends PreferenceActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getFragmentManager().findFragmentById(android.R.id.content) == null) {
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, new MyPreferenceFragment())
                    .commit();
        }
    }

    public static class MyPreferenceFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener,
                        SharedPreferences.OnSharedPreferenceChangeListener {

//        SharedPreferences sharedPreferences;
        private final static String PREF_DAYS_COUNT = "DaysCount";

        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

//            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//            onSharedPreferenceChanged(sharedPreferences, "DaysCount");

            findPreference(PREF_DAYS_COUNT).setOnPreferenceChangeListener(this);
            int aNumber = getPreferenceManager().getSharedPreferences().getInt(PREF_DAYS_COUNT, -1);

            Preference daysCountPreference = findPreference(PREF_DAYS_COUNT);

            if (aNumber == -1) {
                aNumber = 7;

                if (daysCountPreference instanceof NumberPickerPreference) {
                    ((NumberPickerPreference) daysCountPreference).setValue(7);
                }
            }
            daysCountPreference.setSummary(Integer.toString(aNumber));
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
/*
            Preference preference = findPreference(key);

            if (preference instanceof EditTextPreference) {
                preference.setSummary(((EditTextPreference) preference).getText());
            }
*/
        }

        /**
         * Called when a Preference has been changed by the user. This is called before the state of the
         * Preference is about to be updated and before the state is persisted. Return true to persist
         * the changed value, or false to ignore the change.
         */
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            switch (preference.getKey()) {
                case PREF_DAYS_COUNT:
                    preference.setSummary(newValue.toString());
                    return true;
                default:
                    return true;
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }
    }
}
