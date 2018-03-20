package com.joeecodes.firebaselogin;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.joeecodes.firebaselogin.Common.Common;
import com.joeecodes.firebaselogin.ViewHolder.DeliveryOrderDetailAdapter;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ServerDeliveryOrderDetail extends AppCompatActivity {

    TextView order_id,order_phone,order_address,order_total,order_comment;
    String order_id_value="";
    RecyclerView lstFood;
    RecyclerView.LayoutManager layoutManager;

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
        setContentView(R.layout.activity_server_delivery_order_detail);

        order_id=(TextView)findViewById(R.id.order_id);
        order_phone=(TextView)findViewById(R.id.order_phone);
        order_address=(TextView)findViewById(R.id.order_address);
        order_total=(TextView)findViewById(R.id.order_total);
        order_comment=(TextView)findViewById(R.id.order_comment);

        lstFood=(RecyclerView)findViewById(R.id.lstFood);
        lstFood.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        lstFood.setLayoutManager(layoutManager);

        if(getIntent()!=null)
            order_id_value=getIntent().getStringExtra("OrderId");

        //Set Value
        order_id.setText(order_id_value);
        order_phone.setText(Common.currentRequest.getPhone());
        order_total.setText(Common.currentRequest.getTotal());
        order_address.setText(Common.currentRequest.getAddress());
        order_comment.setText(Common.currentRequest.getComment());

        DeliveryOrderDetailAdapter adapter = new DeliveryOrderDetailAdapter(Common.currentRequest.getFoods());
        adapter.notifyDataSetChanged();
        lstFood.setAdapter(adapter);
    }
}
