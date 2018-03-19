package com.joeecodes.firebaselogin.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.joeecodes.firebaselogin.Interface.ItemClickListener;
import com.joeecodes.firebaselogin.R;

/**
 * Created by Lenovo on 8/3/2018.
 */

public class DeliveryOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtOrderId,txtOrderStatus,txtOrderPhone,txtOrderAddress,txtOrderComment;
    private ItemClickListener itemClickListener;
    public DeliveryOrderViewHolder(View itemView) {
        super(itemView);
        txtOrderAddress = (TextView)itemView.findViewById(R.id.order_address);
        txtOrderId = (TextView)itemView.findViewById(R.id.order_id);
        txtOrderStatus = (TextView)itemView.findViewById(R.id.order_status);
        txtOrderPhone = (TextView)itemView.findViewById(R.id.order_phone);
        txtOrderComment = (TextView)itemView.findViewById(R.id.order_comment);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}




