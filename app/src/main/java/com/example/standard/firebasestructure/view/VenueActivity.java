package com.example.standard.firebasestructure.view;

import android.arch.lifecycle.*;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.*;

import com.example.standard.firebasestructure.*;
import com.example.standard.firebasestructure.R;
import com.example.standard.firebasestructure.model.*;
import com.example.standard.firebasestructure.model.adapters.VenueRecyclerAdapter;
import com.example.standard.firebasestructure.viewmodel.VenueViewModel;

import java.util.*;

public class VenueActivity extends AppCompatActivity {

    private RecyclerView recyclerViewVenues;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerManager;

    private VenueViewModel venueViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue);

        venueViewModel = ViewModelProviders.of(this).get(VenueViewModel.class);

        recyclerViewVenues = findViewById(R.id.recyclerVenues);
        recyclerViewVenues.setHasFixedSize(true);

        if(venueViewModel != null){
            LiveData<List<Venue>> venueLiveData = venueViewModel.getVenueLiveData();

            venueLiveData.observe(this, new Observer<List<Venue>>() {
                @Override
                public void onChanged(@Nullable List<Venue> venues) {
                    venues = Utils.sortVenuesByAttendees(venues);
                    recyclerManager = new LinearLayoutManager(VenueActivity.this);
                    recyclerViewVenues.setLayoutManager(recyclerManager);
                    recyclerAdapter = new VenueRecyclerAdapter(VenueActivity.this, venues, new OnItemClickListener() {
                        @Override
                        public void onItemClick(Venue venue) {
                            Intent intent = new Intent(VenueActivity.this, VenueDetailActivity.class);
                            intent.putExtra("venue", venue);
                            startActivity(intent);
                        }

                        @Override
                        public void onItemClick(User friend) {
                            //null
                        }

                        @Override
                        public void onItemClick(OutGoer outGoer) {
                            //null
                        }
                    });
                    recyclerViewVenues.setAdapter(recyclerAdapter);
                }
            });
        }
    }
}
