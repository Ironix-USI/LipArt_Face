package com.mobile.lipart.ui.profile.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mobile.lipart.R;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Feedback");
        setContentView(R.layout.activity_feedback);
    }
}
