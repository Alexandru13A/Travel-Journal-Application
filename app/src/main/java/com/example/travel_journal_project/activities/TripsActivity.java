package com.example.travel_journal_project.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.travel_journal_project.R;
import com.example.travel_journal_project.models.Trip;
import com.example.travel_journal_project.recycleview.TripAdapter;
import com.example.travel_journal_project.viewmodel.TripViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TripsActivity extends AppCompatActivity {
    public static final int ADD_TRIP_REQUEST = 1;
    public static final int UPDATE_TRIP_REQUEST = 2;
    public static final int READ_TRIP_REQUEST = 3;
    private TripViewModel tripViewModel;
    private TripAdapter adapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);


         setRecyclerView();
        showAllTrips();
        addFabButton();
        readOrUpdateOnClick();
        deleteTripOnDrag();


    }

    public void deleteTripOnDrag() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                tripViewModel.deleteTrip(adapter.getTripAt(viewHolder.getAdapterPosition()));
                Toast.makeText(TripsActivity.this, "TRIP DELETED", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    public void setRecyclerView() {
        recyclerView = findViewById(R.id.trip_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(TripsActivity.this));
        recyclerView.setHasFixedSize(true);
        adapter = new TripAdapter();
        recyclerView.setAdapter(adapter);
    }

    public void showAllTrips() {
        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);
        tripViewModel.allTrips().observe(this, new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> trips) {
                adapter.setTrips(trips);
                Toast.makeText(TripsActivity.this, "On Change", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addFabButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(TripsActivity.this, AddTripActivity.class);
            startActivityForResult(intent, ADD_TRIP_REQUEST);
        });

    }

    public void readOrUpdateOnClick() {
        adapter.setTripClickListener(new TripAdapter.TripClickListener() {
            @Override
            public void onItemClick(Trip trip) {
                Intent intent = new Intent(TripsActivity.this, ReadTripActivity.class);
                intent.putExtra(ReadTripActivity.EXTRA_ID, trip.getTripId());
                intent.putExtra(ReadTripActivity.EXTRA_NAME, trip.getTripName());
                intent.putExtra(ReadTripActivity.EXTRA_DESTINATION, trip.getTripDestination());
                intent.putExtra(ReadTripActivity.EXTRA_TYPE, trip.getTripType());
                intent.putExtra(ReadTripActivity.EXTRA_PRICE, trip.getTripPrice());
                intent.putExtra(ReadTripActivity.EXTRA_START_DATE, trip.getTripStartDate());
                intent.putExtra(ReadTripActivity.EXTRA_END_DATE, trip.getTripEndDate());
                intent.putExtra(ReadTripActivity.EXTRA_RATING, trip.getTripRating());
                intent.putExtra(ReadTripActivity.EXTRA_FAVORITE, trip.isTripIsFavorite());
                startActivityForResult(intent, READ_TRIP_REQUEST);
            }

            @Override
            public void onLongItemClick(Trip trip) {
                Intent intent = new Intent(TripsActivity.this, UpdateTripActivity.class);
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

            Trip trip = new Trip(tripName, tripDestination, tripType, startingDate, endingDate, tripRating, tripPrice);
            tripViewModel.insert(trip);
            Toast.makeText(TripsActivity.this, "TRIP SAVED", Toast.LENGTH_SHORT).show();

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
