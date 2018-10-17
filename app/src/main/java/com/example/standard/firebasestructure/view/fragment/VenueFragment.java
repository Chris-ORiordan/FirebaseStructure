package com.example.standard.firebasestructure.view.fragment;


import android.arch.lifecycle.*;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.*;
import android.support.v7.widget.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.standard.firebasestructure.*;
import com.example.standard.firebasestructure.R;
import com.example.standard.firebasestructure.model.*;
import com.example.standard.firebasestructure.model.adapters.VenueRecyclerAdapter;
import com.example.standard.firebasestructure.view.*;
import com.example.standard.firebasestructure.viewmodel.VenueViewModel;

import java.util.List;

public class VenueFragment extends Fragment {

    private RecyclerView recyclerViewVenues;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerManager;

    private VenueViewModel venueViewModel;

    public VenueFragment() {
        // Required empty public constructor
    }

    public static VenueFragment newInstance(){
        return new VenueFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_venue, container, false);

        venueViewModel = ViewModelProviders.of(this).get(VenueViewModel.class);

        recyclerViewVenues = fragmentView.findViewById(R.id.recyclerVenues);
        recyclerViewVenues.setHasFixedSize(true);

        if(venueViewModel != null){
            LiveData<List<Venue>> venueLiveData = venueViewModel.getVenueLiveData();

            venueLiveData.observe(VenueFragment.this, new Observer<List<Venue>>() {
                @Override
                public void onChanged(@Nullable List<Venue> venues) {
                    venues = Utils.sortVenuesByAttendees(venues);
                    recyclerManager = new LinearLayoutManager(getContext());
                    recyclerViewVenues.setLayoutManager(recyclerManager);
                    recyclerAdapter = new VenueRecyclerAdapter(getContext(), venues, new OnItemClickListener() {
                        @Override
                        public void onItemClick(Venue venue) {
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("venue", venue);
                            VenueDetailFragment venueDetailFragment= VenueDetailFragment.newInstance();
                            venueDetailFragment.setArguments(bundle);
                            fragmentTransaction.replace(R.id.framelayoutContainer, venueDetailFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
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

        return fragmentView;
    }

}
