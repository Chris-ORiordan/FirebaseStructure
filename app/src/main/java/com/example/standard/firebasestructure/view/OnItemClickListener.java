package com.example.standard.firebasestructure.view;

import com.example.standard.firebasestructure.model.*;

public interface OnItemClickListener {

    void onItemClick(Venue venue);

    void onItemClick(User friend);

    void onItemClick(OutGoer outGoer);

}
