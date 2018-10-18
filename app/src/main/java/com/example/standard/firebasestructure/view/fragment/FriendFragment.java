package com.example.standard.firebasestructure.view.fragment;


import android.arch.lifecycle.*;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.*;
import android.support.v7.widget.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.example.standard.firebasestructure.*;
import com.example.standard.firebasestructure.R;
import com.example.standard.firebasestructure.model.*;
import com.example.standard.firebasestructure.model.adapters.*;
import com.example.standard.firebasestructure.view.*;
import com.example.standard.firebasestructure.viewmodel.UserViewModel;

import java.util.List;

public class FriendFragment extends Fragment {

    private Spinner spinnerCurrentUser;
    private UserAdapter userAdapter;
    private User currentUser;

    private RecyclerView recyclerViewFriends;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerManager;

    private UserViewModel userViewModel;

    public FriendFragment() {
        // Required empty public constructor
    }

    public static FriendFragment newInstance(){
        return new FriendFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_friend, container, false);

        ((MainActivity) getActivity()).setActionBarTitle(R.string.friends);
        ((MainActivity) getActivity()).setDisplayHomeAsUpEnabled(false);

        spinnerCurrentUser = fragmentView.findViewById(R.id.spinnerCurrentUser);
        recyclerViewFriends = fragmentView.findViewById(R.id.recyclerFriends);
        recyclerViewFriends.setHasFixedSize(true);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        if(userViewModel != null){
            LiveData<List<User>> userLiveData = userViewModel.getUserLiveData();

            userLiveData.observe(this, new Observer<List<User>>() {
                @Override
                public void onChanged(@Nullable List<User> users) {
                    userAdapter = new UserAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item,users);
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

                    friendLiveData.observe(FriendFragment.this, new Observer<List<User>>() {
                        @Override
                        public void onChanged(@Nullable List<User> friends) {
                            friends = Utils.sortFriendsByDestinations(friends);
                            recyclerManager = new LinearLayoutManager(
                                    getContext());
                            recyclerViewFriends.setLayoutManager(recyclerManager);
                            recyclerAdapter = new FriendRecyclerAdapter(getContext(), friends, new OnItemClickListener() {
                                @Override
                                public void onItemClick(Venue venue) {
                                    //null
                                }

                                @Override
                                public void onItemClick(User friend) {
                                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("friend", friend);
                                    FriendDetailFragment friendDetailFragment= FriendDetailFragment.newInstance();
                                    friendDetailFragment.setArguments(bundle);
                                    fragmentTransaction.replace(R.id.framelayoutContainer, friendDetailFragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }

                                @Override
                                public void onItemClick(OutGoer outGoer) {
                                    //null
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

        return fragmentView;
    }

}
