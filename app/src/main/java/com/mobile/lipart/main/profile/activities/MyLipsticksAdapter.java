package com.mobile.lipart.main.profile.activities;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.lipart.R;
import com.mobile.lipart.model.Lipstick;

import java.util.ArrayList;


public class MyLipsticksAdapter extends RecyclerView.Adapter<MyLipsticksAdapter.ViewHolder> {

    private ArrayList<Lipstick> lipsticks;
    private LayoutInflater mInflater;


    MyLipsticksAdapter(Context context, ArrayList<Lipstick> lipsticks) {
        this.mInflater = LayoutInflater.from(context);
        this.lipsticks = lipsticks;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.my_lipstick_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(getItemCount() != 0) {
            holder.lipstickName.setText(lipsticks.get(position).getName().isEmpty() ? "Unnamed" : lipsticks.get(position).getName());
            holder.myLipstick.setColorFilter(Color.parseColor(lipsticks.get(position).getColor().trim()));
        }

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return lipsticks == null ? 0: lipsticks.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView myLipstick;
        TextView lipstickName;

        ViewHolder(View itemView) {
            super(itemView);
            myLipstick = itemView.findViewById(R.id.my_lipstick_item);
            lipstickName = itemView.findViewById(R.id.my_lipstick_item_tooltip);
        }

    }

}
