package com.example.moon_app.model;

public class UpcomingPhase {
    private String name;
    private String date;
    private String daysLeft;

    public UpcomingPhase(String name, String date, String daysLeft) {
        this.name = name;
        this.date = date;
        this.daysLeft = daysLeft;
    }

    public String getName() { return name; }
    public String getDate() { return date; }
    public String getDaysLeft() { return daysLeft; }
}