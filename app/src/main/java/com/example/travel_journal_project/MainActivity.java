package com.example.travel_journal_project;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.travel_journal_project.activities.AddTripActivity;
import com.example.travel_journal_project.activities.ReadTripActivity;
import com.example.travel_journal_project.activities.UpdateTripActivity;
import com.example.travel_journal_project.models.Trip;
import com.example.travel_journal_project.recycleview.TripAdapter;
import com.example.travel_journal_project.viewmodel.TripViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_TRIP_REQUEST = 1;
    public static final int UPDATE_TRIP_REQUEST = 2;
    public static final int READ_TRIP_REQUEST = 3;
    private TripViewModel tripViewModel;
    private ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.trip_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        TripAdapter adapter = new TripAdapter();
        recyclerView.setAdapter(adapter);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Journeys To Remember ");
        }

        FloatingActionButton fab = findViewById(R.id.fab_add_trip);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTripActivity.class);
            startActivityForResult(intent, ADD_TRIP_REQUEST);
        });

        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);
        tripViewModel.allTrips().observe(this, new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> trips) {
                adapter.setTrips(trips);
                Toast.makeText(MainActivity.this, "On Change", Toast.LENGTH_SHORT).show();
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                tripViewModel.deleteTrip(adapter.getTripAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "TRIP DELETED", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setTripClickListener(new TripAdapter.TripClickListener() {
            @Override
            public void onItemClick(Trip trip) {
                Intent intent = new Intent(MainActivity.this, ReadTripActivity.class);
                intent.putExtra(UpdateTripActivity.EXTRA_ID, trip.getTripId());
                intent.putExtra(UpdateTripActivity.EXTRA_NAME, trip.getTripName());
                intent.putExtra(UpdateTripActivity.EXTRA_DESTINATION, trip.getTripDestination());
                intent.putExtra(UpdateTripActivity.EXTRA_TYPE, trip.getTripType());
                intent.putExtra(UpdateTripActivity.EXTRA_PRICE, trip.getTripPrice());
                intent.putExtra(UpdateTripActivity.EXTRA_START_DATE, trip.getTripStartDate());
                intent.putExtra(UpdateTripActivity.EXTRA_END_DATE, trip.getTripEndDate());
                intent.putExtra(UpdateTripActivity.EXTRA_RATING, trip.getTripRating());
                startActivityForResult(intent, READ_TRIP_REQUEST);
            }

            @Override
            public void onLongItemClick(Trip trip) {
                Intent intent = new Intent(MainActivity.this, UpdateTripActivity.class);
                intent.putExtra(UpdateTripActivity.EXTRA_ID, trip.getTripId());
                intent.putExtra(UpdateTripActivity.EXTRA_NAME, trip.getTripName());
                intent.putExtra(UpdateTripActivity.EXTRA_DESTINATION, trip.getTripDestination());
                intent.putExtra(UpdateTripActivity.EXTRA_TYPE, trip.getTripType());
                intent.putExtra(UpdateTripActivity.EXTRA_PRICE, trip.getTripPrice());
                intent.putExtra(UpdateTripActivity.EXTRA_START_DATE, trip.getTripStartDate());
                intent.putExtra(UpdateTripActivity.EXTRA_END_DATE, trip.getTripEndDate());
                intent.putExtra(UpdateTripActivity.EXTRA_RATING, trip.getTripRating());
                startActivityForResult(intent, UPDATE_TRIP_REQUEST);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_TRIP_REQUEST && resultCode == RESULT_OK) {

            String tripName = data.getStringExtra(AddTripActivity.EXTRA_NAME);
            String tripDestination = data.getStringExtra(AddTripActivity.EXTRA_DESTINATION);
            String tripType = data.getStringExtra(AddTripActivity.EXTRA_TYPE);
            int tripPrice = data.getIntExtra(AddTripActivity.EXTRA_PRICE, 0);
            String startingDate = data.getStringExtra(AddTripActivity.EXTRA_START_DATE);
            String endingDate = data.getStringExtra(AddTripActivity.EXTRA_END_DATE);
            float tripRating = data.getFloatExtra(AddTripActivity.EXTRA_RATING, 0);
            // boolean isFavorite = data.getBooleanExtra(AddTripActivity.EXTRA_FAVORITE, false);

            Trip trip = new Trip(tripName, tripDestination, tripType, startingDate, endingDate, tripRating, tripPrice);
            tripViewModel.insert(trip);
            Toast.makeText(MainActivity.this, "TRIP SAVED", Toast.LENGTH_SHORT).show();

        } else if (requestCode == UPDATE_TRIP_REQUEST && resultCode == RESULT_OK) {
            long id = data.getLongExtra(UpdateTripActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "NOTE CAN'T BE UPDATED", Toast.LENGTH_SHORT).show();
                return;
            }

            String tripName = data.getStringExtra(AddTripActivity.EXTRA_NAME);
            String tripDestination = data.getStringExtra(AddTripActivity.EXTRA_DESTINATION);
            String tripType = data.getStringExtra(AddTripActivity.EXTRA_TYPE);
            int tripPrice = data.getIntExtra(AddTripActivity.EXTRA_PRICE, 0);
            String startingDate = data.getStringExtra(AddTripActivity.EXTRA_START_DATE);
            String endingDate = data.getStringExtra(AddTripActivity.EXTRA_END_DATE);
            float tripRating = data.getFloatExtra(AddTripActivity.EXTRA_RATING, 0);
            Trip trip = new Trip(tripName, tripDestination, tripType, startingDate, endingDate, tripRating, tripPrice);
            trip.setTripId(id);
            tripViewModel.updateTrip(trip);

            Toast.makeText(this, "TRIP UPDATED", Toast.LENGTH_SHORT).show();
        }

    }
}