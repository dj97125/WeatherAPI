package com.example.weatherapi.model.network


import com.example.weatherapi.common.END_POINT_FORECAST
import com.example.weatherapi.common.END_POINT_GEO_CODE
import com.example.weatherapi.common.END_POINT_WEATHER
import com.example.weatherapi.common.LIMIT_VALUE
import com.example.weatherapi.common.PARAM_APPID
import com.example.weatherapi.common.PARAM_CITY_NAME
import com.example.weatherapi.common.PARAM_LAT
import com.example.weatherapi.common.PARAM_LIMIT
import com.example.weatherapi.common.PARAM_LON
import com.example.weatherapi.common.PARAM_UNITS
import com.example.weatherapi.common.TOKEN
import com.example.weatherapi.common.UNITS_DEFAULT
import com.example.weatherapi.model.network.response.ForeCastResponse
import com.example.weatherapi.model.network.response.GeoCodeResponse
import com.example.weatherapi.model.network.response.GeoCodeResponseItem
import com.example.weatherapi.model.network.response.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET(END_POINT_WEATHER)
    suspend fun getWeatherByCoord(
        @Query(PARAM_LAT) lat: String,
        @Query(PARAM_LON) lon: String,
        @Query(PARAM_UNITS) units: String = UNITS_DEFAULT,
        @Query(PARAM_APPID) token: String = TOKEN,
    ): Response<WeatherResponse>

    @GET(END_POINT_FORECAST)
    suspend fun getForecastByCoord(
        @Query(PARAM_LAT) lat: String,
        @Query(PARAM_LON) lon: String,
        @Query(PARAM_UNITS) units: String = UNITS_DEFAULT,
        @Query(PARAM_APPID) token: String = TOKEN,
    ): Response<ForeCastResponse>

    @GET(END_POINT_GEO_CODE)
    suspend fun getGeoCode(
        @Query(PARAM_CITY_NAME) city: String,
        @Query(PARAM_APPID) token: String = TOKEN,
    ): Response<GeoCodeResponse>

}