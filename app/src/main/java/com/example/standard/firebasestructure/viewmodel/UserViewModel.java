package com.example.standard.firebasestructure.viewmodel;

import android.arch.core.util.Function;
import android.arch.lifecycle.*;
import android.support.annotation.*;

import com.example.standard.firebasestructure.model.*;
import com.example.standard.firebasestructure.network.FirebaseQueryLiveData;
import com.google.firebase.database.*;

import java.util.*;

public class UserViewModel extends ViewModel {
    private static final DatabaseReference USER_REF = FirebaseDatabase.getInstance().getReference().child("Users");

    private List<User> userList = new ArrayList<>();

    @NonNull
    public LiveData<List<User>> getUserLiveData(){
        FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(USER_REF);
        LiveData<List<User>> usersLiveData = Transformations.map(liveData, new Deserialiser());
        return usersLiveData;
    }

    private class Deserialiser implements Function<DataSnapshot, List<User>>{
        @Override
        public List<User> apply(DataSnapshot dataSnapshot) {
            userList.clear();

            for(DataSnapshot dsp: dataSnapshot.getChildren()){
                User user = dsp.getValue(User.class);
                userList.add(user);
            }
            return userList;
        }
    }

    public void onUserGoingOut(User selectedUser, Venue selectedVenue){
        USER_REF.child(selectedUser.getUserId()).child("destinations").child(selectedVenue.getVenueId()).setValue(true);
    }


    public void updateUser(User currentUser, Map<String, Object> newNameMap){
        USER_REF.child(currentUser.getUserId()).updateChildren(newNameMap);
    }
}
