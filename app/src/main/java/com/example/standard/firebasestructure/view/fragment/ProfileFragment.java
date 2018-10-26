package com.example.standard.firebasestructure.view.fragment;


import android.app.Activity;
import android.arch.lifecycle.*;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.*;
import android.support.v4.app.Fragment;
import android.support.v7.widget.*;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.example.standard.firebasestructure.*;
import com.example.standard.firebasestructure.R;
import com.example.standard.firebasestructure.model.*;
import com.example.standard.firebasestructure.model.adapters.*;
import com.example.standard.firebasestructure.view.*;
import com.example.standard.firebasestructure.viewmodel.*;
import com.google.android.gms.tasks.*;
import com.google.firebase.storage.*;
import com.squareup.haha.perflib.Main;

import java.util.*;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private EditText editTextNewName;
    private RecyclerView recyclerViewDestinations;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerLayoutManager;
    private Button buttonUpdate;
    private Button buttonUpload;

    private UserViewModel userViewModel;
    private VenueViewModel venueViewModel;
    private OutGoerViewModel outGoerViewModel;

    private StorageReference storageReference;
    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(){
        return new ProfileFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_profile, container, false);

        storageReference = FirebaseStorage.getInstance().getReference();

        ((MainActivity) getActivity()).setActionBarTitle(R.string.update);
        ((MainActivity) getActivity()).setDisplayHomeAsUpEnabled(false);

        editTextNewName = fragmentView.findViewById(R.id.editTextNewName);
        recyclerViewDestinations = fragmentView.findViewById(R.id.recyclerDestinations);
        recyclerViewDestinations.setHasFixedSize(true);
        buttonUpdate = fragmentView.findViewById(R.id.buttonUpdate);
        buttonUpload = fragmentView.findViewById(R.id.buttonUpload);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        venueViewModel = ViewModelProviders.of(this).get(VenueViewModel.class);
        outGoerViewModel = ViewModelProviders.of(this).get(OutGoerViewModel.class);

        editTextNewName.setText(MainApplication.getCurrentUser().getUserName());

        if(venueViewModel != null){
            LiveData<List<Venue>>destinationVenues = venueViewModel.getDestinationVenueLiveData();

            destinationVenues.observe(this, new Observer<List<Venue>>() {
                @Override
                public void onChanged(@Nullable List<Venue> destinations) {
                    recyclerLayoutManager = new LinearLayoutManager(getContext());
                    recyclerViewDestinations.setLayoutManager(recyclerLayoutManager);
                    recyclerAdapter = new ProfileDestinationRecyclerAdapter(getContext(), destinations, new OnItemClickListener() {
                        @Override
                        public void onItemClick(Venue venue) {

                        }

                        @Override
                        public void onItemClick(User friend) {

                        }

                        @Override
                        public void onItemClick(OutGoer outGoer) {

                        }
                    });
                    recyclerViewDestinations.setAdapter(recyclerAdapter);
                }
            });
        }

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextNewName.getText().toString().length() != 0 && editTextNewName.getText() != null){
                    Map<String, Object> newNameMap = new HashMap<>();
                    newNameMap.put("userName", editTextNewName.getText().toString());
                    userViewModel.updateUser(MainApplication.getCurrentUser(), newNameMap);
                    outGoerViewModel.updateOutGoerUser(MainApplication.getCurrentUser(), newNameMap);
                }
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                getActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.REQUEST_GALLERY);
            }
        });

        return fragmentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        userViewModel.updatePhoto(requestCode, resultCode, data);
    }
}
