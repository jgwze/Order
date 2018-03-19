package com.joeecodes.firebaselogin.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.joeecodes.firebaselogin.Interface.ItemClickListener;
import com.joeecodes.firebaselogin.R;

/**
 * Created by Lenovo on 14/3/2018.
 */

public class ServerDeliveryOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnLongClickListener,
        View.OnCreateContextMenuListener {
    public TextView txtOrderId,txtOrderStatus,txtOrderPhone,txtOrderAddress,txtOrderComment;
    private ItemClickListener itemClickListener;
    public ServerDeliveryOrderViewHolder(View itemView) {
        super(itemView);
        txtOrderAddress = (TextView)itemView.findViewById(R.id.order_address);
        txtOrderId = (TextView)itemView.findViewById(R.id.order_id);
        txtOrderStatus = (TextView)itemView.findViewById(R.id.order_status);
        txtOrderPhone = (TextView)itemView.findViewById(R.id.order_phone);
        txtOrderComment = (TextView)itemView.findViewById(R.id.order_comment);

        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) { itemClickListener.onClick(view,getAdapterPosition(),false);}

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Select the Action");
        contextMenu.add(0,0,getAdapterPosition(),"Update");
        contextMenu.add(0,1,getAdapterPosition(),"Delete");


    }

    @Override
    public boolean onLongClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),true);
        return true;
    }
}
