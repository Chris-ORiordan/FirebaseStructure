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
    private List<User> friendList = new ArrayList<>();

    @NonNull
    public LiveData<List<User>> getUserLiveData(){
        FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(USER_REF);
        LiveData<List<User>> usersLiveData = Transformations.map(liveData, new Deserialiser());
        return usersLiveData;
    }

    public LiveData<List<User>> getFriendLiveData(Set<String> friendIDs) {
        FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(USER_REF);
        LiveData<List<User>> friendsLiveData = Transformations.map(liveData, new FriendDeserialiser(friendIDs));
        return friendsLiveData;
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

    private class FriendDeserialiser implements Function<DataSnapshot, List<User>>{

        private Set<String> friendIds;

        public FriendDeserialiser(Set<String> friendIDs) {
            this.friendIds = friendIDs;
        }
        @Override
        public List<User> apply(DataSnapshot dataSnapshot) {
            friendList.clear();
            for(String friendId: friendIds){
                for(DataSnapshot dsp: dataSnapshot.getChildren()){
                    User user = dsp.getValue(User.class);
                    if(friendId.equals(user.getUserId())){
                        friendList.add(user);
                    }
                }
            }
            return friendList;
        }
    }

    public void onUserGoingOut(User selectedUser, Venue selectedVenue){
        USER_REF.child(selectedUser.getUserId()).child("destinations").child(selectedVenue.getVenueId()).setValue(true);
    }

    public void createUser(User newUser){
        USER_REF.child(newUser.getUserId()).setValue(newUser);
    }

    public void updateUser(User currentUser, Map<String, Object> newNameMap){
        USER_REF.child(currentUser.getUserId()).updateChildren(newNameMap);
    }

    public void createFriendship(User user1, User user2){
        USER_REF.child(user1.getUserId()).child("friends").child(user2.getUserId()).setValue(true);
        USER_REF.child(user2.getUserId()).child("friends").child(user1.getUserId()).setValue(true);
    }
}
