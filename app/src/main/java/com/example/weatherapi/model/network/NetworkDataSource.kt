package com.example.weatherapi.model.network

import com.example.weatherapi.common.Exception400
import com.example.weatherapi.common.FailedNetworkResponseException
import com.example.weatherapi.common.StateAction
import com.example.weatherapi.domain.WeatherDomain.CityCoordinatesDomain
import com.example.weatherapi.domain.WeatherDomain.CityWeatherDomain
import com.example.weatherapi.domain.WeatherDomain.WeatherDescriptionDomain
import com.example.weatherapi.domain.WeatherDomain.WeatherDomain
import com.example.weatherapi.model.network.response.CityCoordinates
import com.example.weatherapi.model.network.response.CityWeather
import com.example.weatherapi.model.network.response.WeatherDescription
import com.example.weatherapi.model.network.response.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


interface NetworkDataSource {
    fun getWeather(code: String, unit: String): Flow<StateAction>
    fun getForeCast(code: String, unit: String): Flow<StateAction>
}

class NetworkDataSourceImpl @Inject constructor(
    private val service: WeatherApi
) : NetworkDataSource {
    override fun getWeather(code: String, unit: String): Flow<StateAction> = flow {
        val response = service.getWeather(zip = code, units = unit)
        if (response.isSuccessful) {
            response.body()?.let {
                emit(StateAction.Succes(it.convertToDomainWeather(), response.code()))
            }
        } else {
            if (response.code().toString().startsWith("4")) {
                emit(StateAction.Error(Exception400()))
            } else {
                emit(StateAction.Error(FailedNetworkResponseException()))
            }

        }
    }

    override fun getForeCast(code: String, unit: String): Flow<StateAction> = flow {
        val response = service.getForecast(zip = code, units = unit)
        if (response.isSuccessful) {
            response.body()?.let {
                emit(StateAction.Succes(it, response.code()))
            }
        } else {
            if (response.code().toString().startsWith("4")) {
                emit(StateAction.Error(Exception400()))
            } else {
                emit(StateAction.Error(FailedNetworkResponseException()))
            }

        }
    }
}

private fun WeatherResponse.convertToDomainWeather(): WeatherDomain =
    WeatherDomain(
        date = "0",
        coord?.convertToCityCordinatesDomain(),
        weather.convertToWeatherDescriptionDomain(),
        main?.convertToCityWeatherDomain(),
        dt,
        name, code
    )

private fun CityCoordinates.convertToCityCordinatesDomain(): CityCoordinatesDomain =
    CityCoordinatesDomain(lat, lon)

private fun CityWeather.convertToCityWeatherDomain(): CityWeatherDomain =
    CityWeatherDomain(temp, temp_max, temp_min)

private fun List<WeatherDescription?>.convertToWeatherDescriptionDomain():
        List<WeatherDescriptionDomain?> = map { it?.convertToWeatherDescriptionObjectDomain() }

private fun WeatherDescription.convertToWeatherDescriptionObjectDomain(): WeatherDescriptionDomain =
    WeatherDescriptionDomain(id, main, icon)


