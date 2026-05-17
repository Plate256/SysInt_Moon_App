package com.example.moon_app.network;

import com.example.moon_app.model.MoonApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface MoonApiService {
    @GET("moon/phase")
    Call<MoonApiResponse> getMoonData(
        @Header("x-api-key") String apiKey,
        @Query("date") String date,
        @Query("lat") double lat,
        @Query("lon") double lon,
        @Query("include_zodiac") boolean includeZodiac,
        @Query("include_interpretation") boolean includeInterpretation,
        @Query("include_special") boolean includeSpecial,
        @Query("include_forecast") boolean includeForecast
    );
}