package com.mobile.lipart.ui.buy;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.mobile.lipart.CircleActivity;
import com.mobile.lipart.R;
import com.mobile.lipart.ui.circle.CircleViewModel;

public class BuyFragment extends Fragment {

    private BuyViewModel buyViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        buyViewModel =
//                ViewModelProviders.of(this).get(BuyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_buy, container, false);
//        final TextView textView = root.findViewById(R.id.text_buy);
//        buyViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
////                textView.setText(s);
//            }
//        });

        Fragment someFragment = new LipstickFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_buy, someFragment ); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();

//        startActivity(new Intent(getActivity(), BuyActivity.class));
        return root;
    }
}