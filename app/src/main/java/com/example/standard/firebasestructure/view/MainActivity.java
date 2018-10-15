package com.example.standard.firebasestructure.view;

import android.arch.lifecycle.*;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.example.standard.firebasestructure.R;
import com.example.standard.firebasestructure.model.*;
import com.example.standard.firebasestructure.model.adapters.*;
import com.example.standard.firebasestructure.viewmodel.*;
import com.google.firebase.database.*;

import java.util.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Spinner spinnerUser1;
    private Spinner spinnerUser2;
    private Button buttonAdd;
    private Button buttonFeed;
    private Button buttonFriend;
    private Button buttonVenue;
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

        spinnerUser1 = findViewById(R.id.spinnerUser1);
        spinnerUser2 = findViewById(R.id.spinnerUser2);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonFeed = findViewById(R.id.buttonFeed);
        buttonFriend = findViewById(R.id.buttonFriend);
        buttonVenue = findViewById(R.id.buttonVenue);
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

    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()){
            case R.id.buttonAdd:
                addFriendships();
//                userViewModel.onUserGoingOut(selectedUser, selectedVenue);
//                venueViewModel.onUserGoingOut(selectedUser, selectedVenue);
//                outGoerViewModel.onUserGoingOut(selectedUser, selectedVenue);
                break;
            case R.id.buttonFeed:
                intent = new Intent(MainActivity.this, FeedActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonFriend:
                intent = new Intent(MainActivity.this, FriendActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonVenue:
                intent = new Intent(MainActivity.this, VenueActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonGoUpdate:
                intent = new Intent(MainActivity.this, UpdateActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void addListeners() {

        buttonAdd.setOnClickListener(this);
        buttonFeed.setOnClickListener(this);
        buttonFriend.setOnClickListener(this);
        buttonVenue.setOnClickListener(this);
        buttonGoUpdate.setOnClickListener(this);

        spinnerUser1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedUser = userAdapter.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerUser2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //selectedVenue = venueAdapter.getItem(i);
                otherSelectedUser = otherUserAdapter.getItem(i);
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
        userViewModel.createUser(user1);
        userViewModel.createUser(user2);
        userViewModel.createUser(user3);
        userViewModel.createUser(user4);
        userViewModel.createUser(user5);

        //Create Venues and add to firebase
        Venue venue1 = new Venue(reference.push().getKey(), "Xico", null);
        Venue venue2 = new Venue(reference.push().getKey(), "Diceys", null);
        Venue venue3 = new Venue(reference.push().getKey(), "Coppers", null);
        Venue venue4 = new Venue(reference.push().getKey(), "Workmans", null);
        Venue venue5 = new Venue(reference.push().getKey(), "Opium", null);
        venueViewModel.createVenue(venue1);
        venueViewModel.createVenue(venue2);
        venueViewModel.createVenue(venue3);
        venueViewModel.createVenue(venue4);
        venueViewModel.createVenue(venue5);
    }

    private void populateSpinners() {

        if(userViewModel != null){
            LiveData<List<User>> userLiveData = userViewModel.getUserLiveData();

            userLiveData.observe(this, new Observer<List<User>>() {
                @Override
                public void onChanged(@Nullable List<User> users) {
                    userAdapter = new UserAdapter(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, users);
                    userAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    spinnerUser1.setAdapter(userAdapter);
                    otherUserAdapter = new UserAdapter(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, users);
                    otherUserAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    spinnerUser2.setAdapter(otherUserAdapter);
                }
            });
        }

//        if(venueViewModel != null){
//            LiveData<List<Venue>> venueLiveData = venueViewModel.getVenueLiveData();
//
//            venueLiveData.observe(this, new Observer<List<Venue>>() {
//                @Override
//                public void onChanged(@Nullable List<Venue> venues) {
//                    venueAdapter = new VenueAdapter(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, venues);
//                    venueAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//                    spinnerVenue.setAdapter(venueAdapter);
//                }
//            });
//        }
    }

    private void addFriendships(){
        userViewModel.createFriendship(selectedUser, otherSelectedUser);
    }
}
