package com.bugua.culweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017-10-14.
 */

public class HeWeather {
    public String status;
    public Aqi aqi;
    public Basic basic;
    public Now now;
    public Suggestion suggestion;
    @SerializedName("daily_forecast")
    public List<DailyForecast> dailyForecast;
    @SerializedName("hourly_forecast")
    public List<HourlyForecast> hourlyForecasts;

}
