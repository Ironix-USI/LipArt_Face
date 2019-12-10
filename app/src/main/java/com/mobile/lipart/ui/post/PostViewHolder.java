package com.mobile.lipart.ui.post;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mobile.lipart.R;
import com.mobile.lipart.model.Post;

public class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView authorView;
    public ImageView starView;
    public TextView numStarsView;
    public TextView bodyView;
    public ImageView colorView;

    public PostViewHolder(View itemView) {
        super(itemView);

        authorView = itemView.findViewById(R.id.postAuthor);
        starView = itemView.findViewById(R.id.star);
        numStarsView = itemView.findViewById(R.id.postNumStars);
        bodyView = itemView.findViewById(R.id.postBody);
        colorView = itemView.findViewById(R.id.commentColor);
    }

    public void bindToPost(Post post, View.OnClickListener starClickListener) {
        authorView.setText(post.author);
        numStarsView.setText(String.valueOf(post.starCount));
        bodyView.setText(post.body);

        starView.setOnClickListener(starClickListener);

        if (post.color == null || post.color.equals("")) {
            colorView.setColorFilter(Color.WHITE);
        }
        else {
            colorView.setColorFilter(Color.parseColor(post.color));
        }
    }
}