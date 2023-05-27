package com.example.weatherapi.domain

import com.example.weatherapi.common.InternetCheck
import com.example.weatherapi.common.StateAction
import com.example.weatherapi.domain.WeatherDomain.WeatherDomain
import com.example.weatherapi.model.local.LocalDataSource
import com.example.weatherapi.model.network.NetworkDataSource
import com.example.weatherapi.model.network.response.ForeCastResponse
import com.example.weatherapi.model.network.response.GeoCodeResponse
import com.example.weatherapi.model.network.response.GeoCodeResponseItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


/*
     if i would have more time:

   - id like to implement the users location
   - id like to use my persist information in room for fetching the las infomation
   in my entity and make a backend petition
   - id like to implement workManager for every day while having wifi conection just
   keep the information for todays date and errase the prev day or prev days
   - id like testing my view model and my corroutines using mokito and junit4 for my
   corroutines
   - in my view i got an settings section while id like the customer choose between
   differentes degress (K,F,C)
   - id like to implement the geo code API for let the user do a research just for city
 */
interface Repository {
    fun getGeoCode(cityName: String): Flow<StateAction>
    fun getWeatherByCoord(lat: String, lon: String): Flow<StateAction>
    fun getForecastByCoord(lat: String, lon: String): Flow<StateAction>

}

class RepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource, private val localDataSource: LocalDataSource
) : Repository {
    override fun getGeoCode(cityName: String): Flow<StateAction> = flow {
        val connected = InternetCheck()
        val remoteService = networkDataSource.getGeoCode(cityName = cityName)

        if (connected.isConnected()) {
            remoteService.collect { stateAction ->
                when (stateAction) {
                    is StateAction.Succes<*> -> {

                        val retrievedUnitInfo = stateAction.response as GeoCodeResponse
                        emit(StateAction.Succes(retrievedUnitInfo, stateAction.code))
                    }

                    is StateAction.Error -> {
                        emit(StateAction.Error(stateAction.error))
                    }

                    else -> {}
                }

            }
        } else {
            val cache = localDataSource.getTodaysWeather()
            emit(StateAction.Succes(cache, 200))
        }
    }

    override fun getWeatherByCoord(lat: String, lon: String): Flow<StateAction> = flow {
        val connected = InternetCheck()
        val remoteService = networkDataSource.getWeatherByCoord(lat = lat, lon = lon)

        if (connected.isConnected()) {
            remoteService.collect { stateAction ->
                when (stateAction) {
                    is StateAction.Succes<*> -> {
                        val response = stateAction.response as WeatherDomain
                        emit(StateAction.Succes(response, stateAction.code))
                    }

                    is StateAction.Error -> {
                        emit(StateAction.Error(stateAction.error))
                    }

                    else -> {}
                }

            }
        }
    }

    override fun getForecastByCoord(lat: String, lon: String): Flow<StateAction> = flow {
        val connected = InternetCheck()
        val remoteService = networkDataSource.getForecastByCoord(lat = lat, lon = lon)

        if (connected.isConnected()) {
            remoteService.collect { stateAction ->
                when (stateAction) {
                    is StateAction.Succes<*> -> {
                        val response = stateAction.response as ForeCastResponse
                        emit(StateAction.Succes(response, stateAction.code))
                    }

                    is StateAction.Error -> {
                        emit(StateAction.Error(stateAction.error))
                    }

                    else -> {}
                }

            }
        }
    }

}