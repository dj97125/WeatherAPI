package com.example.weatherapi.model.network


import com.example.weatherapi.common.END_POINT_FORECAST
import com.example.weatherapi.common.END_POINT_WEATHER
import com.example.weatherapi.common.PARAM_APPID
import com.example.weatherapi.common.PARAM_UNITS
import com.example.weatherapi.common.PARAM_ZIP
import com.example.weatherapi.model.local.response.ForeCastResponse
import com.example.weatherapi.model.local.response.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET(END_POINT_WEATHER)
    fun getWeather(
        @Query(PARAM_ZIP) zip: String,
        @Query(PARAM_APPID) units: String,
        @Query(PARAM_UNITS) token: String
    ): Call<WeatherResponse>

    @GET(END_POINT_FORECAST)
    fun getCityWeatherDetails(
        @Query(PARAM_ZIP) zip: String,
        @Query(PARAM_APPID) token: String,
        @Query(PARAM_UNITS) units: String
    ): Call<ForeCastResponse>

}