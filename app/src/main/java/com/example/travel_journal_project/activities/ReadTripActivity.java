package com.example.travel_journal_project.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travel_journal_project.R;
import com.example.travel_journal_project.viewmodel.TripViewModel;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReadTripActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.example.travel_journal_project.activities.EXTRA_ID";
    public static final String EXTRA_NAME = "com.example.travel_journal_project.activities.EXTRA_NAME";
    public static final String EXTRA_DESTINATION = "com.example.travel_journal_project.activities.EXTRA_DESTINATION";
    public static final String EXTRA_TYPE = "com.example.travel_journal_project.activities.EXTRA_TYPE";
    public static final String EXTRA_RATING = "com.example.travel_journal_project.activities.EXTRA_RATING";
    public static final String EXTRA_PRICE = "com.example.travel_journal_project.activities.EXTRA_PRICE";
    public static final String EXTRA_START_DATE = "com.example.travel_journal_project.activities.EXTRA_START_DATE";
    public static final String EXTRA_END_DATE = "com.example.travel_journal_project.activities.EXTRA_END_DATE";
    public static final String EXTRA_FAVORITE = "com.example.travel_journal_project.activities.EXTRA_FAVORITE";
    public static final String EXTRA_URL = "com.example.travel_journal_project.activities.EXTRA_URL";
    private static final String API_KEY = "e4b67f36ff0d78945ab3b63ddcb777de";

    private ImageView readTripPhotoUrl;
    private TextView readTripName;
    private TextView readTripDestination;
    private TextView readTripPrice;
    private TextView readTripRating;
    private TextView readTripStartDate;
    private TextView readTripEndDate;
    private TextView readTripType;
    private TextView tripNameToolbar;
    private FloatingActionButton tripFavoriteButton;

    private TripViewModel tripViewModel;

    private boolean tripIsFavorite;

    private TextView locationTemperature;
    private TextView weatherDescription;
    private ImageView descriptionImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_trip);


        setToolbar();
        setComponents();
        fillFields();
        setTemperature();


    }

    public void addRemoveFromFavorites(long id) {

        if (tripIsFavorite == true) {
            tripFavoriteButton.setImageResource(R.drawable.not_favorite);
            tripViewModel.addToFavorite(id, false);
            Toast.makeText(ReadTripActivity.this, "removed", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            tripFavoriteButton.setImageResource(R.drawable.is_favorite);
            tripViewModel.addToFavorite(id, true);
            Toast.makeText(ReadTripActivity.this, "added", Toast.LENGTH_SHORT).show();
            finish();
        }


    }


    public void onBackButtonClicked(View view) {
        onBackPressed();
    }

    public void fillFields() {

        Intent intent = getIntent();
        tripNameToolbar.setText(intent.getStringExtra(EXTRA_NAME));
        String url = intent.getStringExtra(EXTRA_URL);
        if (url != null) {
            Picasso.get().load(url).into(readTripPhotoUrl);
        } else {
            Picasso.get().load(R.drawable.imageholder).into(readTripPhotoUrl);
        }
        readTripStartDate.setText(intent.getStringExtra(EXTRA_START_DATE));
        readTripEndDate.setText(intent.getStringExtra(EXTRA_END_DATE));
        readTripName.setText(intent.getStringExtra(EXTRA_NAME));
        readTripDestination.setText(intent.getStringExtra(EXTRA_DESTINATION).toString());
        readTripType.setText(intent.getStringExtra(EXTRA_TYPE));
        readTripRating.setText(intent.getFloatExtra(EXTRA_RATING, 0) + " ✪ ");
        readTripPrice.setText(intent.getIntExtra(EXTRA_PRICE, 0) + " € ");
        tripIsFavorite = intent.getBooleanExtra(EXTRA_FAVORITE, false);
        if (tripIsFavorite == true) {
            tripFavoriteButton.setImageResource(R.drawable.is_favorite);
        } else {
            tripFavoriteButton.setImageResource(R.drawable.not_favorite);
        }
        long id = intent.getLongExtra(EXTRA_ID, 0);
        showTemperature(intent.getStringExtra(EXTRA_DESTINATION).trim());


        tripFavoriteButton.setOnClickListener(v -> addRemoveFromFavorites(id));


    }


    public void setTemperature() {
        locationTemperature = findViewById(R.id.locationTemperature);
        weatherDescription = findViewById(R.id.weatherDescription);
        descriptionImageView = findViewById(R.id.descriptionImageView);

    }

    public void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void setComponents() {

        readTripPhotoUrl = findViewById(R.id.readTripImage);
        tripNameToolbar = findViewById(R.id.trip_name_toolbar);
        tripFavoriteButton = findViewById(R.id.tripFavoriteButton);
        readTripPhotoUrl = findViewById(R.id.readTripImage);
        readTripName = findViewById(R.id.readTripName);
        readTripDestination = findViewById(R.id.readTripDestination);
        readTripPrice = findViewById(R.id.readTripPrice);
        readTripRating = findViewById(R.id.readTripRating);
        readTripStartDate = findViewById(R.id.readItemStartDate);
        readTripEndDate = findViewById(R.id.readTripEndDate);
        readTripType = findViewById(R.id.readTripType);
        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);
    }


    public void showTemperature(String cityName) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&APPID=" + API_KEY + "&units=Metric";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("main");
                    JSONArray jsonArray = response.getJSONArray("weather");
                    JSONObject object = jsonArray.getJSONObject(0);
                    double temperatureDouble = jsonObject.getDouble("temp");
                    String description = object.getString("main");


                    double temperatureRounded = (int) Math.round(temperatureDouble);
                    int temperatureInt = (int) temperatureRounded;
                    String temp = String.valueOf(temperatureInt + "°C");

                    locationTemperature.setText(temp);

                    if (description.contains("Cloud")) {
                        descriptionImageView.setImageResource(R.drawable.ic_cloud);
                        weatherDescription.setText(description);
                    } else if (description.contains("Clear")) {
                        descriptionImageView.setImageResource(R.drawable.ic_sun);
                        weatherDescription.setText(description);
                    } else if (description.contains("Rain")) {
                        descriptionImageView.setImageResource(R.drawable.ic_rain);
                        weatherDescription.setText(description);
                    } else if (description.contains("Snow")) {
                        descriptionImageView.setImageResource(R.drawable.ic_snow);
                        weatherDescription.setText(description);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> error.printStackTrace());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}
