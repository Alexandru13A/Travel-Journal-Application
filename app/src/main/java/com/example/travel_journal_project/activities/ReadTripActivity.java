package com.example.travel_journal_project.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travel_journal_project.R;
import com.example.travel_journal_project.models.Trip;
import com.example.travel_journal_project.viewmodel.TripViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

    private ImageView readTripPhotoUrl;
    private TextView readTripName;
    private TextView readTripDestination;
    private TextView readTripPrice;
    private TextView readTripRating;
    private TextView readTripStartDate;
    private TextView readTripEndDate;
    private TextView readTripType;
    private FloatingActionButton tripFavoriteButton;

    private TripViewModel tripViewModel;

    private boolean tripIsFavorite;

    private final SimpleDateFormat inputDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault());
    private final SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_trip);

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


        Intent intent = getIntent();
        setTitle(intent.getStringExtra(EXTRA_NAME));
        String startingDate = intent.getStringExtra(EXTRA_START_DATE);
        String endingDate = intent.getStringExtra(EXTRA_END_DATE);
        try {
            Date startDate = inputDateFormat.parse(startingDate);
            String formatStartDate = outputDateFormat.format(startDate);

            Date endDate = inputDateFormat.parse(endingDate);
            String formatEndingDate = outputDateFormat.format(endDate);
            readTripStartDate.setText(formatStartDate);
            readTripEndDate.setText(formatEndingDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        readTripName.setText(intent.getStringExtra(EXTRA_NAME));
        readTripDestination.setText(intent.getStringExtra(EXTRA_DESTINATION));
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

        tripFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRemoveFromFavorites(id);
            }
        });

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


}
