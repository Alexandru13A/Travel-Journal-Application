package com.example.travel_journal_project.viewmodel;

import android.app.Application;
import android.widget.Toast;

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
    private LiveData<List<Trip>> getFavoritesTrips;

    private LiveData<Trip> favoriteLiveData;


    public TripViewModel(@NonNull Application application) {
        super(application);
        repository = new TripRepository(application);
        getAllTrips = repository.getAllTrips();
        getFavoritesTrips = repository.getAllFavoritesTrips();

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

    public LiveData<List<Trip>> allFavoritesTrips() {
        return getFavoritesTrips;
    }


    public void addToFavorite(long id, boolean value) {
        repository.addToFavorite(id, value);
    }

    public Trip getTripById(long id) {
        return repository.getTripById(id);
    }


}
