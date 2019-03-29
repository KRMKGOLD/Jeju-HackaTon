package com.example.hackatonproject.data;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface JejuService {
    @GET("/")
    Call<List<Repo>> listRepose();

    @POST("/cctv")
    Call<JsonObject> cctvData(@Body JsonObject jsonArray);

    @POST("/dick")
    Call<JsonObject> lightData(@Body JsonObject jsonArray);

}