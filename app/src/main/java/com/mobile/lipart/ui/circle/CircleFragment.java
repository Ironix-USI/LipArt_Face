package com.mobile.lipart.ui.circle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.mobile.lipart.R;

public class CircleFragment extends Fragment {

    private CircleViewModel circleViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        circleViewModel =
                ViewModelProviders.of(this).get(CircleViewModel.class);
        View root = inflater.inflate(R.layout.fragment_circle, container, false);
        final TextView textView = root.findViewById(R.id.text_circle);
        circleViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}