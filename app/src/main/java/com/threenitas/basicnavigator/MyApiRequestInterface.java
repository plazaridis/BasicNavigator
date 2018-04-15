package com.threenitas.basicnavigator;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface MyApiRequestInterface {

    @GET("/maps/api/directions/json")
    public void getJson(@Query("origin") String origin, @Query("destination") String destination, Callback<DirectionResults> callback);}