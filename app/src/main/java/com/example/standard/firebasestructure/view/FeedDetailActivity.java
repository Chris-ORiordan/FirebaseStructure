package com.example.standard.firebasestructure.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.standard.firebasestructure.R;
import com.example.standard.firebasestructure.model.OutGoer;

public class FeedDetailActivity extends AppCompatActivity {

    private TextView textViewDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);

        textViewDetails = findViewById(R.id.textViewDetails);

        OutGoer outGoer = (OutGoer) getIntent().getSerializableExtra("outGoer");
        textViewDetails.setText(outGoer.getUserName() + " is going to " + outGoer.getVenueName());
    }
}
