package com.example.standard.firebasestructure.model.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.*;

import com.example.standard.firebasestructure.*;
import com.example.standard.firebasestructure.model.Venue;
import com.example.standard.firebasestructure.view.*;

import java.util.*;

public class ProfileDestinationRecyclerAdapter extends RecyclerView.Adapter<ProfileDestinationRecyclerAdapter.VenueViewHolder> {

    private List<Venue> venueList;
    private LayoutInflater inflater;
    private final OnItemClickListener listener;
    private Context context;

    public static class VenueViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewDestinationName;
        public ToggleButton tglGoing;

        public VenueViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDestinationName = itemView.findViewById(R.id.destinationName);
            tglGoing = itemView.findViewById(R.id.tglGoing);
        }

        public void bind(Venue venue, OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(venue);
                }
            });
        }
    }

    Venue getItem(int id){
        return venueList.get(id);
    }

    public ProfileDestinationRecyclerAdapter(Context context, List<Venue> venueList, OnItemClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.venueList = venueList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProfileDestinationRecyclerAdapter.VenueViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.view_profile_venue, viewGroup, false);
        return new VenueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VenueViewHolder venueViewHolder, int i) {
        venueViewHolder.bind(venueList.get(i), listener);
        venueViewHolder.textViewDestinationName.setText(venueList.get(i).getVenueName());
    }

    @Override
    public int getItemCount() {
        if(venueList != null){
            return venueList.size();
        } else {
            return 0;
        }
    }
}

