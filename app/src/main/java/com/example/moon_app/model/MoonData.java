package com.example.moon_app.model;

import com.google.gson.annotations.SerializedName;

public class MoonData {
    @SerializedName("phase")
    private String phase;
    
    @SerializedName("illumination")
    private double illumination;
    
    @SerializedName("age_days")
    private double ageDays;
    
    @SerializedName("moon_age")
    private double moonAge; // Sometimes called moon_age

    public String getPhase() {
        return phase;
    }

    public double getIllumination() {
        return illumination;
    }

    public double getAgeDays() {
        return ageDays != 0 ? ageDays : moonAge;
    }
}