package com.example.travel_journal_project.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.travel_journal_project.database.AppDatabase;
import com.example.travel_journal_project.database.MyExecutor;
import com.example.travel_journal_project.database.TripDao;
import com.example.travel_journal_project.models.Trip;

import java.util.List;

public class TripRepository {

    private TripDao tripDao;
    private LiveData<List<Trip>> allTrips;
    private MyExecutor executor;

    public TripRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        tripDao = database.tripDao();
        executor = new MyExecutor();
        allTrips = tripDao.getAllTrips();
    }

    public LiveData<List<Trip>> getAllTrips() {
        return allTrips;
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




}
