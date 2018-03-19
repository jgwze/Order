package com.joeecodes.firebaselogin.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.joeecodes.firebaselogin.Common.Common;
import com.joeecodes.firebaselogin.DeliveryOrderStatus;
import com.joeecodes.firebaselogin.Model.DeliveryRequest;
import com.joeecodes.firebaselogin.R;

public class ListentoOrder extends Service implements ChildEventListener {
    FirebaseDatabase db;
    DatabaseReference deliveryrequests;
    public ListentoOrder() {
    }

    @Override
    public IBinder onBind(Intent intent) {return null;}

    @Override
    public void onCreate() {
        super.onCreate();
        db = FirebaseDatabase.getInstance();
        deliveryrequests = db.getReference("DeliveryRequests");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        deliveryrequests.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        DeliveryRequest deliveryRequest =dataSnapshot.getValue(DeliveryRequest.class);
        showNotification(dataSnapshot.getKey(),deliveryRequest);
    }

    private void showNotification(String key, DeliveryRequest deliveryRequest) {
        Intent intent = new Intent(getBaseContext(), DeliveryOrderStatus.class);
        intent.putExtra("userPhone",deliveryRequest.getPhone());
        PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL).setWhen(System.currentTimeMillis())
                .setTicker("Joee")
                .setContentInfo("Your order is updated")
                .setContentText("Order #"+key+"status is updated to "+ Common.convertCodetoStatus(deliveryRequest.getStatus()))
                .setContentIntent(contentIntent)
                .setContentInfo("Info")
                .setSmallIcon(R.mipmap.ic_launcher);

        NotificationManager notificationManager = (NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
