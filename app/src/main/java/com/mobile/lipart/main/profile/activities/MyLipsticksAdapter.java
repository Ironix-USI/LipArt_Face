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

    /**
     * List to store all the lipstick details.
     */
    private ArrayList<Lipstick> lipsticks;
    private LayoutInflater mInflater;


    MyLipsticksAdapter(Context context, ArrayList<Lipstick> lipsticks) {
        this.mInflater = LayoutInflater.from(context);
        this.lipsticks = lipsticks;
    }

    /**
     * This method creates views for the RecyclerView by inflating the layout
     * into the view holders which helps to display the items in the RecyclerView.
     */
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.my_lipstick_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * This method is called when binding the data
     * to the views being created in the recycler view.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(getItemCount() != 0) {
            /**
             * Setting the data to the views here.
             * */
            holder.lipstickName.setText(lipsticks.get(position).getName().isEmpty() ? "Unnamed" : lipsticks.get(position).getName());
            holder.myLipstick.setColorFilter(Color.parseColor(lipsticks.get(position).getColor().trim()));
        }

    }

    /**
     * Total number of lipsticks.
     * */
    @Override
    public int getItemCount() {
        return lipsticks == null ? 0: lipsticks.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView myLipstick;
        TextView lipstickName;

        /**
         * This is the view holder class
         * that helps to populating the data to the view.
         * */
        ViewHolder(View itemView) {
            super(itemView);
            myLipstick = itemView.findViewById(R.id.my_lipstick_item);
            lipstickName = itemView.findViewById(R.id.my_lipstick_item_tooltip);
        }

    }

}
