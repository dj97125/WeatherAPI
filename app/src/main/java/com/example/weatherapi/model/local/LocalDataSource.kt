package com.example.weatherapi.model.local

import com.example.weatherapi.domain.WeatherDomain.CityCoordinatesDomain
import com.example.weatherapi.domain.WeatherDomain.CityWeatherDomain
import com.example.weatherapi.domain.WeatherDomain.WeatherDescriptionDomain
import com.example.weatherapi.domain.WeatherDomain.WeatherDomain
import com.example.weatherapi.model.local.entities.CityCoordinatesEntity
import com.example.weatherapi.model.local.entities.CityWeatherEntity
import com.example.weatherapi.model.local.entities.WeatherDescriptionEntity
import com.example.weatherapi.model.local.entities.WeatherEntity
import java.util.Calendar
import javax.inject.Inject


interface LocalDataSource {
    suspend fun insertTodaysWeather(weatherDomain: WeatherDomain)
    suspend fun deletePastWeather(date: String)
    suspend fun getTodaysWeather(): List<WeatherEntity>

}

class LocalDataSourceImpl @Inject constructor(
    private val weatherDao: WeatherDao
) : LocalDataSource {

    override suspend fun insertTodaysWeather(weatherDomain: WeatherDomain) {
        val currentDate = Calendar.getInstance().time.toString()
        weatherDao.insertTodaysWeather(weatherDomain.fromWeatherDomainToEntity(currentDate))
    }

    override suspend fun deletePastWeather(date: String) {
        weatherDao.deletePastWeather(date)
    }

    override suspend fun getTodaysWeather(): List<WeatherEntity> = weatherDao.getTodaysWeather()
}

private fun WeatherDomain.fromWeatherDomainToEntity(currentDate: String): WeatherEntity =
    WeatherEntity(
        id = 0,
        date = currentDate,
        coord?.fromCoordDomainToEntity(),
//        weather.fromWeatherDomainToEntity(),
        main?.fromCityWeatherDomainToEntity(),
        dt,
        name
    )

private fun CityCoordinatesDomain.fromCoordDomainToEntity(): CityCoordinatesEntity =
    CityCoordinatesEntity(lat, lon)

private fun List<WeatherDescriptionDomain?>.fromWeatherDomainToEntity(): List<WeatherDescriptionEntity?> =
    map { it?.fromWeatherDomainObjectToEntity() }

private fun WeatherDescriptionDomain.fromWeatherDomainObjectToEntity(): WeatherDescriptionEntity =
    WeatherDescriptionEntity(id, main, icon)

private fun CityWeatherDomain.fromCityWeatherDomainToEntity(): CityWeatherEntity =
    CityWeatherEntity(temp, temp_max, temp_min)