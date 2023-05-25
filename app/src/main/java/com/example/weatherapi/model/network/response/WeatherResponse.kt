package com.example.weatherapi.model.network.response

import com.example.weatherapi.domain.WeatherDomain.CityCoordinatesDomain
import com.example.weatherapi.domain.WeatherDomain.CityWeatherDomain
import com.example.weatherapi.domain.WeatherDomain.WeatherDescriptionDomain


data class WeatherResponse(
    val coord: CityCoordinates?,
    val weather: List<WeatherDescription?>,
    val main: CityWeather?,
    val dt: Int?,
    val name: String?,
    val code: Int,
)

//City
data class CityWeather(val temp: Double?, val temp_min: Double?, val temp_max: Double?)
data class CityCoordinates(val lon: Double?, val lat: Double?)
data class WeatherDescription(val id: Int?, val main: String?, val icon: String?)

