package com.example.weatherapi.model.network


import com.example.weatherapi.common.END_POINT_FORECAST
import com.example.weatherapi.common.END_POINT_WEATHER
import com.example.weatherapi.common.PARAM_APPID
import com.example.weatherapi.common.PARAM_UNITS
import com.example.weatherapi.common.PARAM_ZIP
import com.example.weatherapi.common.TOKEN
import com.example.weatherapi.model.network.response.ForeCastResponse
import com.example.weatherapi.model.network.response.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET(END_POINT_WEATHER)
    suspend fun getWeather(
        @Query(PARAM_ZIP) zip: String,
        @Query(PARAM_UNITS) units: String,
        @Query(PARAM_APPID) token: String = TOKEN
    ): Response<WeatherResponse>

    @GET(END_POINT_FORECAST)
    suspend fun getForecast(
        @Query(PARAM_ZIP) zip: String,
        @Query(PARAM_UNITS) units: String,
        @Query(PARAM_APPID) token: String = TOKEN,
    ): Response<ForeCastResponse>

}