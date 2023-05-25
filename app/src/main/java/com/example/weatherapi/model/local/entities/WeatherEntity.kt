package com.example.weatherapi.model.local.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var date: String?,
    @Embedded
    val coord: CityCoordinatesEntity?,
//    @Embedded
//    val weather: List<WeatherDescriptionEntity?>,
    @Embedded
    val main: CityWeatherEntity?,
    val dt: Int?,
    val name: String?,
)

data class CityWeatherEntity(val temp: Double?, val temp_min: Double?, val temp_max: Double?)
data class CityCoordinatesEntity(val lon: Double?,val lat: Double?)
data class WeatherDescriptionEntity(
    val id: Int?,
    val main: String?,
    val icon: String?,
)
