package com.example.standard.firebasestructure.view;

import android.arch.lifecycle.*;
import android.arch.lifecycle.Observer;
import android.support.annotation.*;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.example.standard.firebasestructure.R;
import com.example.standard.firebasestructure.model.*;
import com.example.standard.firebasestructure.model.adapters.UserAdapter;
import com.example.standard.firebasestructure.viewmodel.*;
import com.google.firebase.database.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class  ViewActivity extends AppCompatActivity {

    private TextView textViewOutput;
    private Spinner spinnerWhoAmI;
    private FloatingActionButton fabChangeDetails;
    private EditText editTextNewName;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private UserAdapter userAdapter;

    private User userIAm;
    private OutGoer outGoer;
    private User outGoerUser;
    private Venue outGoerVenue;

    private static final String TAG = ViewActivity.class.getSimpleName();

    //----------------------------------------------

    private UserViewModel userViewModel;
    private VenueViewModel venueViewModel;
    private OutGoerViewModel outGoerViewModel;

    //----------------------------------------------

    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        textViewOutput = findViewById(R.id.textOutput);
        spinnerWhoAmI = findViewById(R.id.spinnerWhoAmI);
        fabChangeDetails = findViewById(R.id.fabUpdateDetails);
        editTextNewName = findViewById(R.id.editTextNewName);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        populateSpinner();
        addListeners();

        userList = new ArrayList<>();

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        venueViewModel = ViewModelProviders.of(this).get(VenueViewModel.class);
        outGoerViewModel = ViewModelProviders.of(this).get(OutGoerViewModel.class);

        //observe user data
        if(userViewModel != null){
            LiveData<List<User>> userLiveData = userViewModel.getUserLiveData();

            userLiveData.observe(this, new Observer<List<User>>() {
                @Override
                public void onChanged(@Nullable List<User> users) {
                    userList.clear();
                    for(int i=0; i < users.size(); i++){
                        Log.d(TAG, users.get(i).getUserName());
                        userList.add(users.get(i));
                    }
                }
            });
        }

        //observe venue data
        if(venueViewModel != null){
            LiveData<List<Venue>> venueLiveData = venueViewModel.getVenueLiveData();

            venueLiveData.observe(this, new Observer<List<Venue>>() {
                @Override
                public void onChanged(@Nullable List<Venue> venues) {
                    for(int i=0; i < venues.size(); i++){
                        Log.d(TAG, venues.get(i).getVenueName());
                    }
                }
            });
        }
    }

    private void populateSpinner() {
        final List<User> userList = new ArrayList<>();

        reference.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);
                userList.add(user);
                userAdapter = new UserAdapter(ViewActivity.this, R.layout.support_simple_spinner_dropdown_item, userList);
                userAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spinnerWhoAmI.setAdapter(userAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addListeners() {
        spinnerWhoAmI.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userIAm = userAdapter.getItem(i);
                if(outGoerViewModel != null){
                    LiveData<List<OutGoer>> outGoerLiveData = outGoerViewModel.getOutGoerLiveData(userIAm.getUserId());

                    outGoerLiveData.observe(ViewActivity.this, new Observer<List<OutGoer>>() {
                        @Override
                        public void onChanged(@Nullable List<OutGoer> outGoers) {
                            for(int i=0; i < outGoers.size(); i++){
                                Log.d(TAG, outGoers.get(i).getUserId() +  " is going to " + outGoers.get(i).getVenueId()
                                    + " as of " + calculateTimeSince(outGoers.get(i).getTimeMillis()));
                            }
                        }
                    });
                }
                //loadFeed(userIAm);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fabChangeDetails.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Map<String, Object> userUpdate = new HashMap<>();
                //userUpdate.put("Users/" + userIAm.getUserId(), editTextNewName.getText().toString());

                List<String> idList = outGoerViewModel.getIds();

                for(int i = 0; i < idList.size(); i++){
                    Log.d(TAG, idList.get(i));
//                    userUpdate.put("OutGoer/" + userIAm.getUserId() + "/" + idList.get(i)
//                            + "/userName", newName);

                }

                reference.updateChildren(userUpdate);
            }
        });
    }

    private void loadFeed(User userIAm) {

        textViewOutput.setText("");

        reference.child("OutGoer").child(userIAm.getUserId()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                outGoer = dataSnapshot.getValue(OutGoer.class);

                reference.child("Users").child(outGoer.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        outGoerUser = dataSnapshot.getValue(User.class);

                        reference.child("Venues").child(outGoer.getVenueId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                outGoerVenue = dataSnapshot.getValue(Venue.class);

                                textViewOutput.append("\n" + outGoerUser.getUserName() + " is going to " + outGoerVenue.getVenueName() + " as of "
                                        + calculateTimeSince(outGoer.getTimeMillis()));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private String calculateTimeSince(Long timeMillis) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - timeMillis);
        if(minutes < 60){
            return minutes + " minutes ago";
        } else {
            double d = Math.floor(minutes/ 60);
            Long l = (long) d;
            return l + " hours ago";
        }
    }
}
