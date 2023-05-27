package com.example.travel_journal_project.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.travel_journal_project.R;
import com.example.travel_journal_project.models.Trip;
import com.example.travel_journal_project.viewmodel.TripViewModel;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTripActivity extends AppCompatActivity {


    private TripViewModel tripViewModel;

    private EditText tripNameEditText;
    private EditText tripDestinationEditText;
    private RadioGroup tripTypeRadioGroup;
    private RatingBar tripRatingBar;
    private SeekBar tripPricePicker;
    private TextView tripPriceTextView;
    private TextView startTripDate;
    private TextView endTripDate;
    private Boolean isFavorite;

    private TextView activityNameToolbar;
    private Button saveTripButton;
    private ImageView tripItemImage;
    private Button tripGalleryButton;
    public static final int REQUEST_PICK_IMAGE = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);
        activityNameToolbar = findViewById(R.id.activity_name_toolbar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        activityNameToolbar.setText("Create a new Trip");
        tripItemImage = findViewById(R.id.tripItemImage);

        tripGalleryButton = findViewById(R.id.tripGalleryButton);
        saveTripButton = findViewById(R.id.createTripSaveButton);
        isFavorite = false;
        tripNameEditText = findViewById(R.id.createTripNameEditText);
        tripDestinationEditText = findViewById(R.id.createTripDestinationEditText);
        tripTypeRadioGroup = findViewById(R.id.createRadioGroup);
        tripRatingBar = findViewById(R.id.createRatingBar);
        tripPriceTextView = findViewById(R.id.tripPriceTextView);
        tripPricePicker = findViewById(R.id.createPriceSeekBar);
        startTripDate = findViewById(R.id.selectStartingDateEditText);
        endTripDate = findViewById(R.id.selectEndingDateEditText);


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
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddTripActivity.this, new DatePickerDialog.OnDateSetListener() {
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
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddTripActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        tripGalleryButton.setOnClickListener(v -> {
            openGallery();
        });

        saveTripButton.setOnClickListener(v -> {
            saveNote();
            finish();
        });


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

    private Date extractDateFromEditText(TextView tripDate) {
        try {
            String dateStr = tripDate.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveNote() {
        String tripName = tripNameEditText.getText().toString();
        String tripDestination = tripDestinationEditText.getText().toString();
        String tripType = tripTypePicker();
        int tripPrice = tripPricePicker.getProgress();
        String startTrip = String.valueOf(extractDateFromEditText(startTripDate));
        String endTrip = String.valueOf(extractDateFromEditText(endTripDate));
        float tripRating = tripRatingBar.getRating();
        String imageUrl = imageUri != null ? getImagePath(imageUri) : null;
        if (tripName.trim().isEmpty() || tripDestination.trim().isEmpty() || tripType.equals("") || tripPrice <= 0) {
            Toast.makeText(this, "Please fill all spaces", Toast.LENGTH_SHORT).show();

        } else {
            Trip trip = new Trip(tripName, tripDestination, tripType, startTrip, endTrip, tripRating, tripPrice, imageUrl);
            tripViewModel.insert(trip);
            Toast.makeText(AddTripActivity.this, "TRIP SAVED", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackButtonClicked(View view) {
        onBackPressed();
        Toast.makeText(this, "Trip not saved", Toast.LENGTH_SHORT).show();
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, REQUEST_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(tripItemImage);
        }
    }

    private String getImagePath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String imagePath = cursor.getString(columnIndex);
            cursor.close();
            return imagePath;
        }
        return uri.getPath();
    }
}
