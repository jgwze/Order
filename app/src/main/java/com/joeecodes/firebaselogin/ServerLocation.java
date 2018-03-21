package com.joeecodes.firebaselogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.joeecodes.firebaselogin.Common.Common;
import com.joeecodes.firebaselogin.Interface.ItemClickListener;
import com.joeecodes.firebaselogin.Model.DeliveryRequest;
import com.joeecodes.firebaselogin.ViewHolder.ServerLocationViewHolder;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Lenovo on 21/3/2018.
 */

public class ServerLocation extends AppCompatActivity {
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<DeliveryRequest,ServerLocationViewHolder> adapter;

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
        setContentView(R.layout.activity_server_location);

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
        adapter = new FirebaseRecyclerAdapter<DeliveryRequest, ServerLocationViewHolder>(
                DeliveryRequest.class,
                R.layout.deliveryorder_layout,
                ServerLocationViewHolder.class,
                deliveryrequests
        ){

            @Override
            protected void populateViewHolder(ServerLocationViewHolder viewHolder, final DeliveryRequest model, int position) {
                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText(Common.convertCodetoStatus(model.getStatus()));
                viewHolder.txtOrderAddress.setText(model.getAddress());
                viewHolder.txtOrderPhone.setText(model.getPhone());
                viewHolder.txtOrderComment.setText(model.getComment());


                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        if(!isLongClick) {
                            Intent trackingOrder = new Intent(ServerLocation.this, OrderTracking.class);
                            Common.currentRequest = model;
                            startActivity(trackingOrder);
                        }
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
}
