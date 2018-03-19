package com.joeecodes.firebaselogin.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.joeecodes.firebaselogin.Interface.ItemClickListener;
import com.joeecodes.firebaselogin.R;

/**
 * Created by Lenovo on 8/2/2018.
 */

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView food_name;
    public ImageView food_image,share_image;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FoodViewHolder(View itemView) {
        super(itemView);
        food_name = (TextView)itemView.findViewById(R.id.food_name);
        food_image = (ImageView)itemView.findViewById(R.id.food_image);
        share_image = (ImageView)itemView.findViewById(R.id.btnShare);

        itemView.setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
            itemClickListener.onClick(view,getAdapterPosition(),false);
        }
}
