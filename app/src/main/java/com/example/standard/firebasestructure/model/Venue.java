package com.example.standard.firebasestructure.model;

import android.support.annotation.Nullable;

import java.util.*;

public class Venue {
    private String venueId;
    private String venueName;
    @Nullable
    private Map<String, Boolean> attendees;

    public Venue() {
    }

    public Venue(String venueId, String venueName, Map<String,Boolean> attendees) {
        this.venueId = venueId;
        this.venueName = venueName;
        this.attendees = attendees;
    }

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public Map<String,Boolean> getAttendees() {
        return attendees;
    }

    public void setAttendees(Map<String, Boolean> attendees) {
        this.attendees = attendees;
    }

}

