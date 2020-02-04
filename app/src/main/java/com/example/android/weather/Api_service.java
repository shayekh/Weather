package com.example.android.weather;

import com.example.android.weather.weather.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface Api_service {
    @GET()
    Call<WeatherResponse> getAllUser(@Url String url);
}
