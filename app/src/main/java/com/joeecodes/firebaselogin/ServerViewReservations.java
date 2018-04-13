package com.joeecodes.firebaselogin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.joeecodes.firebaselogin.Common.Common;
import com.joeecodes.firebaselogin.Interface.ItemClickListener;
import com.joeecodes.firebaselogin.Model.DeliveryRequest;
import com.joeecodes.firebaselogin.Model.Reserve;
import com.joeecodes.firebaselogin.ViewHolder.ServerDeliveryOrderViewHolder;
import com.joeecodes.firebaselogin.ViewHolder.ServerReservationViewHolder;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ServerViewReservations extends AppCompatActivity {
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Reserve,ServerReservationViewHolder> adapter;

    FirebaseDatabase db;
    DatabaseReference reservations;

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
        setContentView(R.layout.activity_server_view_reservations);

        //Init Firebase
        db = FirebaseDatabase.getInstance();
        reservations = db.getReference("Reservation");

        recyclerView = (RecyclerView)findViewById(R.id.listOrders1);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if (getIntent()==null)
            loadReservations(Common.currentUser.getPhone()); //Load all Reservation Bookings
        else
            loadReservations(getIntent().getStringExtra("userPhone"));
    }

    private void loadReservations(String phone) {
        adapter = new FirebaseRecyclerAdapter<Reserve, ServerReservationViewHolder>(
                Reserve.class,
                R.layout.viewreservation_layout,
                ServerReservationViewHolder.class,
                reservations
        ){

            @Override
            protected void populateViewHolder(ServerReservationViewHolder viewHolder, final Reserve model, int position) {
                viewHolder.txtReserveNamePhone.setText(model.getName()+" "+model.getPhone());
                viewHolder.txtReserveConfirm.setText(Common.convertConfirmationStatus(model.getConfirmation()));
                viewHolder.txtReservePax.setText(model.getPax()+" Pax");
                viewHolder.txtReserveDateTime.setText(adapter.getRef(position).getKey());

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals("Accept")){
            acceptOrder(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));}
        else if (item.getTitle().equals("Decline")){
            rejectOrder(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));}
//        Toast.makeText(ServerViewReservations.this, "Decline o", Toast.LENGTH_SHORT).show();
        return super.onContextItemSelected(item);
    }

    private void acceptOrder(String key, Reserve item) {
        item.setConfirmation("1");
        final String localkey = key;
        reservations.child(localkey).setValue(item);
    }

    private void rejectOrder(String key, Reserve item) {
        //reservations.child(key).removeValue();
        //Toast.makeText(ServerViewReservations.this,"Delivery Request"+key+ "was deleted", Toast.LENGTH_SHORT).show();
        item.setConfirmation("2");
        final String localkey = key;
        reservations.child(localkey).setValue(item);
    }
}
