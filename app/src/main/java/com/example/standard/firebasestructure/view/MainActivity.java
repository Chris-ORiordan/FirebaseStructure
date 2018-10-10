package com.example.standard.firebasestructure.view;

import android.arch.lifecycle.*;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.example.standard.firebasestructure.R;
import com.example.standard.firebasestructure.model.*;
import com.example.standard.firebasestructure.model.adapters.*;
import com.example.standard.firebasestructure.viewmodel.*;
import com.google.firebase.database.*;

import java.util.*;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerUserGoingOut;
    private Spinner spinnerVenue;
    private Button buttonAdd;
    private Button buttonFeed;
    private Button buttonGoUpdate;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private UserAdapter userAdapter;
    private UserAdapter otherUserAdapter;
    private VenueAdapter venueAdapter;

    private UserViewModel userViewModel;
    private VenueViewModel venueViewModel;
    private OutGoerViewModel outGoerViewModel;

    private User selectedUser;
    private User otherSelectedUser;
    private Venue selectedVenue;

    final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerUserGoingOut = findViewById(R.id.spinnerUserGoingOut);
        spinnerVenue = findViewById(R.id.spinnerVenue);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonFeed = findViewById(R.id.buttonFeed);
        buttonGoUpdate = findViewById(R.id.buttonGoUpdate);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        venueViewModel = ViewModelProviders.of(this).get(VenueViewModel.class);
        outGoerViewModel = ViewModelProviders.of(this).get(OutGoerViewModel.class);

        //createUsersAndVenues();

        populateSpinners();

        addListeners();
    }

    private void addListeners() {
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //addFriendships();
                userViewModel.onUserGoingOut(selectedUser, selectedVenue);
                venueViewModel.onUserGoingOut(selectedUser, selectedVenue);
                outGoerViewModel.onUserGoingOut(selectedUser, selectedVenue);
            }
        });

        buttonFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FeedActivity.class);
                startActivity(intent);
            }
        });

        buttonGoUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
                startActivity(intent);
            }
        });

        spinnerUserGoingOut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedUser = userAdapter.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerVenue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedVenue = venueAdapter.getItem(i);
                //otherSelectedUser = otherUserAdapter.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void createUsersAndVenues() {
        //create users and add to firebase
        User user1 = new User(reference.push().getKey(), "Chris", null, null);
        User user2 = new User(reference.push().getKey(), "Jack", null, null);
        User user3 = new User(reference.push().getKey(), "Rich", null, null);
        User user4 = new User(reference.push().getKey(), "Cian", null, null);
        User user5 = new User(reference.push().getKey(), "Deasy", null, null);
        reference.child("Users").child(user1.getUserId()).setValue(user1);
        reference.child("Users").child(user2.getUserId()).setValue(user2);
        reference.child("Users").child(user3.getUserId()).setValue(user3);
        reference.child("Users").child(user4.getUserId()).setValue(user4);
        reference.child("Users").child(user5.getUserId()).setValue(user5);

        //Create Venues and add to firebase
        Venue venue1 = new Venue(reference.push().getKey(), "Xico", null);
        Venue venue2 = new Venue(reference.push().getKey(), "Diceys", null);
        Venue venue3 = new Venue(reference.push().getKey(), "Coppers", null);
        Venue venue4 = new Venue(reference.push().getKey(), "Workmans", null);
        Venue venue5 = new Venue(reference.push().getKey(), "Opium", null);
        reference.child("Venues").child(venue1.getVenueId()).setValue(venue1);
        reference.child("Venues").child(venue2.getVenueId()).setValue(venue2);
        reference.child("Venues").child(venue3.getVenueId()).setValue(venue3);
        reference.child("Venues").child(venue4.getVenueId()).setValue(venue4);
        reference.child("Venues").child(venue5.getVenueId()).setValue(venue5);
    }

    private void populateSpinners() {

        if(userViewModel != null){
            LiveData<List<User>> userLiveData = userViewModel.getUserLiveData();

            userLiveData.observe(this, new Observer<List<User>>() {
                @Override
                public void onChanged(@Nullable List<User> users) {
                    userAdapter = new UserAdapter(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, users);
                    userAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    spinnerUserGoingOut.setAdapter(userAdapter);
//                    otherUserAdapter = new UserAdapter(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, users);
//                    otherUserAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//                    spinnerVenue.setAdapter(otherUserAdapter);
                }
            });
        }

        if(venueViewModel != null){
            LiveData<List<Venue>> venueLiveData = venueViewModel.getVenueLiveData();

            venueLiveData.observe(this, new Observer<List<Venue>>() {
                @Override
                public void onChanged(@Nullable List<Venue> venues) {
                    venueAdapter = new VenueAdapter(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, venues);
                    venueAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    spinnerVenue.setAdapter(venueAdapter);
                }
            });
        }
    }

//    private void onUserGoingOut(){
//        //add to user destinations
//        reference.child("Users").child(selectedUser.getUserId())
//                .child("destinations").child(selectedVenue.getVenueId()).setValue(true);
//
//        //add to venue attendees
//        reference.child("Venues").child(selectedVenue.getVenueId())
//                .child("attendees").child(selectedUser.getUserId()).setValue(true);
//
//        //add to friends going out
//        //final String key = reference.push().getKey();
//        final OutGoer outGoer = new OutGoer(selectedUser.getUserId(), selectedUser.getUserName(),
//                selectedVenue.getVenueId(), selectedVenue.getVenueName(), System.currentTimeMillis());
//        reference.child("Users").child(selectedUser.getUserId()).child("friends").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot dsp: dataSnapshot.getChildren()){
//                    String friendId = dsp.getKey();
//                    reference.child("OutGoer").child(friendId).child(selectedUser.getUserId()).child(selectedVenue.getVenueId()).setValue(outGoer);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e(TAG, databaseError.toString());
//            }
//        });
//    }

    private void addFriendships(){
        reference.child("Users").child(selectedUser.getUserId())
                .child("friends").child(otherSelectedUser.getUserId()).setValue(true);

        reference.child("Users").child(otherSelectedUser.getUserId())
                .child("friends").child(selectedUser.getUserId()).setValue(true);
    }
}
