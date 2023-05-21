package com.example.travel_journal_project.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.travel_journal_project.models.Trip;

import java.util.List;


@Dao
public interface TripDao {

    @Query("UPDATE trip_table SET tripIsFavorite = :isFavorite  WHERE tripId = :tripId")
    void update(long tripId, boolean isFavorite);

    @Query("SELECT* FROM trip_table WHERE tripId = :id")
    LiveData<Trip> getFavoriteLiveData(long id);

    @Query("SELECT * FROM trip_table")
    LiveData<List<Trip>> getAllTrips();

    @Query("SELECT * FROM trip_table WHERE tripIsFavorite = 1")
    LiveData<List<Trip>> getFavoritesTrips();


    @Insert
    void insertTrip(Trip trip);

    @Update
    void updateTrip(Trip trip);

    @Delete
    void deleteTrip(Trip trip);


}
