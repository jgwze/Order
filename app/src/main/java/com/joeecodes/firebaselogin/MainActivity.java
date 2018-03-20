package com.joeecodes.firebaselogin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
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
import com.rey.material.widget.CheckBox;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {
    EditText edtPhone, edtPassword;
    Button btnSignIn,btnSignUp;
    CheckBox cbRemember;
    TextView txtSlogan;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        //Implement font before setContentView
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/BebasNeue Regular.otf").setFontAttrId(R.attr.fontPath).build());
        setContentView(R.layout.activity_main);

        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);
        edtPhone = (MaterialEditText) findViewById(R.id.edtPhone);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        txtSlogan = (TextView)findViewById(R.id.txtSlogan);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/Wonderbar Demo.otf");
        txtSlogan.setTypeface(face);
        cbRemember = (CheckBox) findViewById(R.id.cbRemember);

        //Init Paper
        Paper.init(this);

        printKeyHash();

        // Init Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Common.IsConnectedToInternet(getBaseContext())) {

                    //Remember User & Password Details
                    if(cbRemember.isChecked())
                    {
                        Paper.book().write(Common.USER_KEY,edtPhone.getText().toString());
                        Paper.book().write(Common.PWD_KEY,edtPassword.getText().toString());
                    }

                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
//                        if user exists
                            if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                                // Get Information
                                User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                                user.setPhone((edtPhone.getText().toString())); //set Phone
                                if ((user.getPassword().equals(edtPassword.getText().toString())) && (user.getStaffaccount().equals("false")))
                                {
                                    ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "Loading",
                                            "Welcome Back! Please Wait..");

                                    Intent homeIntent = new Intent(MainActivity.this, home.class);
                                    Common.currentUser = user;
                                    startActivity(homeIntent);
                                    finish();

                                } else if ((user.getPassword().equals(edtPassword.getText().toString())) && (user.getStaffaccount().equals("true"))) {
                                    ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "Loading", "Boss");

                                    Intent serverhomeIntent = new Intent(MainActivity.this, ServerHome.class);
                                    Common.currentUser = user; //send user info common.currentUser for later use
                                    startActivity(serverhomeIntent);
                                    finish();
                                } else {
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
                else { Toast.makeText(MainActivity.this, "Please Check Your Connection", Toast.LENGTH_SHORT).show();
                        return;
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SignUp = new Intent(MainActivity.this,SignUp.class);
                startActivity(SignUp);
            }
        });

        //Check if 'remember'ed
        String user=Paper.book().read(Common.USER_KEY);
        String pwd=Paper.book().read(Common.PWD_KEY);
        if(user!=null && pwd!=null)
        {
            if(!user.isEmpty()&&!pwd.isEmpty())
            {
                login(user,pwd);
            }
        }

    }

    private void printKeyHash() {
        try{
            PackageInfo info = getPackageManager().getPackageInfo("com.joeecodes.firebaselogin",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature:info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void login(final String phone, final String pwd) {
        // Init Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                        if user exists
                if (dataSnapshot.child(phone).exists()) {
                    // Get Information
                    User user = dataSnapshot.child(phone).getValue(User.class);
                    user.setPhone(phone); //set Phone
//                            Toast.makeText(MainActivity.this, ""+user.getPhone(), Toast.LENGTH_SHORT).show();
                    if ((user.getPassword().equals(pwd)) && (user.getStaffaccount().equals("false")))
//                            if (user.getPassword().equals(edtPassword.getText().toString()))
                    {
                        ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "Loading", "Please Wait..");
//                                Toast.makeText(MainActivity.this, "Sign in successfully !", Toast.LENGTH_SHORT).show();
//                                Toast.makeText(MainActivity.this, ""+user.getStaffaccount(), Toast.LENGTH_SHORT).show();

                        Intent homeIntent = new Intent(MainActivity.this, home.class);
                        Common.currentUser = user;
                        startActivity(homeIntent);
                        finish();

                    } else if ((user.getPassword().equals(edtPassword.getText().toString())) && (user.getStaffaccount().equals("true"))) {
                        ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "Loading", "Boss");
//                                Toast.makeText(MainActivity.this, "Sign in successfully !", Toast.LENGTH_SHORT).show();
//                                Toast.makeText(MainActivity.this, ""+user.getStaffaccount(), Toast.LENGTH_SHORT).show();

                        Intent serverhomeIntent = new Intent(MainActivity.this, ServerHome.class);
                        Common.currentUser = user; //send user info common.currentUser for later use
                        startActivity(serverhomeIntent);
                        finish();

                    } else {

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
}