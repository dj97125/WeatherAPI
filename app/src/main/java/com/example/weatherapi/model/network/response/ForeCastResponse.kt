package com.example.weatherapi.model.network.response

data class ForeCastResponse(
    val list: List<ForeCast>?,
)

data class ForeCast(
    val dt: Int?,
    val main: Main?,
    val weather: List<Weather?>?,
    val dt_txt: String?
)

data class Main(
    val feels_like: Double?,
    val grnd_level: Int?,
    val humidity: Int?,
    val pressure: Int?,
    val sea_level: Int?,
    val temp: Double?,
    val temp_kf: Double?,
    val temp_max: Double?,
    val temp_min: Double?
)

data class Weather(
    val description: String?,
    val icon: String?,
    val id: Int?,
    val main: String?
)