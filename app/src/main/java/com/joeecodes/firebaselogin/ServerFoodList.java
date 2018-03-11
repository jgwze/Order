package com.joeecodes.firebaselogin;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.joeecodes.firebaselogin.Interface.ItemClickListener;
import com.joeecodes.firebaselogin.Model.Food;
import com.joeecodes.firebaselogin.ViewHolder.FoodViewHolder;
import com.joeecodes.firebaselogin.ViewHolder.ServerFoodViewHolder;
import com.squareup.picasso.Picasso;

public class ServerFoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FloatingActionButton fab;

    //Firebase
    FirebaseDatabase db;
    DatabaseReference foodList;
    FirebaseStorage storage;
    StorageReference storageReference;

    String CategoryId = "";
    FirebaseRecyclerAdapter<Food,ServerFoodViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_food_list);

        //Firebase
        db = FirebaseDatabase.getInstance();
        foodList = db.getReference("Foods");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //Init View
        recyclerView = (RecyclerView) findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        /*fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Code later
            }
        });*/

//Get Intent here
        if (getIntent() != null)
        {CategoryId = getIntent().getStringExtra("CategoryId");}
        if (!CategoryId.isEmpty() && CategoryId != null) {
            loadListFood(CategoryId);
        }
    }

    private void loadListFood(String category_id) {
        adapter = new FirebaseRecyclerAdapter<Food, ServerFoodViewHolder>(Food.class,R.layout.food_item,ServerFoodViewHolder.class,
                foodList.orderByChild("menuId").equalTo(category_id)) {
            //            Select * from Food where menuId=Category
            @Override
            protected void populateViewHolder(ServerFoodViewHolder viewHolder, Food model, int position) {
                viewHolder.food_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.food_image);

//                final Food local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
//                        Toast.makeText(FoodList.this, ""+local.getName(),Toast.LENGTH_SHORT).show();
                        //Start Activity
                        /*Intent foodDetail = new Intent (ServerFoodList.this,FoodDetail.class);
                        foodDetail.putExtra("foodId",adapter.getRef(position).getKey()); //Send foodId to new Activity
                        startActivity(foodDetail);*/

                        //code later
                    }
                });
            }
        };
        //Set Adapter
        Log.d("TAG",""+adapter.getItemCount());
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
}
