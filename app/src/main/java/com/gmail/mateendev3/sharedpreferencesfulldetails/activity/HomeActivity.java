package com.gmail.mateendev3.sharedpreferencesfulldetails.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.mateendev3.sharedpreferencesfulldetails.databinding.ActivityHomeBinding;
import com.gmail.mateendev3.sharedpreferencesfulldetails.model.Car;
import com.google.gson.Gson;

/**
 * (1) --> AppLevel SharedPreference / Custom SharedPreference
 * (2) --> Activity based Preference / default preference
 * (3) --> attaching listener to default preference set / setting activity preference set
 * (4) --> getting data from intent for GSON object which is in JSON string object stored in the shared prefs
 * (6) --> Delete prefs associated with key | delete all prefs
 */

public class HomeActivity extends AppCompatActivity {

    //declaring members and constants
    private static final String GAME_NAME_KEY = "game.name.key";
    private static final String TAG = "TAGGG";
    private SharedPreferences.OnSharedPreferenceChangeListener mListener;
    private SharedPreferences.OnSharedPreferenceChangeListener mJSONListener;
    private ActivityHomeBinding binding;
    TextView tvEmail, tvGameName, tvDataFromSettings, tvCarName;
    EditText etGameName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //method invoked to initViews
        initViews();

        //(1)
        //method invoked to set data to tvEmail which we will get from Login activity
        //also inserting that data to shared prefs
        setDataToTVEmail();

        //(2)
        //getting data from activity based prefs and set to tvGameName
        setDataToTVGameName();

        //(2)
        //getting data from etGameName and store it in the default preferences / activity based preferences
        getDataFromETGameAndStoreItSharedPrefs();

        //(3)
        //add listener to button to take to all settings activity
        setListenerToButton();

        //(3)
        //attaching listener to default preference set / setting activity preference set
        //OnAttachSharedPreferenceChangedListener
        setListenerToSettingsActivitySettingChanged();

        //(3)
        //setting data to tvDataFromSettings when app launched
        setDataToTVDataFromSettings();

        Log.d(TAG, "onCreate: ");
        //(4)
        //setting data to tvJSONCarData
        setDataToCarTV();

        //(4)
        //getting data from intent for GSON object which is in JSON string object stored in the shared prefs
        getDataFromSharedPrefsAndSetToTVUsingAttachListener();

    }

    private void setDataToCarTV() {
        SharedPreferences preferences = getSharedPreferences(LoginActivity.JSON_SHARED_PREFS, MODE_PRIVATE);
        Car car = getCar(preferences);
        tvCarName.setText(car.getCarName());
    }

    private void getDataFromSharedPrefsAndSetToTVUsingAttachListener() {
        mJSONListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals(LoginActivity.JSON_USER_STR)) {
                    Car car = getCar(sharedPreferences);
                    Toast.makeText(HomeActivity.this, "Hello " + car.getCarName(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onSharedPreferenceChanged: ");
                }
            }
        };
        SharedPreferences preferences = getSharedPreferences(LoginActivity.JSON_SHARED_PREFS, MODE_PRIVATE);
        preferences.registerOnSharedPreferenceChangeListener(mJSONListener);
    }

    private Car getCar(SharedPreferences sharedPreferences) {
        Gson gson = new Gson();
        String stringJSON = sharedPreferences.getString(LoginActivity.JSON_USER_STR, "Nothing");
        return gson.fromJson(stringJSON, Car.class);
    }

    private void setDataToTVDataFromSettings() {
        //getting instance of prefs
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = preferences.getString("signature", "no obj");
        boolean synced = preferences.getBoolean("sync", false);
        tvDataFromSettings.setText(name + "\n");
        tvDataFromSettings.append(synced ? "true" : "false");
    }

    /**
     * method to set listener to settings activity default shared preferences changed listener
     */
    private void setListenerToSettingsActivitySettingChanged() {
        mListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals("signature"))
                    tvDataFromSettings.setText(sharedPreferences.getString("signature", "no obj") + "\n");
                else if (key.equals("sync")) {
                    boolean synced = sharedPreferences.getBoolean("sync", false);
                    tvDataFromSettings.append(synced ? "true" : "false");
                }
            }
        };
        //getting instance of default shared preference / settings activity preference
        SharedPreferences settingsPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        //setting / registering the prefs changed listener to settings prefs
        settingsPrefs.registerOnSharedPreferenceChangeListener(mListener);
    }


    private void setListenerToButton() {
        binding.btnGoToAllSettings.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    private void setDataToTVGameName() {
        //getting instance of of shared prefs
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        String gameName = preferences.getString(GAME_NAME_KEY, "no game name");
        if (gameName != null)
            tvGameName.setText(gameName);
    }

    private void getDataFromETGameAndStoreItSharedPrefs() {

        //getting instance of etGameName and Store it in the Shared prefs and set to tvGameName
        binding.btnSaveGame.setOnClickListener(v -> {
            String gameName = etGameName.getText().toString();

            if (gameName.equals("no game name") || gameName.isEmpty()) {
                Toast.makeText(this, "No game name in the edit text", Toast.LENGTH_SHORT).show();
                return;
            }

            //setting data to tvGameName
            tvGameName.setText(gameName);

            //saving game name in the default shared prefs
            //getting instance of shared prefs editor for modifications
            SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
            editor.putString(GAME_NAME_KEY, gameName);
            editor.apply();

        });

    }

    private void setDataToTVEmail() {
        String email = getIntent().getStringExtra(LoginActivity.EMAIL_KEY);

        if (email != null) {
            tvEmail.setText(email);

            //inserting data to shared prefs
            //getting instance of AppLevel / Custom shared prefs if don't exits
            SharedPreferences preferences = getSharedPreferences(LoginActivity.FIRST_SHARED_PREFS, MODE_PRIVATE);
            //Creating instance of prefs editor to insert data
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(LoginActivity.EMAIL_KEY, email);
            editor.apply();
        }

        //setting listener to tvEmail to finish the activity
        tvEmail.setOnClickListener(v -> {
            finish();
        });
    }

    private void initViews() {
        tvEmail = binding.tvEmail;
        tvGameName = binding.tvGameName;
        etGameName = binding.etGameName;
        tvDataFromSettings = binding.tvDataFromSettings;
        tvCarName = binding.tvJsonCarData;
    }
}