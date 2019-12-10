package com.mobile.lipart.ui.buy;

import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import com.mobile.lipart.BaseActivity;
import com.mobile.lipart.R;
import com.mobile.lipart.model.Lipstick;

import java.util.ArrayList;

public class BuyActivity extends BaseActivity {
    private LipstickAdapter lipstickAdapter;
    private ArrayList<Lipstick> lipstickList = new ArrayList<>();
    private RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_buy);

//        recycler = findViewById(R.id.lipstickList);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        recycler.setLayoutManager(layoutManager);
//        lipstickAdapter = new LipstickAdapter(lipstickList);
//        recycler.setAdapter(lipstickAdapter);
//
//        //Load the date from the network or other resources
//        //into the array list asynchronously
//
//        lipstickList.add(new Lipstick("19", "\"#B13C79\"", "Le Fuchsia"));
//        lipstickList.add(new Lipstick("19", "\"#B13C79\"", "Le Fuchsia"));
//        lipstickList.add(new Lipstick("19", "\"#B13C79\"", "Le Fuchsia"));
//
//        lipstickAdapter.notifyDataSetChanged();
    }


}
