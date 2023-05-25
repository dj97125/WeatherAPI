package com.example.weatherapi.domain

import com.example.weatherapi.common.InternetCheck
import com.example.weatherapi.common.StateAction
import com.example.weatherapi.domain.WeatherDomain.WeatherDomain
import com.example.weatherapi.model.local.LocalDataSource
import com.example.weatherapi.model.network.NetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface Repository {
    fun getWeather(code: String, unit: String): Flow<StateAction>
}

class RepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource, private val localDataSource: LocalDataSource
) : Repository {
    override fun getWeather(code: String, unit: String): Flow<StateAction> = flow {
        val connected = InternetCheck()
        val remoteService = networkDataSource.getWeather(code = code, unit = unit)

        if (connected.isConnected()) {
            remoteService.collect { stateAction ->
                when (stateAction) {
                    is StateAction.Succes<*> -> {
                        val retrievedUnitInfo = stateAction.response as WeatherDomain
                        emit(StateAction.Succes(retrievedUnitInfo, stateAction.code))
                        localDataSource.insertTodaysWeather(retrievedUnitInfo)
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

}