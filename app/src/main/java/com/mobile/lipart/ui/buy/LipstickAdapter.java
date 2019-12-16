package com.mobile.lipart.ui.buy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.lipart.R;
import com.mobile.lipart.model.LipstickItem;

import java.util.ArrayList;


public class LipstickAdapter extends RecyclerView.Adapter<LipstickAdapter.LipstickViewHolder> {

        /**
         * List to store all the lipstick details
         */
        private ArrayList<LipstickItem> lipstickList;
        private Context mContext;


        public LipstickAdapter(Context c, ArrayList<LipstickItem> lipstickList) {
            this.lipstickList = lipstickList;
            this.mContext = c;

        }

        /**
         * This method creates views for the RecyclerView by inflating the layout
         * into the view holders which helps to display the items in the RecyclerView.
         */
        @Override
        public LipstickViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


            View view = LayoutInflater.from(mContext).inflate(R.layout.item_lipstick, parent, false);
            return new LipstickViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return lipstickList == null? 0: lipstickList.size();
        }

        /**
         * This method is called when binding the data
         * to the views being created in the recycler view.
         */
        @Override
        public void onBindViewHolder(@NonNull LipstickViewHolder holder, final int position) {
            final LipstickItem lipstick = lipstickList.get(position);

            /**
             * Setting the data to the views here
             * */
            holder.bindToLipstick(lipstick);

        }

        /**
         * This is the lipstick view holder class
         * that helps to populating the data to the view.
         * */
        public class LipstickViewHolder extends RecyclerView.ViewHolder {

            public TextView lipstickBrandView;
            public TextView lipstickNameView;
            public ImageView lipstickColorView;
            public Button lipstickBuyButton;


            public LipstickViewHolder(View itemView) {
                super(itemView);

                lipstickBrandView = itemView.findViewById(R.id.lipstickName);
                lipstickNameView = itemView.findViewById(R.id.lipstickBrand);
                lipstickColorView = itemView.findViewById(R.id.lipstickColor);
                lipstickBuyButton = itemView.findViewById(R.id.button_buy);

            }

            public void bindToLipstick(final LipstickItem lipstick) {
                lipstickBrandView.setText(lipstick.getBrandName());
                lipstickNameView.setText(lipstick.getName());
                lipstickColorView.setColorFilter(Color.parseColor(lipstick.getColor()));
                lipstickBuyButton.setText("Buy");
                lipstickBuyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /**
                         * Opening the browser on Buy button next to
                         * a particular lipstick being clicked.
                         * */
                        String url = lipstick.getLink();
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        i.setData(Uri.parse(url));
                        v.getContext().startActivity(i);
                    }
                });
            }



        }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }}
