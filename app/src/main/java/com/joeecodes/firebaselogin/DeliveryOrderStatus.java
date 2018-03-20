package com.joeecodes.firebaselogin;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.joeecodes.firebaselogin.Common.Common;
import com.joeecodes.firebaselogin.Model.DeliveryRequest;
import com.joeecodes.firebaselogin.ViewHolder.DeliveryOrderViewHolder;
import com.joeecodes.firebaselogin.ViewHolder.OrderViewHolder;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DeliveryOrderStatus extends AppCompatActivity {
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<DeliveryRequest,DeliveryOrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference deliveryrequests;

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
        setContentView(R.layout.activity_delivery_order_status);
        //Firebase
        database = FirebaseDatabase.getInstance();
        deliveryrequests = database.getReference("DeliveryRequests");

        recyclerView = (RecyclerView)findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(Common.currentUser.getPhone());
    }

    private void loadOrders(String phone) {
        adapter = new FirebaseRecyclerAdapter<DeliveryRequest, DeliveryOrderViewHolder>(
                DeliveryRequest.class,
                R.layout.deliveryorder_layout,
                DeliveryOrderViewHolder.class,
                deliveryrequests.orderByChild("phone").equalTo(phone)
        ) {
            @Override
            protected void populateViewHolder(DeliveryOrderViewHolder viewHolder, DeliveryRequest model, int position) {
                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText(convertCodetoStatus(model.getStatus()));
                viewHolder.txtOrderAddress.setText(model.getAddress());
                viewHolder.txtOrderPhone.setText(model.getPhone());
                viewHolder.txtOrderComment.setText(model.getComment());

            }
        };
        recyclerView.setAdapter(adapter);


    }
    private String convertCodetoStatus(String status) {
        //this.status="0"; default is 0. 0:Placed/Awaiting Confirmation 1:Shipped/Reservation Placed

        if(status.equals("0"))
            return("Placed");
        else if(status.equals("1"))
            return "On the Way";
        else
            return "Shipped";
    }



}
