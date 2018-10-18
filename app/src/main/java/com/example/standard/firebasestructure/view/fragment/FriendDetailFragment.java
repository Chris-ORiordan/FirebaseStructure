package com.example.standard.firebasestructure.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.standard.firebasestructure.R;
import com.example.standard.firebasestructure.model.User;
import com.example.standard.firebasestructure.view.MainActivity;

public class FriendDetailFragment extends Fragment {

    private TextView textViewFriendName;


    public FriendDetailFragment() {
        // Required empty public constructor
    }

    public static FriendDetailFragment newInstance(){
        return new FriendDetailFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_friend_detail, container, false);

        ((MainActivity) getActivity()).setDisplayHomeAsUpEnabled(true);

        textViewFriendName = fragmentView.findViewById(R.id.textViewFriendName);

        User friend = (User) getArguments().getSerializable("friend");
        textViewFriendName.setText(friend.getUserName());

        return fragmentView;
    }

}
