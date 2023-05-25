package com.example.weatherapi.domain.WeatherDomain

data class WeatherDomain(
    var date: String?,
    val coord: CityCoordinatesDomain?,
    val weather: List<WeatherDescriptionDomain?>,
    val main: CityWeatherDomain?,
    val dt: Int?,
    val name: String?,
    val code: Int,
)

data class CityWeatherDomain(
    val temp: Double?,
    val temp_min: Double?,
    val temp_max: Double?,
)
data class WeatherDescriptionDomain(
    val id: Int?,
    val main: String?,
    val icon: String?,
)

data class CityCoordinatesDomain(
    val lon: Double?,
    val lat: Double?,
)