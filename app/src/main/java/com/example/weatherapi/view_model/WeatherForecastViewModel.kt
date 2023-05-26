package com.example.weatherapi.view_model

import androidx.lifecycle.ViewModel
import com.example.weatherapi.common.StateAction
import com.example.weatherapi.common.UNITS_DEFAULT
import com.example.weatherapi.common.ZIP_CODE_DEFAULT
import com.example.weatherapi.domain.Repository
import com.example.weatherapi.domain.WeatherDomain.WeatherDomain
import com.example.weatherapi.model.network.response.ForeCastResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
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

    private val _weatherResponse: MutableStateFlow<StateAction?> = MutableStateFlow(null)
    val weatherResponse: StateFlow<StateAction?> = _weatherResponse.asStateFlow()

    private val _forecastResponse: MutableStateFlow<StateAction?> = MutableStateFlow(null)
    val forecastResponse: StateFlow<StateAction?> = _forecastResponse.asStateFlow()

    init {
        getWeather(ZIP_CODE_DEFAULT, UNITS_DEFAULT)
        getForecast(ZIP_CODE_DEFAULT, UNITS_DEFAULT)
    }

    fun getWeather(code: String, unit: String) {
        _weatherResponse.value = StateAction.Loading
        coroutineScope.launch(exceptionHandler) {
            supervisorScope {
                launch {
                    repository.getWeather(code = code, unit = unit).collect() { stateAction ->
                        when (stateAction) {
                            is StateAction.Succes<*> -> {
                                val response = stateAction.response as WeatherDomain
                                val httpCode = stateAction.code
                                _weatherResponse.value = StateAction.Succes(response, httpCode)
                            }

                            is StateAction.Error -> {
                                val retrievedFailure = stateAction.error
                                _weatherResponse.value = StateAction.Error(retrievedFailure)
                            }

                            else -> {}
                        }


                    }
                }
            }
        }
    }

    fun getForecast(code: String, unit: String) {
        _forecastResponse.value = StateAction.Loading
        coroutineScope.launch(exceptionHandler) {
            supervisorScope {
                launch {
                    repository.getForeCast(code = code, unit = unit).collect() { stateAction ->
                        when (stateAction) {
                            is StateAction.Succes<*> -> {

                                val response = stateAction.response as ForeCastResponse
                                val httpCode = stateAction.code

                                _forecastResponse.value =
                                    StateAction.Succes(response, httpCode)
                            }

                            is StateAction.Error -> {
                                val retrievedFailure = stateAction.error
                                _forecastResponse.value = StateAction.Error(retrievedFailure)
                            }

                            else -> {}
                        }

                    }
                }
            }
        }
    }
}