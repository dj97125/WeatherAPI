package com.example.weatherapi.model.local.response

data class WeatherResponse(
    val coord: CityCoordinates?,
    val dt: Int?,
    val main: CityWeather?,
    val weather: List<WeatherDescription?>?,
)

data class WeatherDescription(
    val icon: String?,
    val id: Int?,
    val main: String?
)

data class CityWeather(
    val temp: Double?,
    val temp_max: Double?,
    val temp_min: Double?
)

data class CityCoordinates(
    val lat: Double?,
    val lon: Double?
)