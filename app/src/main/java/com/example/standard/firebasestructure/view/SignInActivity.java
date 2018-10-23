package com.example.standard.firebasestructure.view;

import android.arch.lifecycle.*;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.example.standard.firebasestructure.*;
import com.example.standard.firebasestructure.R;
import com.example.standard.firebasestructure.model.User;
import com.example.standard.firebasestructure.model.adapters.UserAdapter;
import com.example.standard.firebasestructure.viewmodel.UserViewModel;

import java.util.List;

public class SignInActivity extends AppCompatActivity {

    private Spinner spinnerUsers;
    private Button buttonSignIn;
    private UserAdapter userAdapter;

    private UserViewModel userViewModel;
    private User selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        spinnerUsers = findViewById(R.id.spinnerUsers);
        buttonSignIn = findViewById(R.id.buttonSignIn);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        if(userViewModel != null){
            LiveData<List<User>> userLiveData = userViewModel.getUserLiveData();

            userLiveData.observe(this, new Observer<List<User>>() {
                @Override
                public void onChanged(@Nullable List<User> users) {
                    userAdapter = new UserAdapter(SignInActivity.this, R.layout.support_simple_spinner_dropdown_item, users);
                    userAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    spinnerUsers.setAdapter(userAdapter);
                }
            });
        }

        spinnerUsers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedUser = userAdapter.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedUser != null){
                    MainApplication.setCurrentUser(selectedUser);
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
