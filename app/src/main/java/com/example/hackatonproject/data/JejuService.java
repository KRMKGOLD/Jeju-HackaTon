package com.example.hackatonproject.data;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface JejuService {
    @GET("/cctv")
    Call<List<Repo>> sendCCTVRequest();

    @GET("/lamp")
    Call<List<Repo>> sendLightRequest();

    @POST("/cctv")
    Call<JsonObject> cctvData(@Body JsonObject jsonArray);

    @POST("/lamp")
    Call<JsonObject> lightData(@Body JsonObject jsonArray);

}