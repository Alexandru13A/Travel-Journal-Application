package com.example.travel_journal_project.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.travel_journal_project.models.DateConverter;
import com.example.travel_journal_project.models.Trip;

@Database(entities = {Trip.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public static AppDatabase instance;


    public abstract TripDao tripDao();


    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "travel_journal")
                    .fallbackToDestructiveMigration().build();
        }

        return instance;
    }

}
