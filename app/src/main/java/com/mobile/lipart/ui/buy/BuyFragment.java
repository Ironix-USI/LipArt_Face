package com.mobile.lipart.ui.buy;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobile.lipart.R;

public class BuyFragment extends Fragment {

    private BuyViewModel buyViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        buyViewModel =
                ViewModelProviders.of(this).get(BuyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_buy, container, false);
        final TextView textView = root.findViewById(R.id.text_buy);
        buyViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

}
