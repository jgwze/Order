package com.joeecodes.firebaselogin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.joeecodes.firebaselogin.Common.Common;
import com.joeecodes.firebaselogin.Database.Database;
import com.joeecodes.firebaselogin.Model.DeliveryRequest;
import com.joeecodes.firebaselogin.Model.Order;
import com.joeecodes.firebaselogin.Model.Request;
import com.joeecodes.firebaselogin.ViewHolder.CartAdapter;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    
    FirebaseDatabase database;
    DatabaseReference deliveryrequests,requests;
    TextView txtTotalPrice;
    Button btnPlaceOrderDineIn,btnPlaceOrderDelivery;

    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Implement font before setContentView
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
        .setDefaultFontPath("fonts/Assistant-SemiBold.otf").setFontAttrId(R.attr.fontPath).build());
        setContentView(R.layout.activity_cart);
        
        //init Firebase
        database=FirebaseDatabase.getInstance();
        deliveryrequests=database.getReference("DeliveryRequests");
        requests=database.getReference("Requests");


        //Init Layout
        recyclerView=(RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this); 
        recyclerView.setLayoutManager(layoutManager);
        
        txtTotalPrice = (TextView)findViewById(R.id.total);
        btnPlaceOrderDineIn = (Button)findViewById(R.id.btnPlaceOrderDineIn);
        btnPlaceOrderDelivery = (Button)findViewById(R.id.btnPlaceOrderDelivery);

        btnPlaceOrderDineIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Request request = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        txtTotalPrice.getText().toString(),
                        cart
                );
                //Submit to Firebase
                //Using System.CurrentMilli as key
                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request); //System.currentTimeMillis acting as Receipt Number

                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this,"Thank You, Your Dine In Order has been Placed",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        //on click event for btnPlaceOrder
        btnPlaceOrderDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create a new request to be submitted into xx
                if(cart.size()>0)
                showAlertDialog();
                else Toast.makeText(Cart.this,"Cart is Empty!",Toast.LENGTH_SHORT).show();


            }
        });
        loadListFood();
    }
//prompt for address to be keyed in
    private void showAlertDialog() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One more step!");
        alertDialog.setMessage("Enter your Address: ");

        LayoutInflater inflater=this.getLayoutInflater();
        View order_address_comment = inflater.inflate(R.layout.order_address_comment,null);
        final MaterialEditText edtAddress =(MaterialEditText)order_address_comment.findViewById(R.id.edtAddress);
        final MaterialEditText edtComment =(MaterialEditText)order_address_comment.findViewById(R.id.edtComment);

        alertDialog.setView(order_address_comment);
        alertDialog.setIcon((R.drawable.ic_shopping_cart_black_24dp));

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DeliveryRequest request = new DeliveryRequest(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        edtAddress.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        edtComment.getText().toString(),
                        cart
                );
                //Submit to Firebase
                //Using System.CurrentMilli to key
                deliveryrequests.child(String.valueOf(System.currentTimeMillis())).setValue(request); //System.currentTimeMillis acting as Receipt Number

                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this,"Thank You, Your Delivery Order has been Placed",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    private void loadListFood() {
        cart=new Database(this).getCarts();
        adapter = new CartAdapter(cart,this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        //Calculate total price
        int total = 0;
        for(Order order:cart)
            total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
        Locale locale = new Locale("en","US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        txtTotalPrice.setText(fmt.format(total));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.DELETE))
            deleteCart(item.getOrder());
        return true;
    }

    private void deleteCart(int position) {
        cart.remove(position);
        new Database(this).cleanCart(); //Delete all data from SQLlite
        //Update new Data from List<Order> to SQLite
        for(Order item:cart)
            new Database(this).addToCart(item);
        loadListFood();
    }
}
