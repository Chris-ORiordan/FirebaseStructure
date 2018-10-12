package com.example.standard.firebasestructure.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.standard.firebasestructure.R;
import com.example.standard.firebasestructure.model.User;

public class FriendDetailActivity extends AppCompatActivity {

    private TextView friendName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);

        friendName = findViewById(R.id.friendName);

        User friend = (User) getIntent().getSerializableExtra("friend");
        friendName.setText(friend.getUserName());
    }
}
