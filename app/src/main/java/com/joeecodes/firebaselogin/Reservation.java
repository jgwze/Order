package com.joeecodes.firebaselogin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.joeecodes.firebaselogin.Model.Reserve;
import com.joeecodes.firebaselogin.Model.User;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Reservation extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {
    EditText edtPhone,edtName,edtPax,edtReservationTime;
    Calendar dateTime=Calendar.getInstance();
    Calendar calendar;
    int day,month,year,hour,minute;
    int dayFinal,monthFinal,yearFinal,hourFinal,minuteFinal;
    Button btnMakeReservation;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Implement font before setContentView
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/BebasNeue Regular.otf").setFontAttrId(R.attr.fontPath).build());
        setContentView(R.layout.activity_reservation);

        // Init Firebase to submit Reservations
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("Reservation");

        edtPhone = (MaterialEditText) findViewById(R.id.edtPhone);
        edtName = (MaterialEditText) findViewById(R.id.edtName);
        edtPax = (MaterialEditText) findViewById(R.id.edtPax);
        edtReservationTime = (MaterialEditText) findViewById(R.id.edtReservationTime);
        btnMakeReservation = (Button) findViewById(R.id.btnMakeReservation);

        btnMakeReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String ReservationTimeSentFirebase = String.format("%02d-%02d-%04d %02d:%02d",dayFinal,monthFinal,yearFinal,hourFinal,minuteFinal);

                        /*if(dataSnapshot.child(edtReservationTime.getText().toString()).exists())
                        {
                            Toast.makeText(Reservation.this,"Same Reservation has been made",Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            Reserve reserve = new Reserve(edtPhone.getText().toString(),
                            edtName.getText().toString(),
                            edtPax.getText().toString());
                            table_user.child(edtReservationTime.getText().toString()).setValue(reserve);
                            Toast.makeText(Reservation.this,"Reservation done!", Toast.LENGTH_SHORT).show();
                            finish();
                        }*/

                        Reserve reserve = new Reserve(edtPhone.getText().toString(),
                                edtName.getText().toString(),
                                edtPax.getText().toString());
                        table_user.child(ReservationTimeSentFirebase).setValue(reserve);
                        Toast.makeText(Reservation.this,"Reservation made for "+ReservationTimeSentFirebase, Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    public void SelectReservationTime(View view) {
        Calendar c = Calendar.getInstance();
        year=c.get(Calendar.YEAR);
        month=c.get(Calendar.MONTH);
        day=c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Reservation.this,Reservation.this,year,month,day);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        yearFinal=year;
        monthFinal=month+1;
        dayFinal=dayOfMonth;

        Calendar c = Calendar.getInstance();
        hour=c.get(Calendar.HOUR_OF_DAY);
        minute=c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(Reservation.this,Reservation.this,hour,minute, android.text.format.DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        hourFinal=hourOfDay;
        minuteFinal=minute;

        String selectedTime = String.format("%02d:%02d",hourFinal,minuteFinal);

        edtReservationTime.setText(""+dayFinal+"/"+monthFinal+"/"+yearFinal+" "+selectedTime);
    }


/*
        Toast.makeText(Reservation.this, "Select a time!", Toast.LENGTH_SHORT).show();
*/

}
