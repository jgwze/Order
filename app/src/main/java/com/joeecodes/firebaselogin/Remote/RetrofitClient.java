package com.joeecodes.firebaselogin.Remote;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Lenovo on 16/3/2018.
 */

public class RetrofitClient {

    private static Retrofit retrofit =null;

        public static Retrofit getClient(String baseUrl){
            if(retrofit==null)
            {
                retrofit=new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .build();
                /*retrofit= new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();*/
            }
            return retrofit;
    }


/*    public static Retrofit getMapClient(String baseUrl){
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }*/
}
