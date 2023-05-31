package com.example.travel_journal_project.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.travel_journal_project.database.AppDatabase;
import com.example.travel_journal_project.helpers.MyExecutor;
import com.example.travel_journal_project.database.TripDao;
import com.example.travel_journal_project.models.Trip;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class TripRepository {

    private TripDao tripDao;
    private LiveData<List<Trip>> allTrips;

    private LiveData<List<Trip>> allFavoritesTrips;

    private MyExecutor executor;

    public TripRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        tripDao = database.tripDao();
        executor = new MyExecutor();
        allTrips = tripDao.getAllTrips();
        allFavoritesTrips = tripDao.getFavoritesTrips();

    }

    public LiveData<List<Trip>> getAllTrips() {
        return allTrips;
    }

    public LiveData<List<Trip>> getAllFavoritesTrips() {
        return allFavoritesTrips;
    }

    public void insertTrip(Trip trip) {
        executor.execute(() -> {
            tripDao.insertTrip(trip);
        });
    }

    public void updateTrip(Trip trip) {
        executor.execute(() -> {
            tripDao.updateTrip(trip);
        });
    }

    public void deleteTrip(Trip trip) {
        executor.execute(() -> {
            tripDao.deleteTrip(trip);
        });
    }


    public void addToFavorite(long id, boolean value) {
        executor.execute(() -> {
            tripDao.update(id, value);
        });
    }

    public Trip getTripById(long tripId) {
        return tripDao.getTripById(tripId);

    }


}



