package com.joeecodes.firebaselogin.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joeecodes.firebaselogin.Model.Order;
import com.joeecodes.firebaselogin.R;

import java.util.List;

class MyViewHolder extends RecyclerView.ViewHolder{

    public TextView name,quantity,price,discount;

    public MyViewHolder(View itemView) {
        super(itemView);
        name =(TextView)itemView.findViewById(R.id.product_name);
        quantity =(TextView)itemView.findViewById(R.id.product_quantity);
        price =(TextView)itemView.findViewById(R.id.product_price);
        discount =(TextView)itemView.findViewById(R.id.product_discount);
    }
}

public class DeliveryOrderDetailAdapter extends RecyclerView.Adapter<MyViewHolder> {
    List<Order> myOrders;

    public DeliveryOrderDetailAdapter(List<Order> myOrders) {
        this.myOrders = myOrders;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.delivery_order_detail_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Order order=myOrders.get(position);
        holder.name.setText(String.format("Name : %s",order.getProductName()));
        holder.quantity.setText(String.format("Quantity : %s",order.getQuantity()));
        holder.price.setText(String.format("Price : %s",order.getPrice()));
        holder.discount.setText(String.format("Discount : %s",order.getDiscount()));
    }

    @Override
    public int getItemCount() {
        return myOrders.size();
    }
}
