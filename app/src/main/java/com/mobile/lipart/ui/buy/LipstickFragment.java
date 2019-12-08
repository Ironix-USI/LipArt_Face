package com.mobile.lipart.ui.buy;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mobile.lipart.R;
import com.mobile.lipart.model.Brand;
import com.mobile.lipart.model.Lipstick;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.mobile.lipart.model.LipstickItem;
import com.mobile.lipart.model.LipstickSeries;


public class LipstickFragment extends Fragment {
    private static final String TAG = "LipstickFragment";

    private LipstickAdapter lipstickAdapter;
    private ArrayList<LipstickItem> lipstickList = new ArrayList<>();
    private RecyclerView recycler;
    private LinearLayoutManager layoutManager;

    public LipstickFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_buy, container, false);
        recycler = rootView.findViewById(R.id.lipstickList);
        recycler.setHasFixedSize(true);
        // 1. get a reference to recyclerView


        // this is data fro recycler view

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

        ArrayList<LipstickItem> lipstickItems = new ArrayList<>();
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
        }


        for (LipstickItem item : lipstickItems) {
            lipstickList.add(item);
        }

//        lipstickList.add(new Lipstick("19", "\"#B13C79\"", "Le Fuchsia"));
//        lipstickList.add(new Lipstick("19", "\"#B13C79\"", "Le Fuchsia"));
//        lipstickList.add(new Lipstick("19", "\"#B13C79\"", "Le Fuchsia"));



        // 3. create an adapter

//        lipstickAdapter.notifyDataSetChanged();
        // 5. set item animator to DefaultAnimator

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        layoutManager = new LinearLayoutManager(getActivity());

        // 2. set layoutManger
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(layoutManager);
        lipstickAdapter = new LipstickAdapter(lipstickList);
        // 4. set adapter
        recycler.setAdapter(lipstickAdapter);

    }




}
