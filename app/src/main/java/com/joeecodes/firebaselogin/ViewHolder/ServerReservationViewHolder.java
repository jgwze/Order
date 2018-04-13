package com.joeecodes.firebaselogin.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.joeecodes.firebaselogin.Interface.ItemClickListener;
import com.joeecodes.firebaselogin.R;

public class ServerReservationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnCreateContextMenuListener {
    public TextView txtReserveNamePhone,txtReserveConfirm,txtReservePax,txtReserveDateTime;
    private ItemClickListener itemClickListener;
    public ServerReservationViewHolder(View itemView) {
        super(itemView);
        txtReserveNamePhone = (TextView)itemView.findViewById(R.id.reserve_namephone);
        txtReserveConfirm = (TextView)itemView.findViewById(R.id.reserve_confirmation);
        txtReservePax = (TextView)itemView.findViewById(R.id.reserve_pax);
        txtReserveDateTime = (TextView)itemView.findViewById(R.id.reserve_datetime);

        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) { itemClickListener.onClick(view,getAdapterPosition(),false);}

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Select the Action");
        contextMenu.add(0,0,getAdapterPosition(),"Accept");
        contextMenu.add(0,1,getAdapterPosition(),"Decline");
    }
}
