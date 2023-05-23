package com.example.travel_journal_project.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

@Entity(tableName = "trip_table")
public class Trip {
    @PrimaryKey(autoGenerate = true)
    private long tripId;


    private String tripName;
    private String tripDestination;
    private String tripType;

    private String tripStartDate;
    private String tripEndDate;
    private float tripRating;
    private int tripPrice;
    private boolean tripIsFavorite = false;

    private String tripPhotoUrl = "";

    @Ignore
    public Trip() {
    }

    @Ignore
    public Trip(String tripName, String tripDestination, String tripType, String tripStartDate, String tripEndDate, float tripRating, int tripPrice) {
        this.tripName = tripName;
        this.tripDestination = tripDestination;
        this.tripType = tripType;
        this.tripStartDate = tripStartDate;
        this.tripEndDate = tripEndDate;
        this.tripRating = tripRating;
        this.tripPrice = tripPrice;

    }


    public Trip(String tripName, String tripDestination, String tripType, String tripStartDate, String tripEndDate, float tripRating, int tripPrice, boolean tripIsFavorite, String tripPhotoUrl) {
        this.tripName = tripName;
        this.tripDestination = tripDestination;
        this.tripType = tripType;
        this.tripStartDate = tripStartDate;
        this.tripEndDate = tripEndDate;
        this.tripRating = tripRating;
        this.tripPrice = tripPrice;
        this.tripIsFavorite = tripIsFavorite;
        this.tripPhotoUrl = tripPhotoUrl;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getTripDestination() {
        return tripDestination;
    }

    public void setTripDestination(String tripDestination) {
        this.tripDestination = tripDestination;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public String getTripStartDate() {
        return tripStartDate;
    }

    public void setTripStartDate(String tripStartDate) {
        this.tripStartDate = tripStartDate;
    }

    public String getTripEndDate() {
        return tripEndDate;
    }

    public void setTripEndDate(String tripEndDate) {
        this.tripEndDate = tripEndDate;
    }

    public float getTripRating() {
        return tripRating;
    }

    public void setTripRating(float tripRating) {
        this.tripRating = tripRating;
    }

    public int getTripPrice() {
        return tripPrice;
    }

    public void setTripPrice(int tripPrice) {
        this.tripPrice = tripPrice;
    }

    public boolean isTripIsFavorite() {
        return tripIsFavorite;
    }

    public void setTripIsFavorite(boolean tripIsFavorite) {
        this.tripIsFavorite = tripIsFavorite;
    }

    public String getTripPhotoUrl() {
        return tripPhotoUrl;
    }

    public void setTripPhotoUrl(String tripPhotoUrl) {
        this.tripPhotoUrl = tripPhotoUrl;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "tripName='" + tripName + '\'' +
                ", tripDestination='" + tripDestination + '\'' +
                ", tripType='" + tripType + '\'' +
                ", tripStartDate='" + tripStartDate + '\'' +
                ", tripEndDate='" + tripEndDate + '\'' +
                ", tripRating=" + tripRating +
                ", tripPrice=" + tripPrice +
                ", tripIsFavorite=" + tripIsFavorite +
                ", tripPhotoUrl='" + tripPhotoUrl + '\'' +
                '}';
    }
}
