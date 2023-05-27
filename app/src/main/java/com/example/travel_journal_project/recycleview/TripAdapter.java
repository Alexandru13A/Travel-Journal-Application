package com.example.travel_journal_project.recycleview;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_journal_project.R;
import com.example.travel_journal_project.models.Trip;
import com.example.travel_journal_project.repositories.TripRepository;
import com.example.travel_journal_project.viewmodel.TripViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripHolder> {

    private List<Trip> trips = new ArrayList<>();
    private TripClickListener listener;


    @NonNull
    @Override
    public TripHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_trips, parent, false);
        return new TripHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TripHolder holder, int position) {
        Trip currentTrip = trips.get(position);
        holder.tripNameTextView.setText(currentTrip.getTripName());
        holder.tripDestinationTextView.setText(currentTrip.getTripDestination());
        holder.tripPriceTextView.setText(String.valueOf(currentTrip.getTripPrice()) + " € ");
        holder.tripRatingValue.setText(String.valueOf(currentTrip.getTripRating()) + " ✪ ");
        String image = currentTrip.getTripPhotoUrl();

        if (image != null && !image.isEmpty()) {
            Picasso.get().load(new File(image)).into(holder.tripImageItem);
        } else {
            Picasso.get().load(R.drawable.imageholder).into(holder.tripImageItem);
        }


    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
        notifyDataSetChanged();
    }

    public Trip getTripAt(int position) {
        return trips.get(position);
    }


    class TripHolder extends RecyclerView.ViewHolder {
        private FloatingActionButton tripFavoriteButton;
        private ImageView tripImageItem;
        private TextView tripNameTextView;
        private TextView tripDestinationTextView;
        private TextView tripRatingValue;
        private TextView tripPriceTextView;

        public TripHolder(@NonNull View itemView) {
            super(itemView);

            tripImageItem = itemView.findViewById(R.id.tripItemImage);
            tripNameTextView = itemView.findViewById(R.id.tripNameTextViewItem);
            tripDestinationTextView = itemView.findViewById(R.id.tripDestinationTextViewItem);
            tripRatingValue = itemView.findViewById(R.id.tripRatingItem);
            tripPriceTextView = itemView.findViewById(R.id.tripPriceItem);
            tripFavoriteButton = itemView.findViewById(R.id.tripFavoriteButton);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(trips.get(position));
                }
            });
            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onLongItemClick(trips.get(position));
                }
                return true;
            });

        }
    }

    public interface TripClickListener {
        void onItemClick(Trip trip);

        void onLongItemClick(Trip trip);
    }

    public void setTripClickListener(TripClickListener listener) {
        this.listener = listener;
    }


}
