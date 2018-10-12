package com.example.standard.firebasestructure.view;

import android.arch.lifecycle.*;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.*;
import android.view.View;
import android.widget.*;

import com.example.standard.firebasestructure.*;
import com.example.standard.firebasestructure.R;
import com.example.standard.firebasestructure.model.*;
import com.example.standard.firebasestructure.model.adapters.*;
import com.example.standard.firebasestructure.viewmodel.UserViewModel;

import java.util.*;

public class FriendActivity extends AppCompatActivity {

    private Spinner spinnerCurrentUser;
    private UserAdapter userAdapter;
    private User currentUser;

    private RecyclerView recyclerViewFriends;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerManager;

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        spinnerCurrentUser = findViewById(R.id.spinnerCurrentUser);
        recyclerViewFriends = findViewById(R.id.recyclerFriends);
        recyclerViewFriends.setHasFixedSize(true);

        if(userViewModel != null){
            LiveData<List<User>> userLiveData = userViewModel.getUserLiveData();

            userLiveData.observe(this, new Observer<List<User>>() {
                @Override
                public void onChanged(@Nullable List<User> users) {
                    userAdapter = new UserAdapter(FriendActivity.this, R.layout.support_simple_spinner_dropdown_item,users);
                    userAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    spinnerCurrentUser.setAdapter(userAdapter);
                }
            });
        }

        spinnerCurrentUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentUser = userAdapter.getItem(i);
                if(userViewModel != null){
                    LiveData<List<User>> friendLiveData = userViewModel.getFriendLiveData(currentUser.getFriends().keySet());

                    friendLiveData.observe(FriendActivity.this, new Observer<List<User>>() {
                        @Override
                        public void onChanged(@Nullable List<User> friends) {
                            friends = Utils.sortFriendsByDestinations(friends);
                            recyclerManager = new LinearLayoutManager(FriendActivity.this);
                            recyclerViewFriends.setLayoutManager(recyclerManager);
                            recyclerAdapter = new FriendRecyclerAdapter(FriendActivity.this, friends, new OnItemClickListener() {
                                @Override
                                public void onItemClick(Venue venue) {
                                    //null
                                }

                                @Override
                                public void onItemClick(User friend) {
                                    Intent intent = new Intent(FriendActivity.this, FriendDetailActivity.class);
                                    intent.putExtra("friend", friend);
                                    startActivity(intent);
                                }
                            });
                            recyclerViewFriends.setAdapter(recyclerAdapter);
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
