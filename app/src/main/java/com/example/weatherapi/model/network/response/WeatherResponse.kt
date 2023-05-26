package com.example.weatherapi.model.network.response


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

