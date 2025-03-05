package com.example.myapplication;

import java.util.ArrayList;

public class    Weather {
    String temp;
    String could;
    String mondayAvgTemp;
    String tuesdayAvgTemp;
    ArrayList<ArrayList<HourlyWeather>> hourlyWeatherData;

    public Weather() {
        hourlyWeatherData = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            hourlyWeatherData.add(new ArrayList<>());
        }
    }
}
