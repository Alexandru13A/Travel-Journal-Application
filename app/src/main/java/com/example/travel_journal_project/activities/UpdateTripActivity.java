package com.example.travel_journal_project.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travel_journal_project.MainActivity;
import com.example.travel_journal_project.R;
import com.example.travel_journal_project.models.Trip;
import com.example.travel_journal_project.viewmodel.TripViewModel;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpdateTripActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.example.travel_journal_project.activities.EXTRA_ID";

    private EditText tripNameEditText;
    private EditText tripDestinationEditText;
    private RadioGroup tripTypeRadioGroup;
    private RatingBar tripRatingBar;
    private SeekBar tripPricePicker;
    private TextView tripPriceTextView;
    private TextView startTripDate;
    private TextView endTripDate;
    private ImageButton updateButton;
    private TextView activityNameToolbar;
    private ImageView tripItemImage;
    private ImageButton tripGalleryButton;
    private TripViewModel tripViewModel;


    public static final int REQUEST_PICK_IMAGE = 1;

    private byte[] imageTrip;
    private Uri imageUri;
    private Bitmap bitmap;
    private int year;
    private int month;
    private int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_trip);


        setToolbar();
        componentsInitialization();
        fillTheFields();


    }

    public void fillTheFields() {
        Intent intent = getIntent();
        activityNameToolbar.setText(R.string.update_activity_name);
        long id = intent.getLongExtra(EXTRA_ID, 0);

        if (id != -1) {

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> {
                Trip trip = tripViewModel.getTripById(id);
                String tripName = trip.getTripName();
                String tripDestination = trip.getTripDestination();
                String tripType = trip.getTripType();
                String startDate = trip.getTripStartDate();
                String endDate = trip.getTripEndDate();
                float tripRating = trip.getTripRating();
                int tripPrice = trip.getTripPrice();
                imageTrip = trip.getTripImage();

                runOnUiThread(() -> {
                    if (trip != null) {
                        tripNameEditText.setText(tripName);
                        tripDestinationEditText.setText(tripDestination);
                        startTripDate.setText(startDate);
                        endTripDate.setText(endDate);
                        tripPriceTextView.setText(String.valueOf(tripPrice));
                        Bitmap tripImageBitmap = BitmapFactory.decodeByteArray(imageTrip, 0, imageTrip.length);
                        tripItemImage.setImageBitmap(tripImageBitmap);
                        tripRatingBar.setRating(tripRating);
                        tripPricePicker.setProgress(tripPrice);


                        if (tripType.equals("City Break")) {
                            tripTypeRadioGroup.check(R.id.tripTypeOne);
                        } else if (tripType.equals("Sea Side")) {
                            tripTypeRadioGroup.check(R.id.tripTypeTwo);
                        } else if (tripType.equals("Mountains")) {
                            tripTypeRadioGroup.check(R.id.tripTypeThree);
                        }
                    }

                });
            });
        }


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
        updateButton.setOnClickListener(v -> {
            updateTrip();
            Intent intent1 = new Intent(UpdateTripActivity.this, MainActivity.class);
            startActivity(intent1);
        });

        tripGalleryButton.setOnClickListener(v -> {
            openGallery();
        });
    }


    private void updateTrip() {
        String tripName = tripNameEditText.getText().toString();
        String tripDestination = tripDestinationEditText.getText().toString();
        String tripType = tripTypePicker();
        int tripPrice = tripPricePicker.getProgress();
        String startTrip = startTripDate.getText().toString().trim();
        String endTrip = endTripDate.getText().toString().trim();
        float tripRating = tripRatingBar.getRating();

        if (imageTrip != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            imageTrip = stream.toByteArray();
        } else {
            imageTrip = new byte[]{0};
        }
        long id = getIntent().getLongExtra(EXTRA_ID, -1);

        if (tripName.trim().isEmpty() || tripDestination.trim().isEmpty() || tripType.equals("") || tripPrice <= 0 || startTrip.trim().isEmpty() || endTrip.trim().isEmpty()) {
            Toast.makeText(this, "Please fill all spaces", Toast.LENGTH_SHORT).show();

        } else if (id == -1) {
            Toast.makeText(this, "NOTE CAN'T BE UPDATED", Toast.LENGTH_SHORT).show();

        } else {

            Trip trip = new Trip(tripName, tripDestination, tripType, startTrip, endTrip, tripRating, tripPrice, imageTrip);
            trip.setTripId(id);
            tripViewModel.updateTrip(trip);
            Toast.makeText(UpdateTripActivity.this, "TRIP SAVED", Toast.LENGTH_SHORT).show();

            finish();
        }
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


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.createTripSaveButton) {
            updateTrip();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public void onBackButtonClicked(View view) {
        onBackPressed();
        Toast.makeText(this, "Trip not updated", Toast.LENGTH_SHORT).show();
    }

    public void setToolbar() {
        activityNameToolbar = findViewById(R.id.activity_name_toolbar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        activityNameToolbar.setText("Add Trip");

    }

    public void componentsInitialization() {
        tripGalleryButton = findViewById(R.id.tripGalleryButton);
        tripItemImage = findViewById(R.id.tripItemImage);
        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);
        updateButton = findViewById(R.id.createTripSaveButton);
        tripNameEditText = findViewById(R.id.createTripNameEditText);
        tripDestinationEditText = findViewById(R.id.createTripDestinationEditText);
        tripTypeRadioGroup = findViewById(R.id.createRadioGroup);
        tripRatingBar = findViewById(R.id.createRatingBar);
        tripPriceTextView = findViewById(R.id.tripPriceTextView);
        tripPricePicker = findViewById(R.id.createPriceSeekBar);
        startTripDate = findViewById(R.id.selectStartingDateEditText);
        endTripDate = findViewById(R.id.selectEndingDateEditText);
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
            bitmap = uriToBitmap(imageUri);
        }
    }

    private Bitmap uriToBitmap(Uri uri) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), uri);
                return ImageDecoder.decodeBitmap(source);
            } else {
                return MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;

        }
    }


    public void onClickPickStartDate(View view) {
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.getTime();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                startTripDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    public void onClickPickEndDate(View view) {
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.getTime();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                endTripDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }
}
