package com.mobile.lipart.ui.profile;

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

        /**
         * Retrieving the userId.
         * */
        String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        /**
         * Retrieving the number of posts by the retrieved userId.
         * */
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

        /**
         * Retrieving the user name of the current
         * logged in user.
         * */
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (email.contains("@")) {
            mUsername = email.split("@")[0];
        } else {
            mUsername = email;
        }
    }

    /**
     * Returning the post count to the ProfileFragment.
     * */
    public LiveData<String> getPostCount() {
        return mPostCount;
    }

    /**
     * Returning the user name to the ProfileFragment.
     * */
    public static String getUserName() { return mUsername; }

    /**
     * Returning the user id to the ProfileFragment.
     * */
    public static String getUserId() { return FirebaseAuth.getInstance().getCurrentUser().getUid(); }
}