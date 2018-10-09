package com.example.standard.firebasestructure.viewmodel;

import android.arch.lifecycle.*;
import android.arch.core.util.Function;
import android.support.annotation.*;
import android.util.Log;

import com.example.standard.firebasestructure.network.FirebaseQueryLiveData;
import com.example.standard.firebasestructure.model.OutGoer;
import com.google.firebase.database.*;

import java.util.*;

public class OutGoerViewModel extends ViewModel {

    private static final String TAG = OutGoerViewModel.class.getSimpleName();

    private static final DatabaseReference OUTGOER_REF = FirebaseDatabase.getInstance().getReference().child("OutGoer");

    List<String> idList = new ArrayList<>();

    private List<OutGoer> outGoerList = new ArrayList<>();

    @NonNull
    public LiveData<List<OutGoer>> getOutGoerLiveData(String viewerId){
        FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(OUTGOER_REF.child(viewerId));
        LiveData<List<OutGoer>> outGoerLiveData = Transformations.map(liveData, new Deserialiser());
        return outGoerLiveData;
    }

    private class Deserialiser implements Function<DataSnapshot, List<OutGoer>> {
        @Override
        public List<OutGoer> apply(DataSnapshot dataSnapshot) {
            outGoerList.clear();
            for(DataSnapshot dsp: dataSnapshot.getChildren()) {
                String userID = dsp.getKey();
                Log.i(TAG + "User ID: ", userID);
                for(DataSnapshot abc: dsp.getChildren()){
                    String venueID = abc.getKey();
                    Log.i(TAG + " Venue ID: ", venueID);
                    OutGoer outGoer = abc.getValue(OutGoer.class);
                    outGoerList.add(outGoer);
                }
            }
            return outGoerList;
        }
    }

    public List<String> getIds(){
        OUTGOER_REF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp: dataSnapshot.getChildren()){
                    idList.add(dsp.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return idList;
    }
}
