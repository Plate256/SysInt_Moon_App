package com.example.moon_app.network;

import com.example.moon_app.model.MoonApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MoonApiService {
    @GET("moon-phase")
    Call<MoonApiResponse> getMoonPhase(
        @Query("api_key") String apiKey,
        @Query("date") String date // Format YYYY-MM-DD
    );
}