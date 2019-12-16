package com.mobile.lipart.main.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String> mPostCount;
    private DatabaseReference mDatabase;
    public static String mUsername;


    public ProfileViewModel() {
        mPostCount = new MutableLiveData<>();

        String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("user-posts/" + uId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mPostCount.setValue(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (email.contains("@")) {
            mUsername = email.split("@")[0];
        } else {
            mUsername = email;
        }
    }

    public LiveData<String> getPostCount() {
        return mPostCount;
    }
    public static String getUserName() { return mUsername; }
    public static String getUserId() { return FirebaseAuth.getInstance().getCurrentUser().getUid(); }
}