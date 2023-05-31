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
import com.example.travel_journal_project.recycleview.TripAdapter;
import com.example.travel_journal_project.viewmodel.TripViewModel;

import java.util.List;


public class TripsFragment extends Fragment {


    private TripViewModel tripViewModel;
    private TripAdapter adapter;

    private RecyclerView recyclerView;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.trips_recycle_view, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.trip_recycle_view);
        setRecyclerView();
        getAllTrips();
        readOrUpdateOnClick();
        deleteTripOnDrag();

    }

    public void setRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        adapter = new TripAdapter();
        recyclerView.setAdapter(adapter);
    }

    public void getAllTrips() {
        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);
        tripViewModel.allTrips().observe(getViewLifecycleOwner(), new Observer<List<Trip>>() {
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

    public void readOrUpdateOnClick() {
        adapter.setTripClickListener(new TripAdapter.TripClickListener() {
            @Override
            public void onItemClick(Trip trip) {
                Intent intent = new Intent(getContext(), ReadTripActivity.class);
                intent.putExtra(ReadTripActivity.EXTRA_ID, trip.getTripId());
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(Trip trip) {
                Intent intent = new Intent(getContext(), UpdateTripActivity.class);
                intent.putExtra(UpdateTripActivity.EXTRA_ID, trip.getTripId());
                startActivity(intent);
            }
        });

    }

}