package com.mobile.lipart.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.mobile.lipart.R;
import com.mobile.lipart.ui.profile.activities.FeedbackActivity;
import com.mobile.lipart.ui.profile.activities.MyLipsticksActivity;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        final TextView countTextView = root.findViewById(R.id.profile_post_count);
        profileViewModel.getPostCount().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                countTextView.setText(s);
            }

        });
        final TextView userTextView = root.findViewById(R.id.username);
        userTextView.setText(ProfileViewModel.getUserName());

        (root.findViewById(R.id.my_lipsticks_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), MyLipsticksActivity.class);
                myIntent.putExtra("userId", ProfileViewModel.getUserId());
                startActivity(myIntent);
            }
        });

        (root.findViewById(R.id.feedback_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), FeedbackActivity.class);
//        myIntent.putExtra("key", value); //Optional parameters
                startActivity(myIntent);
            }
        });

        return root;
    }

}