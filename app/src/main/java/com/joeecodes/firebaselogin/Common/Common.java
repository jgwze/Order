package com.joeecodes.firebaselogin.Common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.joeecodes.firebaselogin.Model.DeliveryRequest;
import com.joeecodes.firebaselogin.Model.User;
import com.joeecodes.firebaselogin.Remote.GeoCoordinatesInter;
import com.joeecodes.firebaselogin.Remote.RetrofitClient;

public class Common {
    public static User currentUser;
    public static DeliveryRequest currentRequest;

    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";

    public static final  int PICK_IMAGE_REQUEST = 71;

    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "Password";

    public static final String baseUrl = "https://maps.googleapis.com";

    public static boolean IsConnectedToInternet(Context context)
    {
        ConnectivityManager connectivityManager =(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager!=null)
        {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if(info!=null)
            {
                for(int i=0;i<info.length;i++)
                {
                    if(info[i].getState()==NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        } return false;
    }

    public static String convertCodetoStatus(String status) {
        //this.status="0"; default is 0. 0:Placed/Awaiting Confirmation 1:Shipped/Reservation Placed

        if(status.equals("0"))
            return("Placed");
        else if(status.equals("1"))
            return "On the Way";
        else
            return "Shipped";
    }

    public static String convertCodetoPayment(String payment) {
        //this.status="0"; default is 0. 0:Placed/Awaiting Confirmation 1:Shipped/Reservation Placed

        if(payment.equals("0"))
            return("Awaiting Payment");
        else
            return "Paid";
    }

    public static GeoCoordinatesInter getGeoCodeServices()
    {
        return RetrofitClient.getClient(baseUrl).create(GeoCoordinatesInter.class);
    }
    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight){
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        float scaleX = newWidth / (float)bitmap.getWidth();
        float scaleY = newHeight / (float)bitmap.getHeight();
        float pivotX = 0, pivotY = 0;

        Matrix matrix = new Matrix();
        matrix.setScale(scaleX, scaleY, pivotX, pivotY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(matrix);
        canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));

        return  scaledBitmap;
    }
}
