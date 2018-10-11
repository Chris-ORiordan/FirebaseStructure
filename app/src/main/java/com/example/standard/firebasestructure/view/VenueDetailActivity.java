package com.example.standard.firebasestructure.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.standard.firebasestructure.R;
import com.example.standard.firebasestructure.model.Venue;

public class VenueDetailActivity extends AppCompatActivity {

    private TextView textViewVenueName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_detail);

        textViewVenueName = findViewById(R.id.textViewVenueName);

        Intent intent = getIntent();
        Venue venue = (Venue) intent.getSerializableExtra("venue");
        textViewVenueName.setText(venue.getVenueName());
    }
}
