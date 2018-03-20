package com.joeecodes.firebaselogin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.joeecodes.firebaselogin.Common.Common;
import com.joeecodes.firebaselogin.Interface.ItemClickListener;
import com.joeecodes.firebaselogin.Model.Category;
import com.joeecodes.firebaselogin.Model.Food;
import com.joeecodes.firebaselogin.ViewHolder.FoodViewHolder;
import com.joeecodes.firebaselogin.ViewHolder.ServerFoodViewHolder;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ServerFoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    RelativeLayout rootLayout;

    FloatingActionButton fab;

    //Firebase
    FirebaseDatabase db;
    DatabaseReference foodList;
    FirebaseStorage storage;
    StorageReference storageReference;

    String categoryId = "";
    FirebaseRecyclerAdapter<Food,ServerFoodViewHolder> adapter;

    //Add new Food
    MaterialEditText edtAddNewCategoryName,edtServerDescription,edtServerDiscount,edtServerPrice;
    Button btnBrowseFile,btnUpload;
    TextView showValid;

    Food newFood;
    Uri saveUri;

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

        rootLayout=(RelativeLayout)findViewById(R.id.rootLayout);

        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddFoodDialog();
            }
        });

//Get Intent here
        if (getIntent() != null)
        {categoryId = getIntent().getStringExtra("categoryId");}
        if (!categoryId.isEmpty() && categoryId != null) {
            loadListFood(categoryId);
        }
    }

    private void showAddFoodDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(ServerFoodList.this);
        alert.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        alert.setTitle("Add new Food");
        alert.setMessage("Please fill in all fields :");
        LayoutInflater inflater = this.getLayoutInflater();
        View add_category = inflater.inflate(R.layout.add_new_food_layout, null);

        edtAddNewCategoryName = add_category.findViewById(R.id.edtAddNewCategoryName);
        edtServerDescription = add_category.findViewById(R.id.edtServerDescription);
        edtServerDiscount = add_category.findViewById(R.id.edtServerDiscount);
        edtServerPrice = add_category.findViewById(R.id.edtServerPrice);

        btnBrowseFile = add_category.findViewById(R.id.btnBrowseFile);
        btnUpload = add_category.findViewById(R.id.btnUpload);
        showValid= add_category.findViewById(R.id.showValid);

        btnBrowseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrowseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        alert.setView(add_category);

        alert.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
                if(newFood != null){
                    foodList.push().setValue(newFood);
                    Snackbar.make(rootLayout, newFood.getName() + " was added !", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });

        alert.show();
    }
    private void BrowseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an image"), Common.PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Common.PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null){
            saveUri = data.getData();
            showValid.setText("Valid Image Path");
        }
    }

    private void uploadImage() {
        if(saveUri != null){
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Uploading...");
            dialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/" + imageName);

            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            dialog.dismiss();
//                            Toast.makeText(ServerHome.this, "Uploaded successfully !", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    newFood = new Food();
                                    newFood.setName(edtAddNewCategoryName.getText().toString());
                                    newFood.setDescription(edtServerDescription.getText().toString());
                                    newFood.setPrice(edtServerPrice.getText().toString());
                                    newFood.setDiscount(edtServerDiscount.getText().toString());
                                    newFood.setMenuId(categoryId);
                                    newFood.setImage(uri.toString());

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(ServerFoodList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = 100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
                            dialog.setMessage(progress + "23% uploaded");
                        }
                    });
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

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.UPDATE))
        {
            showUpdateFoodDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        else if(item.getTitle().equals(Common.DELETE))
        {
            deleteFood(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));

        }
            return super.onContextItemSelected(item);
    }

    private void deleteFood(String key, Food item) {
        foodList.child(key).removeValue();
//        Snackbar.make(drawer, item.getName() + " was deleted !", Snackbar.LENGTH_LONG).show();
        Snackbar.make(rootLayout, item.getName() + " was deleted !", Snackbar.LENGTH_LONG).show();


    }

    private void showUpdateFoodDialog(final String key, final Food item) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(ServerFoodList.this);
        alert.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        alert.setTitle("Edit category");
        alert.setMessage("Please fill in all fields :");
        LayoutInflater inflater = this.getLayoutInflater();
        View add_category = inflater.inflate(R.layout.add_new_food_layout, null);

        edtAddNewCategoryName = add_category.findViewById(R.id.edtAddNewCategoryName);
        edtServerDescription = add_category.findViewById(R.id.edtServerDescription);
        edtServerDiscount = add_category.findViewById(R.id.edtServerDiscount);
        edtServerPrice = add_category.findViewById(R.id.edtServerPrice);

        //Set Default value for Update Dialog

        edtAddNewCategoryName.setText(item.getName());
        edtServerDescription.setText(item.getDescription());
        edtServerDiscount.setText(item.getDiscount());
        edtServerPrice.setText(item.getPrice());


        btnBrowseFile = add_category.findViewById(R.id.btnBrowseFile);
        btnUpload = add_category.findViewById(R.id.btnUpload);
        showValid= add_category.findViewById(R.id.showValid);

        btnBrowseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrowseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage(item);
            }
        });

        alert.setView(add_category);

        alert.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
//                if(newFood != null){

                    item.setName(edtAddNewCategoryName.getText().toString());
                    item.setPrice(edtServerPrice.getText().toString());
                    item.setDiscount(edtServerDiscount.getText().toString());
                    item.setDescription(edtServerDescription.getText().toString());


//                    foodList.push().setValue(newFood); //only for creating new
                    foodList.child(key).setValue(item);
                    Snackbar.make(rootLayout, item.getName() + " was edited !", Snackbar.LENGTH_LONG).show();
//                }

        }
        });

        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });

        alert.show();
    }

    private void changeImage(final Food item) {
        if(saveUri != null){
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Uploading...");
            dialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/" + imageName);

            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            dialog.dismiss();
//                            Toast.makeText(ServerHome.this, "Uploaded successfully !", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    item.setImage(uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(ServerFoodList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = 100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
                            dialog.setMessage("23% uploaded");
                        }
                    });
        }
    }
}
