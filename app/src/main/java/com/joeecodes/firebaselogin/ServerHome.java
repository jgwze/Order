package com.joeecodes.firebaselogin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.joeecodes.firebaselogin.Common.Common;
import com.joeecodes.firebaselogin.Interface.ItemClickListener;
import com.joeecodes.firebaselogin.Model.Category;
import com.joeecodes.firebaselogin.ViewHolder.ServerMenuViewHolder;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import static com.joeecodes.firebaselogin.Common.Common.PICK_IMAGE_REQUEST;

public class ServerHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView txtFullName;

    //Initiate Database
    FirebaseDatabase database;
    DatabaseReference category;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseRecyclerAdapter<Category,ServerMenuViewHolder> adapter;

    //View
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    MaterialEditText edtAddNewCategoryName;
    Button btnBrowseFile,btnUpload;
    TextView showValid;

    Category newCategory;
    Uri saveUri;

    DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu Management");
        setSupportActionBar(toolbar);

        //init Firebase
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view2);
        navigationView.setNavigationItemSelectedListener(this);

        //Show Name of User
        View headerView = navigationView.getHeaderView(0);
        txtFullName = (TextView)headerView.findViewById(R.id.txtFullName);
        txtFullName.setText(Common.currentUser.getName());

        //Initiate View to Load Menu
        recycler_menu=(RecyclerView)findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);
        loadMenu();
    }


    private void loadMenu(){
        adapter = new FirebaseRecyclerAdapter<Category, ServerMenuViewHolder>(Category.class,R.layout.menu_item,ServerMenuViewHolder.class,category) {
            @Override
            protected void populateViewHolder(ServerMenuViewHolder viewHolder, Category model, int position) {
                viewHolder.txtMenuName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.imageView);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //send Category Id and start new Activity
                        Intent serverfoodListIntent = new Intent(ServerHome.this,ServerFoodList.class);
                        serverfoodListIntent.putExtra("categoryId",adapter.getRef(position).getKey());
                        startActivity(serverfoodListIntent);
                    }
                });

/*                final Category clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
//                        Toast.makeText(home.this,""+clickItem.getName(),Toast.LENGTH_SHORT).show();
                        //Get categoryId and send to new Activity
                        Intent foodList = new Intent(ServerHome.this,FoodList.class);
                        foodList.putExtra("categoryId",adapter.getRef(position).getKey());
                        startActivity(foodList);


                    }
                });*/
            }
        };
        adapter.notifyDataSetChanged();
        recycler_menu.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.server_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            // Handle the camera action
        } else if (id == R.id.nav_reservation) {
            /*Intent reserveIntent = new Intent(home.this,Reservation.class);
            startActivity(reserveIntent);*/

        } else if (id == R.id.nav_cart) {
            /*Intent cartIntent = new Intent(home.this,Cart.class);
            startActivity(cartIntent);*/

        } else if (id == R.id.nav_orders) {
            Intent ServerorderIntent = new Intent(ServerHome.this,ServerDeliveryOrderStatus.class);
            startActivity(ServerorderIntent);

        } else if (id == R.id.nav_log_out) {
            //Log User out of account
            Intent signIn = new Intent(ServerHome.this,MainActivity.class);
            signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(signIn);
            System.exit(0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Update / Delete

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.UPDATE)){
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
        }
        else if(item.getTitle().equals("Delete")){
            deleteCategory(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
//            deleteCategory(adapter.getRef(item.getOrder()).getKey());

        }

        return super.onContextItemSelected(item);
    }

    private void showDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(ServerHome.this);
        alert.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        alert.setTitle("Add new category");
        alert.setMessage("Please fill in all fields :");
        LayoutInflater inflater = this.getLayoutInflater();
        View add_category = inflater.inflate(R.layout.add_new_menu_layout, null);

        edtAddNewCategoryName = add_category.findViewById(R.id.edtAddNewCategoryName);
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
                if(newCategory != null){
                    category.push().setValue(newCategory);
                    Snackbar.make(drawer, newCategory.getName() + " was added !", Snackbar.LENGTH_LONG).show();
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
                                    newCategory = new Category(
                                            edtAddNewCategoryName.getText().toString(),
                                            uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(ServerHome.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void showUpdateDialog(final String key, final Category item) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(ServerHome.this);
        alert.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        alert.setTitle("Update category");
        alert.setMessage("Please fill in all fields :");
        LayoutInflater inflater = this.getLayoutInflater();
        View add_category = inflater.inflate(R.layout.add_new_menu_layout, null);

        edtAddNewCategoryName = add_category.findViewById(R.id.edtAddNewCategoryName);
        btnBrowseFile = add_category.findViewById(R.id.btnBrowseFile);
        btnUpload = add_category.findViewById(R.id.btnUpload);
        showValid= add_category.findViewById(R.id.showValid);

        // Set the old name of category
        edtAddNewCategoryName.setText(item.getName());

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
                item.setName(edtAddNewCategoryName.getText().toString());
                category.child(key).setValue(item);
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

    private void changeImage(final Category item) {
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
                            Toast.makeText(ServerHome.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

/*    private void deleteCategory(String key, Category item) {


        DatabaseReference food = database.getReference("Category");
        Query foodByCategory = food.orderByChild("category_id").equalTo(key);

        foodByCategory.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    child.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        category.child(key).removeValue();
        Toast.makeText(this, item.getName() + " was deleted !", Toast.LENGTH_SHORT).show();
    }*/
//private void deleteCategory(String key) {
    private void deleteCategory(String key, Category item) {

        //get all food in that particular Category we are deleting
        DatabaseReference foods = database.getReference("Foods");
        Query foodsInCategory = foods.orderByChild("menuId").equalTo(key);
        foodsInCategory.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapShot:dataSnapshot.getChildren())
                {
                    postSnapShot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        category.child(key).removeValue();//
        Snackbar.make(drawer, item.getName() + " was deleted !", Snackbar.LENGTH_LONG).show();
}

}
