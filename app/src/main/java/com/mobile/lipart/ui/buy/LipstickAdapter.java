package com.mobile.lipart.ui.buy;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.mobile.lipart.R;
import com.mobile.lipart.model.Lipstick;
import com.mobile.lipart.model.LipstickItem;

import java.util.ArrayList;


public class LipstickAdapter extends RecyclerView.Adapter<LipstickAdapter.LipstickViewHolder> {

        // List to store all the contact details
        private ArrayList<LipstickItem> lipstickList;

        // Counstructor for the Class
        public LipstickAdapter(ArrayList<LipstickItem> lipstickList) {
            this.lipstickList = lipstickList;

        }

        // This method creates views for the RecyclerView by inflating the layout
        // Into the viewHolders which helps to display the items in the RecyclerView
        @Override
        public LipstickViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

            // Inflate the layout view you have created for the list rows here
            View view = layoutInflater.inflate(R.layout.item_lipstick, parent, false);
            return new LipstickViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return lipstickList == null? 0: lipstickList.size();
        }

        // This method is called when binding the data to the views being created in RecyclerView
        @Override
        public void onBindViewHolder(@NonNull LipstickViewHolder holder, final int position) {
            final LipstickItem lipstick = lipstickList.get(position);

            // Set the data to the views here
            holder.bindToLipstick(lipstick);

            // You can set click listners to indvidual items in the viewholder here
            // make sure you pass down the listner or make the Data members of the viewHolder public

        }

        // This is your ViewHolder class that helps to populate data to the view
        public class LipstickViewHolder extends RecyclerView.ViewHolder {

            public TextView lipstickBrandView;
            public TextView lipstickNameView;
            public ImageView lipstickColorView;
            public Button lipstickBuyButton;
            public Button lipstickTryButton;


            public LipstickViewHolder(View itemView) {
                super(itemView);

                lipstickBrandView = itemView.findViewById(R.id.lipstickName);
                lipstickNameView = itemView.findViewById(R.id.lipstickBrand);
                lipstickColorView = itemView.findViewById(R.id.lipstickColor);
                lipstickBuyButton = itemView.findViewById(R.id.button_buy);
                lipstickTryButton = itemView.findViewById(R.id.button_try);
            }

            public void bindToLipstick(LipstickItem lipstick) {
                lipstickBrandView.setText(lipstick.getBrandName());
                lipstickNameView.setText(lipstick.getName());
                lipstickColorView.setColorFilter(Color.parseColor(lipstick.getColor()));
                lipstickBuyButton.setText("Buy");
                lipstickTryButton.setText("Try");
            }
        }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }}
