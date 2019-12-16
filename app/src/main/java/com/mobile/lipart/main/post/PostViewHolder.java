package com.mobile.lipart.main.post;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.lipart.R;
import com.mobile.lipart.model.Post;
import com.mobile.lipart.model.User;

public class PostViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "CircleActivity";
    public TextView authorView;
    public ImageView starView;
    public TextView numStarsView;
    public TextView bodyView;
    public ImageView colorView;
    public Button deleteView;
    private DatabaseReference mDatabase;

    public PostViewHolder(View itemView) {
        super(itemView);

        authorView = itemView.findViewById(R.id.postAuthor);
        starView = itemView.findViewById(R.id.star);
        numStarsView = itemView.findViewById(R.id.postNumStars);
        bodyView = itemView.findViewById(R.id.postBody);
        colorView = itemView.findViewById(R.id.commentColor);
        deleteView = itemView.findViewById(R.id.deleteButton);
    }

    public void bindToPost(final Post post, View.OnClickListener starClickListener) {
        authorView.setText(post.author);
        numStarsView.setText(String.valueOf(post.starCount));
        bodyView.setText(post.body);

        starView.setOnClickListener(starClickListener);

        if (post.color == null || post.color.equals("")) {
            colorView.setColorFilter(Color.WHITE);
        }
        else {
            colorView.setColorFilter(Color.parseColor(post.color));
        }

        if (post.uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            deleteView.setVisibility(View.VISIBLE);
            deleteView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deletePost(post.key);
                        }
                    });
        }
        else {
            deleteView.setVisibility(View.INVISIBLE);
        }
    }

    private void deletePost(final String key) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                        } else {
                            // Write new post
                            removePost(userId, key);
                        }
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
        // [END single_value_read]
    }

    // [START write_fan_out]
    private void removePost(String userId, String key) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        DatabaseReference postRef = mDatabase.child("/posts/" + key);
        postRef.removeValue();
        postRef = mDatabase.child("/user-posts/" + userId + "/" + key);
        postRef.removeValue();
    }
}