package com.example.standard.firebasestructure.view.fragment;


import android.arch.lifecycle.*;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.example.standard.firebasestructure.*;
import com.example.standard.firebasestructure.R;
import com.example.standard.firebasestructure.model.User;
import com.example.standard.firebasestructure.model.adapters.UserAdapter;
import com.example.standard.firebasestructure.view.MainActivity;
import com.example.standard.firebasestructure.viewmodel.*;
import com.squareup.haha.perflib.Main;

import java.util.*;

public class ProfileFragment extends Fragment {

    private EditText editTextNewName;
    private TextView textViewNumDestinations;
    private TextView textViewNumFriends;
    private Button buttonUpdate;

    private UserViewModel userViewModel;
    private OutGoerViewModel outGoerViewModel;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(){
        return new ProfileFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_profile, container, false);

        ((MainActivity) getActivity()).setActionBarTitle(R.string.update);
        ((MainActivity) getActivity()).setDisplayHomeAsUpEnabled(false);

        editTextNewName = fragmentView.findViewById(R.id.editTextNewName);
        textViewNumDestinations = fragmentView.findViewById(R.id.textViewNumDestinations);
        textViewNumFriends = fragmentView.findViewById(R.id.textViewNumFriends);
        buttonUpdate = fragmentView.findViewById(R.id.buttonUpdate);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        outGoerViewModel = ViewModelProviders.of(this).get(OutGoerViewModel.class);

        editTextNewName.setText(MainApplication.getCurrentUser().getUserName());
        textViewNumDestinations.setText(MainApplication.getCurrentUser().getDestinations().size() + " Destinations");
        textViewNumFriends.setText(MainApplication.getCurrentUser().getFriends().size() + " Friends");

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextNewName.getText().toString().length() != 0 && editTextNewName.getText() != null){
                    Map<String, Object> newNameMap = new HashMap<>();
                    newNameMap.put("userName", editTextNewName.getText().toString());
                    userViewModel.updateUser(MainApplication.getCurrentUser(), newNameMap);
                    outGoerViewModel.updateOutGoerUser(MainApplication.getCurrentUser(), newNameMap);
                }
            }
        });

        return fragmentView;
    }

}
