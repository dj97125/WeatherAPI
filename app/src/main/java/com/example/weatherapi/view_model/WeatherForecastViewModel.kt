package com.example.weatherapi.view_model

import androidx.lifecycle.ViewModel
import com.example.weatherapi.common.StateAction
import com.example.weatherapi.common.UNITS_DEFAULT
import com.example.weatherapi.common.ZIP_CODE_DEFAULT
import com.example.weatherapi.domain.Repository
import com.example.weatherapi.domain.WeatherDomain.WeatherDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.lang.Thread.State
import javax.inject.Inject

@HiltViewModel
class WeatherForecastViewModel @Inject constructor(
    private val repository: Repository,
    private val exceptionHandler: CoroutineExceptionHandler,
    private val coroutineScope: CoroutineScope
) : ViewModel() {

    private val _weatherResponse: MutableStateFlow<StateAction?> = MutableStateFlow(null)
    val weatherResponse: StateFlow<StateAction?> = _weatherResponse.asStateFlow()

    init {
        getWeather(ZIP_CODE_DEFAULT, UNITS_DEFAULT)
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
                                val code = stateAction.code
                                _weatherResponse.value = StateAction.Succes(response, code)
                            }

                            is StateAction.Error -> {
                                val retrievedFailure = stateAction.error
                                _weatherResponse.value = StateAction.Error(retrievedFailure)
                            }
                        }

                    }
                }
            }
        }
    }
}