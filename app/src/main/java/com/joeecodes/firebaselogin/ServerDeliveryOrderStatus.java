package com.joeecodes.firebaselogin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.joeecodes.firebaselogin.Common.Common;
import com.joeecodes.firebaselogin.Interface.ItemClickListener;
import com.joeecodes.firebaselogin.Model.DeliveryRequest;
import com.joeecodes.firebaselogin.ViewHolder.DeliveryOrderViewHolder;
import com.joeecodes.firebaselogin.ViewHolder.ServerDeliveryOrderViewHolder;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ServerDeliveryOrderStatus extends AppCompatActivity {
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<DeliveryRequest,ServerDeliveryOrderViewHolder> adapter;

    FirebaseDatabase db;
    DatabaseReference deliveryrequests;

    MaterialSpinner spinner;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Implement font before setContentView
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/restaurant_font.otf").setFontAttrId(R.attr.fontPath).build());
        setContentView(R.layout.activity_server_delivery_order_status);

        //Init Firebase
        db = FirebaseDatabase.getInstance();
        deliveryrequests = db.getReference("DeliveryRequests");

        recyclerView = (RecyclerView)findViewById(R.id.listOrders1);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if (getIntent()==null)
            loadOrders(Common.currentUser.getPhone()); //Load all Delivery Orders
        else
            loadOrders(getIntent().getStringExtra("userPhone"));
    }

    private void loadOrders(String phone) {
        adapter = new FirebaseRecyclerAdapter<DeliveryRequest, ServerDeliveryOrderViewHolder>(
                DeliveryRequest.class,
                R.layout.deliveryorder_layout,
                ServerDeliveryOrderViewHolder.class,
                deliveryrequests
        ){

            @Override
            protected void populateViewHolder(ServerDeliveryOrderViewHolder viewHolder, final DeliveryRequest model, int position) {
                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText(Common.convertCodetoStatus(model.getStatus()));
                viewHolder.txtOrderAddress.setText(model.getAddress());
                viewHolder.txtOrderPhone.setText(model.getPhone());
                viewHolder.txtOrderComment.setText(model.getComment());


                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        if(!isLongClick) {
//                            Intent trackingOrder = new Intent(ServerDeliveryOrderStatus.this, OrderTracking.class);
//                            Common.currentRequest = model;
//                            startActivity(trackingOrder);
//                        }
//                        else
//                        {
                            Intent orderDetailIntent = new Intent(ServerDeliveryOrderStatus.this,ServerDeliveryOrderDetail.class);
                            Common.currentRequest=model;
                            orderDetailIntent.putExtra("OrderId",adapter.getRef(position).getKey());
                            startActivity(orderDetailIntent);
                        }
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.UPDATE)){
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));}
        else if (item.getTitle().equals(Common.DELETE)){
            deleteOrder(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));}
//        else{ Toast.makeText(ServerDeliveryOrderStatus.this, "View Details", Toast.LENGTH_SHORT).show();}
        return super.onContextItemSelected(item);
    }

    private void deleteOrder(String key, DeliveryRequest item) {
        deliveryrequests.child(key).removeValue();
        Toast.makeText(ServerDeliveryOrderStatus.this,"Delivery Request"+key+ "was deleted", Toast.LENGTH_SHORT).show();
    }

    private void showUpdateDialog (String key, final DeliveryRequest item) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ServerDeliveryOrderStatus.this);
        alertDialog.setTitle("Update Delivery Status");
        alertDialog.setMessage("Please Change Delivery Status");

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.server_update_delivery_order_layout,null);

        spinner = (MaterialSpinner)view.findViewById(R.id.statusSpinner);
        spinner.setItems("Placed","On The Way","Received");

        alertDialog.setView(view);
        final String localkey = key;
        alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
                item.setStatus(String.valueOf(spinner.getSelectedIndex()));

                deliveryrequests.child(localkey).setValue(item);

            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
}
