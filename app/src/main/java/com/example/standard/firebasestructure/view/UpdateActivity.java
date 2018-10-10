package com.example.standard.firebasestructure.view;

import android.arch.lifecycle.*;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.example.standard.firebasestructure.R;
import com.example.standard.firebasestructure.model.*;
import com.example.standard.firebasestructure.model.adapters.*;
import com.example.standard.firebasestructure.viewmodel.*;

import java.util.*;

public class UpdateActivity extends AppCompatActivity {

    private static final String TAG = UpdateActivity.class.getSimpleName();

    private Spinner spinnerUserIAm;
    private EditText editTextNewName;
    private Button buttonUpdate;

    private UserViewModel userViewModel;
    private VenueViewModel venueViewModel;
    private OutGoerViewModel outGoerViewModel;

    private UserAdapter userAdapter;
    private VenueAdapter venueAdapter;

    private User currentUser;
    private Venue currentVenue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        spinnerUserIAm = findViewById(R.id.spinnerWhoAmI);
        editTextNewName = findViewById(R.id.editTextNewName);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        venueViewModel = ViewModelProviders.of(this).get(VenueViewModel.class);
        outGoerViewModel = ViewModelProviders.of(this).get(OutGoerViewModel.class);

        if(userViewModel != null){
            LiveData<List<User>> userLiveData = userViewModel.getUserLiveData();

            userLiveData.observe(UpdateActivity.this, new Observer<List<User>>() {
                @Override
                public void onChanged(@Nullable List<User> userList) {
                    userAdapter = new UserAdapter(UpdateActivity.this, R.layout.support_simple_spinner_dropdown_item, userList);
                    userAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    spinnerUserIAm.setAdapter(userAdapter);
                }
            });
        }

        spinnerUserIAm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                currentUser = userAdapter.getItem(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextNewName.getText().toString().length() != 0 && editTextNewName.getText() != null){
                    Map<String, Object> newNameMap = new HashMap<>();
                    newNameMap.put("userName", editTextNewName.getText().toString());
                    userViewModel.updateUser(currentUser, newNameMap);
                    outGoerViewModel.updateOutGoerUser(currentUser, newNameMap);
                }
            }
        });

    }


}
