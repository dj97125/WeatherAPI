//package com.example.weatherapi.view_model
//
//
//import com.example.weatherapi.common.StateAction
//import com.example.weatherapi.domain.Repository
//import kotlinx.coroutines.CoroutineExceptionHandler
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.flow.StateFlow
//import org.junit.Before
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.mockito.Mock
//import org.mockito.junit.MockitoJUnitRunner
//
//import org.mockito.kotlin.*
//
//@RunWith(MockitoJUnitRunner::class)
//class WeatherForecastViewModelTest {
//
//    @Mock
//    private lateinit var repository: Repository
//
//    @Mock
//    private lateinit var exceptionHandler: CoroutineExceptionHandler
//
//    @Mock
//    private lateinit var coroutineScope: CoroutineScope
//
//    @Mock
//    private lateinit var weatherResponse: StateFlow<StateAction?>
//
//    @Mock
//    private lateinit var forecastResponse: StateFlow<StateAction?>
//
//    private lateinit var viewModel: WeatherForecastViewModel
//
//    @Before
//    fun setUp() {
//        viewModel = WeatherForecastViewModel(repository, exceptionHandler, coroutineScope)
//        viewModel.setWeatherResponse(weatherResponse)
//        viewModel.setForecastResponse(forecastResponse)
//    }
//
//    @Test
//    fun getWeather_success() {
//        // Given
//        val code = "US"
//        val unit = "F"
//        val response = WeatherDomain(...) // mock the response object
//        val httpCode = 200
//        val success = StateAction.Succes(response, httpCode)
//
//        // When
//        whenever(repository.getWeather(code, unit)).thenReturn(stateFlow)
//        whenever(stateFlow.collect(any())).thenReturn(success)
//        viewModel.getWeather(code, unit)
//
//        // Then
//        verify(weatherResponse).setValue(StateAction.Loading) // verify loading state is set
//        verify(weatherResponse).setValue(success) // verify success state is set with correct response and code
//    }
//
//    @Test
//    fun getWeather_error() {
//        // Given
//        val code = "US"
//        val unit = "F"
//        val failure = Failure(...) // mock the failure object
//        val error = StateAction.Error(failure)
//
//        // When
//        whenever(repository.getWeather(code, unit)).thenReturn(stateFlow)
//        whenever(stateFlow.collect(any())).thenReturn(error)
//        viewModel.getWeather(code, unit)
//
//        // Then
//        verify(weatherResponse).setValue(StateAction.Loading) // verify loading state is set
//        verify(weatherResponse).setValue(error) // verify error state is set with correct failure object
//    }
//
//    @Test
//    fun getForecast_success() {
//        // Given
//        val code = "US"
//        val unit = "F"
//        val response = ForeCastResponse(...) // mock the response object
//        val httpCode = 200
//        val success = StateAction.Succes(response, httpCode)
//
//        // When
//        whenever(repository.getForeCast(code, unit)).thenReturn(stateFlow)
//        whenever(stateFlow.collect(any())).thenReturn(success)
//        viewModel.getForecast(code, unit)
//
//        // Then
//        verify(forecastResponse).setValue(StateAction.Loading) // verify loading state is set
//        verify(forecastResponse).setValue(success) // verify success state is set with correct response and code
//    }
//
//    @Test
//    fun getForecast_error() {
//        // Given
//        val code = "US"
//        val unit = "F"
//        val failure = Failure(...) // mock the failure object
//        val error = StateAction.Error(failure)
//
//        // When
//        whenever(repository.getForeCast(code, unit)).thenReturn(stateFlow)
//        whenever(stateFlow.collect(any())).thenReturn(error)
//        viewModel.getForecast(code, unit)
//
//        // Then
//        verify(forecastResponse).setValue(StateAction.Loading) // verify loading state is set
//        verify(forecastResponse).setValue(error) // verify error state is set with correct failure object
//    }
//}
//
//
