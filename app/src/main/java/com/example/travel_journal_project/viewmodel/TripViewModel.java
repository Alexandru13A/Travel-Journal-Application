package com.example.travel_journal_project.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.travel_journal_project.models.Trip;
import com.example.travel_journal_project.repositories.TripRepository;

import java.util.List;


public class TripViewModel extends AndroidViewModel {

    private TripRepository repository;
    private LiveData<List<Trip>> getAllTrips;



    public TripViewModel(@NonNull Application application) {
        super(application);
        repository = new TripRepository(application);
        getAllTrips = repository.getAllTrips();

    }

    @Insert
    public void insert(Trip trip) {
        repository.insertTrip(trip);

    }

    @Update
    public void updateTrip(Trip trip) {
        repository.updateTrip(trip);
    }

    @Delete
    public void deleteTrip(Trip trip) {
        repository.deleteTrip(trip);
    }

    public LiveData<List<Trip>> allTrips() {
        return getAllTrips;
    }



}
