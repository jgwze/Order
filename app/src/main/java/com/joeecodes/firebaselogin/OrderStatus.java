package com.joeecodes.firebaselogin;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.joeecodes.firebaselogin.Common.Common;
import com.joeecodes.firebaselogin.Model.DeliveryRequest;
import com.joeecodes.firebaselogin.Model.Request;
import com.joeecodes.firebaselogin.ViewHolder.OrderViewHolder;

public class OrderStatus extends AppCompatActivity {
    Button btnViewDeliveryOrders;
public RecyclerView recyclerView;
public RecyclerView.LayoutManager layoutManager;

FirebaseRecyclerAdapter<Request,OrderViewHolder> adapter;

FirebaseDatabase database;
DatabaseReference requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
//        btnViewDeliveryOrders = (Button) findViewById(R.id.ViewDeliveryOrders);

        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerView = (RecyclerView)findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(Common.currentUser.getPhone());
    }

    private void loadOrders(String phone) {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests.orderByChild("phone").equalTo(phone)
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {
                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText(convertCodetoStatus(model.getStatus()));
                viewHolder.txtOrderPhone.setText(model.getPhone());
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


    public void ViewDeliveryOrders(View view) {
        Intent intentDeliveryOrderStatus = new Intent(OrderStatus.this,DeliveryOrderStatus.class);
        startActivity(intentDeliveryOrderStatus);
    }
}
