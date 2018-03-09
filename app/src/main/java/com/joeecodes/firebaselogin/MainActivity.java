package com.joeecodes.firebaselogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.joeecodes.firebaselogin.Common.Common;
import com.joeecodes.firebaselogin.Model.User;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {
    EditText edtPhone, edtPassword;
    Button btnSignIn,btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);
        edtPhone = (MaterialEditText) findViewById(R.id.edtPhone);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        // Init Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if user exists
                        if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                            // Get Information
                            User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                            user.setPhone((edtPhone.getText().toString())); //set Phone
//                            Toast.makeText(MainActivity.this, ""+user.getPhone(), Toast.LENGTH_SHORT).show();
                            if ((user.getPassword().equals(edtPassword.getText().toString()))&&(user.getStaffaccount().equals("false")))
//                            if (user.getPassword().equals(edtPassword.getText().toString()))
                            {
                                ProgressDialog dialog = ProgressDialog.show(MainActivity.this,"Loading","Please Wait..");
//                                Toast.makeText(MainActivity.this, "Sign in successfully !", Toast.LENGTH_SHORT).show();
//                                Toast.makeText(MainActivity.this, ""+user.getStaffaccount(), Toast.LENGTH_SHORT).show();

                                Intent homeIntent = new Intent(MainActivity.this,home.class);
                                Common.currentUser = user;
                                startActivity(homeIntent);
                                finish();

                            }

                            else if ((user.getPassword().equals(edtPassword.getText().toString()))&&(user.getStaffaccount().equals("true")))
                            {
                                ProgressDialog dialog = ProgressDialog.show(MainActivity.this,"Loading","Boss");
//                                Toast.makeText(MainActivity.this, "Sign in successfully !", Toast.LENGTH_SHORT).show();
//                                Toast.makeText(MainActivity.this, ""+user.getStaffaccount(), Toast.LENGTH_SHORT).show();

                                Intent serverhomeIntent = new Intent(MainActivity.this,ServerHome.class);
                                Common.currentUser = user; //send user info common.currentUser for later use
                                startActivity(serverhomeIntent);
                                finish();

                            }

                            else {

                                Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(MainActivity.this, "User does not exist in Database", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SignUp = new Intent(MainActivity.this,SignUp.class);
                startActivity(SignUp);
            }
        });
    }
}