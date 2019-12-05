package com.mobile.lipart.ui.snap;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.mobile.lipart.ChooserActivity;
import com.mobile.lipart.R;

public class SnapFragment extends Fragment {

    private SnapViewModel snapViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        snapViewModel =
                ViewModelProviders.of(this).get(SnapViewModel.class);
        View root = inflater.inflate(R.layout.fragment_snap, container, false);
        final TextView textView = root.findViewById(R.id.text_snap);
        snapViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        startActivity(new Intent(getActivity(), ChooserActivity.class));
        return root;
    }

}
