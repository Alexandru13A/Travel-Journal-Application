package com.example.travel_journal_project.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.travel_journal_project.R;
import com.example.travel_journal_project.models.Trip;
import com.example.travel_journal_project.recycleview.FavoritesAdapter;
import com.example.travel_journal_project.viewmodel.TripViewModel;

import java.util.List;

public class FavoritesTripsActivity extends AppCompatActivity {

    private TripViewModel tripViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_trips);
        RecyclerView recyclerView = findViewById(R.id.favorites_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        FavoritesAdapter adapter = new FavoritesAdapter();
        recyclerView.setAdapter(adapter);

        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);
        tripViewModel.allFavoritesTrips().observe(this, new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> trips) {
                adapter.setTrips(trips);
            }
        });

        adapter.favTripClickListener(new FavoritesAdapter.TripClickListener() {

            @Override
            public void onItemClick(Trip trip) {
                Intent intent = new Intent(FavoritesTripsActivity.this, ReadTripActivity.class);
                intent.putExtra(ReadTripActivity.EXTRA_ID, trip.getTripId());
                intent.putExtra(ReadTripActivity.EXTRA_NAME, trip.getTripName());
                intent.putExtra(ReadTripActivity.EXTRA_DESTINATION, trip.getTripDestination());
                intent.putExtra(ReadTripActivity.EXTRA_TYPE, trip.getTripType());
                intent.putExtra(ReadTripActivity.EXTRA_PRICE, trip.getTripPrice());
                intent.putExtra(ReadTripActivity.EXTRA_START_DATE, trip.getTripStartDate());
                intent.putExtra(ReadTripActivity.EXTRA_END_DATE, trip.getTripEndDate());
                intent.putExtra(ReadTripActivity.EXTRA_RATING, trip.getTripRating());
                intent.putExtra(ReadTripActivity.EXTRA_FAVORITE, trip.isTripIsFavorite());
                startActivityForResult(intent, TripsActivity.READ_TRIP_REQUEST);
            }
        });
    }
}