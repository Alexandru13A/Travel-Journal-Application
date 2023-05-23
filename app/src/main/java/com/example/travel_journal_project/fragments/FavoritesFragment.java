package com.example.travel_journal_project.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.travel_journal_project.R;
import com.example.travel_journal_project.activities.ReadTripActivity;
import com.example.travel_journal_project.activities.UpdateTripActivity;
import com.example.travel_journal_project.models.Trip;
import com.example.travel_journal_project.recycleview.FavoritesAdapter;
import com.example.travel_journal_project.recycleview.TripAdapter;
import com.example.travel_journal_project.viewmodel.TripViewModel;

import java.util.List;


public class FavoritesFragment extends Fragment {

    private TripViewModel tripViewModel;
    private FavoritesAdapter adapter;
    private RecyclerView recyclerView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        return inflater.inflate(R.layout.favorites_recycle_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.favorites_recycle_view);

        setRecyclerView();
        getAllFavoritesTrips();
        deleteTripOnDrag();
        readOnClick();

    }

    public void setRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter = new FavoritesAdapter();
        recyclerView.setAdapter(adapter);
    }

    public void getAllFavoritesTrips() {
        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);
        tripViewModel.allFavoritesTrips().observe(getViewLifecycleOwner(), new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> trips) {
                adapter.setTrips(trips);
            }
        });
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
                Toast.makeText(getActivity(), "TRIP DELETED", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    public void readOnClick() {
        adapter.favTripClickListener(trip -> {
            Intent intent = new Intent(getContext(), ReadTripActivity.class);
            intent.putExtra(ReadTripActivity.EXTRA_ID, trip.getTripId());
            intent.putExtra(ReadTripActivity.EXTRA_NAME, trip.getTripName());
            intent.putExtra(ReadTripActivity.EXTRA_DESTINATION, trip.getTripDestination());
            intent.putExtra(ReadTripActivity.EXTRA_TYPE, trip.getTripType());
            intent.putExtra(ReadTripActivity.EXTRA_PRICE, trip.getTripPrice());
            intent.putExtra(ReadTripActivity.EXTRA_START_DATE, trip.getTripStartDate());
            intent.putExtra(ReadTripActivity.EXTRA_END_DATE, trip.getTripEndDate());
            intent.putExtra(ReadTripActivity.EXTRA_RATING, trip.getTripRating());
            intent.putExtra(ReadTripActivity.EXTRA_FAVORITE, trip.isTripIsFavorite());
            startActivity(intent);
        });

    }

}