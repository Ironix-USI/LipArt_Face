package com.mobile.lipart.main.profile.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mobile.lipart.R;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Feedback");
        setContentView(R.layout.activity_feedback);

        /**
         * Attaching a listener to the SEND button in
         * FeedbackActivity, including a Toast pop up
         * upon clicking.
         * */
        Button button = findViewById(R.id.button_send_feedback);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Thank you for your feedback!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}
