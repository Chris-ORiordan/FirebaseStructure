package com.example.standard.firebasestructure.view;

import android.arch.lifecycle.*;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.example.standard.firebasestructure.R;
import com.example.standard.firebasestructure.model.*;
import com.example.standard.firebasestructure.model.adapters.UserAdapter;
import com.example.standard.firebasestructure.viewmodel.*;

import java.util.List;

public class VenueDetailActivity extends AppCompatActivity {

    private Spinner spinnerCurrentUser;
    private TextView textViewVenueName;
    private ToggleButton tglUserGoingOut;

    private UserViewModel userViewModel;
    private VenueViewModel venueViewModel;
    private OutGoerViewModel outGoerViewModel;

    private UserAdapter userAdapter;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_detail);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        venueViewModel = ViewModelProviders.of(this).get(VenueViewModel.class);
        outGoerViewModel = ViewModelProviders.of(this).get(OutGoerViewModel.class);

        spinnerCurrentUser = findViewById(R.id.spinnerUserThatIAm);
        textViewVenueName = findViewById(R.id.textViewVenueName);
        tglUserGoingOut = findViewById(R.id.tglUserGoingOut);

        Intent intent = getIntent();
        Venue chosenVenue = (Venue) intent.getSerializableExtra("venue");
        textViewVenueName.setText(chosenVenue.getVenueName());

        if(userViewModel != null){
            LiveData<List<User>> userLiveData = userViewModel.getUserLiveData();

            userLiveData.observe(this, new Observer<List<User>>() {
                @Override
                public void onChanged(@Nullable List<User> users) {
                    userAdapter = new UserAdapter(VenueDetailActivity.this, R.layout.support_simple_spinner_dropdown_item, users);
                    userAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    spinnerCurrentUser.setAdapter(userAdapter);
                }
            });
        }

        spinnerCurrentUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentUser = userAdapter.getItem(i);
                if(currentUser.getDestinations() != null){
                    for(String venueID: currentUser.getDestinations().keySet()){
                        if(venueID.equals(chosenVenue.getVenueId())){
                            tglUserGoingOut.setChecked(true);
                            break;
                        } else {
                            tglUserGoingOut.setChecked(false);
                        }
                    }
                } else {
                    tglUserGoingOut.setChecked(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tglUserGoingOut.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(tglUserGoingOut.isPressed()){
                    if(checked){
                        userViewModel.addDestination(currentUser, chosenVenue);
                        venueViewModel.addAttendee(currentUser, chosenVenue);
                        outGoerViewModel.createOutGoer(currentUser, chosenVenue);
                    } else {
                        userViewModel.removeDestination(currentUser, chosenVenue);
                        venueViewModel.removeAttendee(currentUser, chosenVenue);
                        outGoerViewModel.deleteOutGoer(currentUser, chosenVenue);
                    }
                }
            }
        });

    }
}
