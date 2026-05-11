package com.example.moon_app.model;

import com.google.gson.annotations.SerializedName;

public class MoonApiResponse {
    @SerializedName("data")
    private MoonData data;

    public MoonData getData() {
        return data;
    }
}