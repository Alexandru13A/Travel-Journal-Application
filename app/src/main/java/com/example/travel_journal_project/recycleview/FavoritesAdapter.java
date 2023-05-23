package com.example.travel_journal_project.recycleview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_journal_project.R;
import com.example.travel_journal_project.models.Trip;

import java.util.ArrayList;
import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesHolder> {

    private List<Trip> favTrips = new ArrayList<>();
    private TripClickListener listener;

    @NonNull
    @Override
    public FavoritesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_favorites, parent, false);
        return new FavoritesHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesHolder holder, int position) {
        Trip currentTrip = favTrips.get(position);
        holder.tripNameTextView.setText(currentTrip.getTripName());
        holder.tripDestinationTextView.setText(currentTrip.getTripDestination());
        holder.tripPriceTextView.setText(String.valueOf(currentTrip.getTripPrice()) + " € ");
        holder.tripRatingValue.setText(String.valueOf(currentTrip.getTripRating()) + " ✪ ");


    }

    @Override
    public int getItemCount() {
        return favTrips.size();
    }

    public void setTrips(List<Trip> favTrips) {
        this.favTrips = favTrips;
        notifyDataSetChanged();
    }

    public Trip getTripAt(int position) {
        return favTrips.get(position);
    }



    class FavoritesHolder extends RecyclerView.ViewHolder {

        private ImageView tripImageItem;
        private TextView tripNameTextView;
        private TextView tripDestinationTextView;
        private TextView tripRatingValue;
        private TextView tripPriceTextView;
        private ImageView isFavImage;

        public FavoritesHolder(@NonNull View itemView) {
            super(itemView);
            tripImageItem = itemView.findViewById(R.id.tripItemImage);
            tripNameTextView = itemView.findViewById(R.id.tripNameTextViewItem);
            tripDestinationTextView = itemView.findViewById(R.id.tripDestinationTextViewItem);
            tripRatingValue = itemView.findViewById(R.id.tripRatingItem);
            tripPriceTextView = itemView.findViewById(R.id.tripPriceItem);
            isFavImage = itemView.findViewById(R.id.isFavImage);


            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(favTrips.get(position));
                }
            });

        }
    }

    public interface TripClickListener {
        void onItemClick(Trip trip);

    }

    public void favTripClickListener(TripClickListener listener) {
        this.listener = listener;
    }

}
