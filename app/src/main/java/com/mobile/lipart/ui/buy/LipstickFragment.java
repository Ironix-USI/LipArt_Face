package com.mobile.lipart.ui.buy;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mobile.lipart.R;
import com.mobile.lipart.model.Brand;
import com.mobile.lipart.model.Lipstick;
import com.mobile.lipart.model.LipstickItem;
import com.mobile.lipart.model.LipstickSeries;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class LipstickFragment extends Fragment {
    private static final String TAG = "LipstickFragment";

    private LipstickAdapter lipstickAdapter;
    private ArrayList<LipstickItem> lipstickList = new ArrayList<>();
    private RecyclerView recycler;
    private LinearLayoutManager layoutManager;
    private Context context;
    private ArrayList<String> brandsList = new ArrayList<>();

    public LipstickFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_buy, container, false);
        context = getContext();
        recycler = rootView.findViewById(R.id.lipstickList);
        recycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);

        // 2. set layoutManger



        InputStream is = getResources().openRawResource(R.raw.color);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (Exception e) {
            Log.e("Unhandled exception", e.toString());
        }
        finally {
            try {
                is.close();
            } catch (Exception e) {
                Log.e("JSON closing", e.toString());
            }
        }

        String jsonString = writer.toString().trim();

        JsonArray convertedObject = new Gson().fromJson(jsonString, JsonObject.class).get("brands").getAsJsonArray();

        final ArrayList<LipstickItem> lipstickItems = new ArrayList<>();
        ArrayList<Brand> brands = new ArrayList<>();
        for (JsonElement element : convertedObject) {
            String bName = ((JsonObject) element).get("name").getAsString();
            ArrayList<LipstickSeries> lipstickSeries = new ArrayList<>();
            for(JsonElement serie: ((JsonObject) element).get("series").getAsJsonArray()) {
                String lsName = ((JsonObject) serie).get("name").getAsString();
                String lsLink = "";
                if(((JsonObject) serie).get("link").getAsString() instanceof String) {
                    lsLink = ((JsonObject) serie).get("link").getAsString();
                }
                ArrayList<Lipstick> lipsticks = new ArrayList<>();
                for(JsonElement lipstick: ((JsonObject) serie).get("lipsticks").getAsJsonArray()) {
                    String lName = ((JsonObject) lipstick).get("name").getAsString();
                    String lId = ((JsonObject) lipstick).get("id").getAsString();
                    String lColor = ((JsonObject) lipstick).get("color").getAsString();
                    lipsticks.add(new Lipstick(lId, lColor, lName));
                    lipstickItems.add(new LipstickItem(lId, lColor, lName, bName, lsName, lsLink));
                }
                lipstickSeries.add(new LipstickSeries(lsName, lsLink, lipsticks));
            }
            brands.add(new Brand(bName, lipstickSeries));
            if(brandsList.isEmpty()){
                brandsList.add("All");
            }
            if (!brandsList.contains(bName)) {
                brandsList.add(bName);
            }
        }


        for (LipstickItem item : lipstickItems) {
            lipstickList.add(item);
        }

        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(layoutManager);
        lipstickAdapter = new LipstickAdapter(context, lipstickList);
        // 4. set adapter
        recycler.setAdapter(lipstickAdapter);

        Spinner spin = (Spinner) rootView.findViewById(R.id.spinner_lipsticks);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, brandsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ArrayList<LipstickItem> secondList = new ArrayList<>();

                ((TextView) parentView.getChildAt(0)).setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                ((TextView) parentView.getChildAt(0)).setTextSize(15);
                ((TextView) parentView.getChildAt(0)).setAllCaps(true);

                for (int counter = 0; counter < lipstickItems.size(); counter++) {
                    if(lipstickItems.get(counter).getBrandName() == brandsList.get(position) && position != 0) {
                        secondList.add(lipstickItems.get(counter));
                    }

                }

                lipstickList.clear();
                if(position == 0) {
                    lipstickList.addAll(lipstickItems);
                } else {
                    lipstickList.addAll(secondList);
                }

                lipstickAdapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });



        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




    }








}
