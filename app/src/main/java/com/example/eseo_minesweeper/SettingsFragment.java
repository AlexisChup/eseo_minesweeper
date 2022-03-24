package com.example.eseo_minesweeper;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {


    public static final String KEY_PREF_ROWS = "pref_rows";
    public static final String KEY_PREF_COLUMNS = "pref_columns";
    public static final String KEY_PREF_BOMBS = "pref_bombs";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //addPreferencesFromResource(R.xml.preferences);

    }




}
