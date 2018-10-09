package com.example.standard.firebasestructure.viewmodel;

import android.arch.core.util.Function;
import android.arch.lifecycle.*;
import android.support.annotation.NonNull;

import com.example.standard.firebasestructure.network.FirebaseQueryLiveData;
import com.example.standard.firebasestructure.model.Venue;
import com.google.firebase.database.*;

import java.util.*;

public class VenueViewModel extends ViewModel {
    private static final DatabaseReference VENUE_REF = FirebaseDatabase.getInstance().getReference().child("Venues");

    private List<Venue> venueList = new ArrayList<>();

    @NonNull
    public LiveData<List<Venue>> getVenueLiveData(){
        FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(VENUE_REF);
        LiveData<List<Venue>> venuesLiveData = Transformations.map(liveData, new Deserialiser());
        return venuesLiveData;
    }

    private class Deserialiser implements Function<DataSnapshot, List<Venue>> {
        @Override
        public List<Venue> apply(DataSnapshot dataSnapshot) {
            venueList.clear();
            for(DataSnapshot dsp: dataSnapshot.getChildren()){
                Venue venue = dsp.getValue(Venue.class);
                venueList.add(venue);
            }
            return venueList;
        }
    }
}
