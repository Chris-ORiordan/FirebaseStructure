package com.example.standard.firebasestructure.view;

import android.arch.lifecycle.*;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.*;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import com.example.standard.firebasestructure.R;
import com.example.standard.firebasestructure.model.*;
import com.example.standard.firebasestructure.model.adapters.*;
import com.example.standard.firebasestructure.view.fragment.*;
import com.example.standard.firebasestructure.viewmodel.*;
import com.google.firebase.database.*;

import java.util.*;

public class MainActivity extends AppCompatActivity{

    private FrameLayout frameLayoutContainer;
    private BottomNavigationView bottomNavigationView;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    private UserViewModel userViewModel;
    private VenueViewModel venueViewModel;

    final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameLayoutContainer = findViewById(R.id.framelayoutContainer);
        bottomNavigationView = findViewById(R.id.navigationBar);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        venueViewModel = ViewModelProviders.of(this).get(VenueViewModel.class);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;
                switch (menuItem.getItemId()){
                    case R.id.feedTab :
                        selectedFragment = FeedFragment.newInstance();
                        break;
                    case R.id.venuesTab :
                        selectedFragment = VenueFragment.newInstance();
                        break;
                    case R.id.friendsTab :
                        selectedFragment = FriendFragment.newInstance();
                        break;
                    case R.id.goingOutTab :
                        selectedFragment = GoingOutFragment.newInstance();
                        break;
                    case R.id.updateTab :
                        selectedFragment = UpdateFragment.newInstance();
                        break;
                }
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(frameLayoutContainer.getId(), selectedFragment);
                fragmentTransaction.commit();
                return true;
            }
        });

        //Manually displaying the first fragment - one time only
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayoutContainer, FeedFragment.newInstance());
        fragmentTransaction.commit();

        //createUsersAndVenues();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;
                switch (menuItem.getItemId()){
                    case R.id.feedTab :
                        selectedFragment = FeedFragment.newInstance();
                        break;
                    case R.id.venuesTab :
                        selectedFragment = VenueFragment.newInstance();
                        break;
                    case R.id.friendsTab :
                        selectedFragment = FriendFragment.newInstance();
                        break;
                    case R.id.goingOutTab :
                        selectedFragment = GoingOutFragment.newInstance();
                        break;
                    case R.id.updateTab :
                        selectedFragment = UpdateFragment.newInstance();
                        break;
                }
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(frameLayoutContainer.getId(), selectedFragment);
                fragmentTransaction.commit();
                return true;
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
}
