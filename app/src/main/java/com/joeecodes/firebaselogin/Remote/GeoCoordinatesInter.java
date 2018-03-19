package com.joeecodes.firebaselogin.Remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Lenovo on 16/3/2018.
 */

public interface GeoCoordinatesInter {
    @GET("maps/api/geocode/json")
    Call<String> getGeocode(@Query("address") String address);

    @GET("maps/api/directions/json")
    Call<String> getDirection(@Query("origin") String origin, @Query("destination") String destination);
}
