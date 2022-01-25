package com.gmail.mateendev3.sharedpreferencesfulldetails.fragment;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.gmail.mateendev3.sharedpreferencesfulldetails.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}