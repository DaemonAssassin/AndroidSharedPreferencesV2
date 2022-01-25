package com.gmail.mateendev3.sharedpreferencesfulldetails.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.gmail.mateendev3.sharedpreferencesfulldetails.R;
import com.gmail.mateendev3.sharedpreferencesfulldetails.databinding.ActivityLoginBinding;
import com.gmail.mateendev3.sharedpreferencesfulldetails.model.Car;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {

    //declaring members and constants
    public static final String EMAIL_KEY = "email.key";
    public static final String FIRST_SHARED_PREFS = "first.shared.prefs.key";
    public static final String JSON_SHARED_PREFS = "json.shared.prefs.key";
    public static final String JSON_USER_STR = "json.user.string.key";
    private ActivityLoginBinding binding;
    EditText etEmail, etCarName, etCarModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //init views
        initViews();

        //getting data from etEmail and send it to home activity
        getDataFromETAndTakeToHome();

        //(4)
        //setting listener to btn to save car name and car model to model object
        setListenerToBtnSaveCarDataAndMove();

        //(5)
        //delete email from prefs
        deleteDataFromPrefs();


    }

    private void deleteDataFromPrefs() {
        binding.btnDeleteEmail.setOnClickListener(v -> {
            SharedPreferences.Editor editor =
                    getSharedPreferences(FIRST_SHARED_PREFS, MODE_PRIVATE).edit();
            editor.clear();
            editor.apply();
        });
    }

    private void setListenerToBtnSaveCarDataAndMove() {
        binding.btnSaveDateToSharedPrefs.setOnClickListener(v -> {
            String carName = etCarName.getText().toString();
            String carModel = etCarModel.getText().toString();
            if (carName.isEmpty() || carModel.isEmpty()) {
                return;
            }
            Car car = new Car(carName, Integer.parseInt(carModel));

            //creating instance of GSON object after adding dependency
            Gson gson = new Gson();
            //converting car object to JSON using gson object through serialization
            String userJSONString = gson.toJson(car, Car.class);
            SharedPreferences.Editor prefsEditor = getSharedPreferences(JSON_SHARED_PREFS, MODE_PRIVATE).edit();
            prefsEditor.putString(JSON_USER_STR, userJSONString);
            prefsEditor.apply();


            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //setting data from sharedPreference to etEmail
        setDataToETFromSharedPrefs();
    }

    /**
     * method to set data to etEmail form shared prefs when app launches
     */
    private void setDataToETFromSharedPrefs() {
        //getting instance of AppLevel / Custom shared prefs
        SharedPreferences preferences = getSharedPreferences(FIRST_SHARED_PREFS, MODE_PRIVATE);
        String email = preferences.getString(EMAIL_KEY, "no email");
        etEmail.setText(email);
    }

    /**
     * method to get data form etEmail and send it to home activity
     */
    private void getDataFromETAndTakeToHome() {
        binding.btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString();

            //condition if email is empty
            if (email.isEmpty()) {
                Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra(EMAIL_KEY, email);
            startActivity(intent);
        });
    }

    /**
     * method to init views
     */
    private void initViews() {
        etEmail = binding.etEmail;
        etCarName = binding.etCarName;
        etCarModel = binding.etCarModel;
    }
}