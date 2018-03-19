package com.joeecodes.firebaselogin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.joeecodes.firebaselogin.Common.Common;
import com.joeecodes.firebaselogin.Interface.ItemClickListener;
import com.joeecodes.firebaselogin.Model.Category;
import com.joeecodes.firebaselogin.Model.Food;
import com.joeecodes.firebaselogin.ViewHolder.FoodViewHolder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;

    String categoryId = "";
    FirebaseRecyclerAdapter<Food,FoodViewHolder> adapter;

    //Facebook Share
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    //Create Target From Picasso
    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            //Create Photo from Bitmap
            SharePhoto photo=new SharePhoto.Builder().setBitmap(bitmap).build();
            if(ShareDialog.canShow(SharePhotoContent.class))
            {
                SharePhotoContent content=new SharePhotoContent.Builder().addPhoto(photo).build();
                shareDialog.show(content);
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //Init Facebook Share
        callbackManager = CallbackManager.Factory.create();
        shareDialog=new ShareDialog(this);

        //Firebase
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Foods");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Get Intent here
        if (getIntent() != null)
            categoryId = getIntent().getStringExtra("categoryId");
        if (!categoryId.isEmpty() && categoryId != null) {

            if(Common.IsConnectedToInternet(getBaseContext()))
            loadListFood(categoryId);
            else{ Toast.makeText(FoodList.this, "Please Check Your Connection", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private void loadListFood(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,R.layout.food_item,FoodViewHolder.class,
                foodList.orderByChild("menuId").equalTo(categoryId)) {
//            Select * from Food where MenuId=Category
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, final Food model, int position) {
                viewHolder.food_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.food_image);

                //Event for btnShare onClick
                viewHolder.share_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Picasso.with(getApplicationContext()).load(model.getImage()).into(target);
                    }
                });

                final Food local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
//                        Toast.makeText(FoodList.this, ""+local.getName(),Toast.LENGTH_SHORT).show();
                    //Start Activity
                        Intent foodDetail = new Intent (FoodList.this,FoodDetail.class);
                        foodDetail.putExtra("foodId",adapter.getRef(position).getKey()); //Send foodId to new Activity
                        startActivity(foodDetail);
                    }
                });
            }
        };
        //Set Adapter
        Log.d("TAG",""+adapter.getItemCount());
        recyclerView.setAdapter(adapter);
    }
}