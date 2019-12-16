package com.mobile.lipart.main.buy;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.mobile.lipart.R;

public class BuyFragment extends Fragment {

    private BuyViewModel buyViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_buy, container, false);

        /**
         * Since the main functionality is about buying lipsticks,
         * it can be found in the LipstickFragment. This fragment serves
         * simply as a container.
         * */
        Fragment someFragment = new LipstickFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_buy, someFragment );
        transaction.addToBackStack(null);
        transaction.commit();

        return root;
    }
}