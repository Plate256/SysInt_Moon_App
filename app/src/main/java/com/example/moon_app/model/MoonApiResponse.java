package com.example.moon_app.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

public class MoonApiResponse {
    @SerializedName("phase")
    private PhaseInfo phase;
    
    @SerializedName("zodiac")
    private ZodiacInfo zodiac;

    @SerializedName("interpretation")
    private InterpretationInfo interpretation;

    @SerializedName("special_moon")
    private SpecialInfo special;

    @SerializedName("forecast")
    private ForecastInfo forecast;

    @SerializedName("next_phases")
    private Map<String, String> nextPhases;

    public PhaseInfo getPhase() {
        return phase;
    }

    public ZodiacInfo getZodiac() {
        return zodiac;
    }

    public InterpretationInfo getInterpretation() {
        return interpretation;
    }

    public SpecialInfo getSpecial() {
        return special;
    }

    public ForecastInfo getForecast() {
        return forecast;
    }

    public Map<String, String> getNextPhases() {
        return nextPhases;
    }

    public static class PhaseInfo {
        @SerializedName("name")
        private String name;
        
        @SerializedName("illumination")
        private double illumination;
        
        @SerializedName("age_days")
        private double ageDays;
        
        @SerializedName("distance_km")
        private double distanceKm;

        public String getName() { return name; }
        public double getIllumination() { return illumination; }
        public double getAgeDays() { return ageDays; }
        public double getDistanceKm() { return distanceKm; }
    }

    public static class ZodiacInfo {
        @SerializedName("sign")
        private String sign;

        public String getSign() { return sign; }
    }

    public static class InterpretationInfo {
        @SerializedName("title")
        private String title;
        
        @SerializedName("body")
        private String body;

        public String getTitle() { return title; }
        public String getBody() { return body; }
    }

    public static class SpecialInfo {
        @SerializedName("is_supermoon")
        private boolean isSupermoon;
        @SerializedName("is_micromoon")
        private boolean isMicromoon;
        @SerializedName("is_blue_moon")
        private boolean isBlueMoon;
        @SerializedName("is_black_moon")
        private boolean isBlackMoon;
        @SerializedName("labels")
        private List<String> labels;

        public boolean isSupermoon() { return isSupermoon; }
        public boolean isMicromoon() { return isMicromoon; }
        public boolean isBlueMoon() { return isBlueMoon; }
        public boolean isBlackMoon() { return isBlackMoon; }
        public List<String> getLabels() { return labels; }
    }

    public static class ForecastInfo {
        @SerializedName("days_until_full_moon")
        private double daysUntilFullMoon;
        @SerializedName("days_until_new_moon")
        private double daysUntilNewMoon;
        @SerializedName("next_special_moon")
        private NextSpecialMoon nextSpecialMoon;
        @SerializedName("next_eclipse")
        private NextEclipse nextEclipse;

        public double getDaysUntilFullMoon() { return daysUntilFullMoon; }
        public double getDaysUntilNewMoon() { return daysUntilNewMoon; }
        public NextSpecialMoon getNextSpecialMoon() { return nextSpecialMoon; }
        public NextEclipse getNextEclipse() { return nextEclipse; }

        public static class NextSpecialMoon {
            @SerializedName("type")
            private String type;
            @SerializedName("days_until")
            private double daysUntil;

            public String getType() { return type; }
            public double getDaysUntil() { return daysUntil; }
        }

        public static class NextEclipse {
            @SerializedName("type")
            private String type;
            @SerializedName("days_until")
            private double daysUntil;

            public String getType() { return type; }
            public double getDaysUntil() { return daysUntil; }
        }
    }
}
