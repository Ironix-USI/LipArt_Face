package com.mobile.lipart.ui.profile.activities;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.Event;
import com.mobile.lipart.R;
import com.mobile.lipart.model.Lipstick;

import java.util.ArrayList;

public class MyLipsticksActivity extends AppCompatActivity {

    MyLipsticksAdapter adapter;
    private DatabaseReference mDatabase;
    ArrayList<Lipstick> lipsticks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("MyLipsticks");
        setContentView(R.layout.activity_my_lipsticks);

        /**
         * Setting up a recycler view with a grid layout,
         * consisting of 3 columns and attaching a MyLipsticksAdapter
         * instance to the recycler.
         * */
        RecyclerView recyclerView = findViewById(R.id.my_lipsticks_recycler);
        int numberOfColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new MyLipsticksAdapter(this, lipsticks);
        recyclerView.setAdapter(adapter);

        /**
         * Retrieving an instance of the db.
         * */
        mDatabase = FirebaseDatabase.getInstance().getReference();

        /**
         * Retrieving the user-colors table from the db
         * for the current logged in user and saving
         * a list of all their lipsticks to be added
         * to the adapter.
         * */
        mDatabase.child("user-colors").child(getIntent().getStringExtra("userId"))
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lipsticks.clear();
                for (DataSnapshot lipstickSnapshot: dataSnapshot.getChildren()) {
                    ArrayList<String> lipstickProperties = new ArrayList<>();
                    for (DataSnapshot lipstickSnapshotItem: lipstickSnapshot.getChildren()) {
                        lipstickProperties.add(lipstickSnapshotItem.getValue().toString());
                    }
                    lipsticks.add(new Lipstick(lipstickSnapshot.getKey(), lipstickProperties.get(0), lipstickProperties.get(1) == null ? "" : lipstickProperties.get(1)));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("LIPSTICK", "onCancelled", databaseError.toException());
            }

        });

    }


}
