package com.example.weatherapi.view_model

import androidx.lifecycle.ViewModel
import com.example.weatherapi.common.StateAction
import com.example.weatherapi.domain.Repository
import com.example.weatherapi.model.network.response.GeoCodeResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

@HiltViewModel
class WeatherForecastViewModel @Inject constructor(
    private val repository: Repository,
    private val exceptionHandler: CoroutineExceptionHandler,
    private val coroutineScope: CoroutineScope
) : ViewModel() {

    private val _weatherResponse: MutableStateFlow<StateAction?> =
        MutableStateFlow(StateAction.Loading)
    val weatherResponse: StateFlow<StateAction?> = _weatherResponse.asStateFlow()

    private val _forecastResponse: MutableStateFlow<StateAction?> =
        MutableStateFlow(StateAction.Loading)
    val forecastResponse: StateFlow<StateAction?> = _forecastResponse.asStateFlow()

    var lat: String? = null
    var lon: String? = null


    fun getGeoCode(city: String) {
        coroutineScope.launch(exceptionHandler) {
            supervisorScope {
                launch {
                    repository.getGeoCode(cityName = city).collect { stateAction ->
                        when (stateAction) {
                            is StateAction.Succes<*> -> {
                                val response = stateAction.response as GeoCodeResponse

                                getWeatherByCoord(
                                    lat = response.firstOrNull()?.lat.toString(),
                                    long = response.firstOrNull()?.lon.toString()
                                )

                            }

                            else -> {}
                        }


                    }
                }
            }
        }
    }

    fun getWeatherByCoord(lat: String, long: String) {
        coroutineScope.launch(exceptionHandler) {
            supervisorScope {
                val differed = listOf(
                    async {
                        repository.getWeatherByCoord(lat = lat, lon = long).collect { stateAction ->
                            _weatherResponse.value = stateAction
                        }
                    },
                    async {
                        repository.getForecastByCoord(lat = lat, lon = long)
                            .collect { stateAction ->
                                _forecastResponse.value = stateAction
                            }
                    }
                )
                differed.awaitAll()
            }
        }
    }

}