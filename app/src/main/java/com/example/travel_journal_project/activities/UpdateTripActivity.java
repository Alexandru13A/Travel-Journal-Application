package com.example.travel_journal_project.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travel_journal_project.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UpdateTripActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.example.travel_journal_project.activities.EXTRA_ID";
    public static final String EXTRA_NAME = "com.example.travel_journal_project.activities.EXTRA_NAME";
    public static final String EXTRA_DESTINATION = "com.example.travel_journal_project.activities.EXTRA_DESTINATION";
    public static final String EXTRA_TYPE = "com.example.travel_journal_project.activities.EXTRA_TYPE";
    public static final String EXTRA_RATING = "com.example.travel_journal_project.activities.EXTRA_RATING";
    public static final String EXTRA_PRICE = "com.example.travel_journal_project.activities.EXTRA_PRICE";
    public static final String EXTRA_START_DATE = "com.example.travel_journal_project.activities.EXTRA_START_DATE";
    public static final String EXTRA_END_DATE = "com.example.travel_journal_project.activities.EXTRA_END_DATE";


    private EditText tripNameEditText;
    private EditText tripDestinationEditText;
    private RadioGroup tripTypeRadioGroup;
    private RatingBar tripRatingBar;
    private SeekBar tripPricePicker;
    private TextView tripPriceTextView;
    private EditText startTripDate;
    private EditText endTripDate;
    private Button updateButton;
    SimpleDateFormat inputDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault());
    SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);


        updateButton = findViewById(R.id.createTripSaveButton);
        tripNameEditText = findViewById(R.id.createTripNameEditText);
        tripDestinationEditText = findViewById(R.id.createTripDestinationEditText);
        tripTypeRadioGroup = findViewById(R.id.createRadioGroup);
        tripRatingBar = findViewById(R.id.createRatingBar);
        tripPriceTextView = findViewById(R.id.tripPriceTextView);
        tripPricePicker = findViewById(R.id.createPriceSeekBar);
        startTripDate = findViewById(R.id.selectStartingDateEditText);
        endTripDate = findViewById(R.id.selectEndingDateEditText);


        Intent intent = getIntent();
        setTitle("UPDATE TRIP");
        if (intent.hasExtra(EXTRA_ID)) {
            String startingDate = intent.getStringExtra(EXTRA_START_DATE);
            String endingDate = intent.getStringExtra(EXTRA_END_DATE);
            try {
                Date startDate = inputDateFormat.parse(startingDate);
                String formatStartDate = outputDateFormat.format(startDate);

                Date endDate = inputDateFormat.parse(endingDate);
                String formatEndingDate = outputDateFormat.format(endDate);
                startTripDate.setText(formatStartDate);
                endTripDate.setText(formatEndingDate);

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            tripNameEditText.setText(intent.getStringExtra(EXTRA_NAME));
            tripDestinationEditText.setText(intent.getStringExtra(EXTRA_DESTINATION));
            tripTypeRadioGroup.getCheckedRadioButtonId();
            tripRatingBar.setRating(intent.getFloatExtra(EXTRA_RATING, 0));
            tripPriceTextView.setText(String.valueOf(intent.getIntExtra(EXTRA_PRICE, 0)));
        }

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        tripPricePicker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tripPriceTextView.setText(String.valueOf(tripPricePicker.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }


            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        startTripDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateTripActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year12, int month12, int day12) {
                    month12 = month12 + 1;
                    Calendar calendar12 = Calendar.getInstance();
                    calendar12.set(year12, month12, day12);
                    calendar12.getTime();
                    String date = day12 + "/" + month12 + "/" + year12;
                    startTripDate.setText(date);
                }
            }, year, month, day);
            datePickerDialog.show();
        });
        endTripDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateTripActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year1, int month1, int day1) {
                    month1 = month1 + 1;
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.set(year1, month1, day1);
                    calendar1.getTime();
                    String date = day1 + "/" + month1 + "/" + year1;
                    endTripDate.setText(date);
                }
            }, year, month, day);
            datePickerDialog.show();
        });
        updateButton.setOnClickListener(v -> {
            updateTrip();
            Intent intent1 = new Intent(UpdateTripActivity.this, TripsActivity.class);
            startActivity(intent1);
        });

    }


    private void updateTrip() {
        String tripName = tripNameEditText.getText().toString();
        String tripDestination = tripDestinationEditText.getText().toString();
        String tripType = tripTypePicker();
        int tripPrice = tripPricePicker.getProgress();
        String startTrip = String.valueOf(extractDateFromEditText(startTripDate));
        String endTrip = String.valueOf(extractDateFromEditText(endTripDate));
        float tripRating = tripRatingBar.getRating();

        long id = getIntent().getLongExtra(EXTRA_ID, -1);

        if (tripName.trim().isEmpty() || tripDestination.trim().isEmpty() || tripType.equals("") || tripPrice <= 0 || startTrip.trim().isEmpty() || endTrip.trim().isEmpty()) {
            Toast.makeText(this, "Please fill all spaces", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent data = new Intent();
        data.putExtra(EXTRA_NAME, tripName);
        data.putExtra(EXTRA_DESTINATION, tripDestination);
        data.putExtra(EXTRA_TYPE, tripType);
        data.putExtra(EXTRA_PRICE, tripPrice);
        data.putExtra(EXTRA_START_DATE, startTrip);
        data.putExtra(EXTRA_END_DATE, endTrip);
        data.putExtra(EXTRA_RATING, tripRating);

        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();
    }


    private String tripTypePicker() {
        String tripTypeString = "";
        int selectChoice = tripTypeRadioGroup.getCheckedRadioButtonId();

        if (selectChoice == R.id.tripTypeOne) {
            tripTypeString = "City Break";
        } else if (selectChoice == R.id.tripTypeTwo) {
            tripTypeString = "Sea Side";

        } else if (selectChoice == R.id.tripTypeThree) {
            tripTypeString = "Mountains";

        }
        return tripTypeString;
    }

    private Date extractDateFromEditText(EditText tripDate) {
        try {
            String dateStr = tripDate.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.createTripSaveButton) {
            updateTrip();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }


}
