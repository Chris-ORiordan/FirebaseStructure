package com.example.standard.firebasestructure.view;

import android.arch.lifecycle.*;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.example.standard.firebasestructure.R;
import com.example.standard.firebasestructure.model.*;
import com.example.standard.firebasestructure.model.adapters.*;
import com.example.standard.firebasestructure.viewmodel.*;

import java.util.*;

import static com.example.standard.firebasestructure.Utils.calculateTimeSince;

public class FeedActivity extends AppCompatActivity {

    private static final String TAG = FeedActivity.class.getSimpleName();

    private Spinner spinnerUserIAm;
    private UserAdapter userAdapter;
    private OutGoerAdapter outGoerAdapter;
    private ListView listViewOutGoers;

    private UserViewModel userViewModel;
    private VenueViewModel venueViewModel;
    private OutGoerViewModel outGoerViewModel;

    private List<User> users;
    private List<Venue> venues;
    private List<OutGoer> outGoers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment);

        spinnerUserIAm = findViewById(R.id.spinnerUserIAm);
        listViewOutGoers = findViewById(R.id.listViewOutGoers);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        venueViewModel = ViewModelProviders.of(this).get(VenueViewModel.class);
        outGoerViewModel = ViewModelProviders.of(this).get(OutGoerViewModel.class);

        users = new ArrayList<>();
        venues = new ArrayList<>();
        outGoers = new ArrayList<>();

        if(userViewModel != null){
            LiveData<List<User>> userLiveData = userViewModel.getUserLiveData();

            userLiveData.observe(this, new Observer<List<User>>() {
                @Override
                public void onChanged(@Nullable List<User> userList) {
                    users = userList;
                    userAdapter = new UserAdapter(FeedActivity.this, R.layout.support_simple_spinner_dropdown_item, userList);
                    userAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    spinnerUserIAm.setAdapter(userAdapter);
                }
            });
        }

        spinnerUserIAm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(outGoerViewModel != null){
                    LiveData<List<OutGoer>> outGoerLiveData = outGoerViewModel.getOutGoerLiveData(userAdapter.getItem(i).getUserId());

                    outGoerLiveData.observe(FeedActivity.this, new Observer<List<OutGoer>>() {
                        @Override
                        public void onChanged(@Nullable List<OutGoer> outGoers) {
                            outGoerAdapter = new OutGoerAdapter(getApplicationContext(), R.layout.view_feed_item, outGoers);
                            listViewOutGoers.setAdapter(outGoerAdapter);
                            for(int i=0; i < outGoers.size(); i++) {
                                OutGoer outGoer = outGoers.get(i);
                                Log.d(TAG, outGoer.getUserName() + " is going to " + outGoer.getVenueName() + " as of " + calculateTimeSince(outGoer.getTimeMillis()));
                            }
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
