package com.example.weatherapi.view_model


import app.cash.turbine.test
import com.example.weatherapi.common.FailedNetworkResponseException
import com.example.weatherapi.common.StateAction
import com.example.weatherapi.domain.Repository
import com.example.weatherapi.domain.WeatherDomain.WeatherDomain
import com.example.weatherapi.fakes.FakeGeoCodeResponseItem
import com.example.weatherapi.model.network.response.ForeCastResponse
import com.example.weatherapi.utilities.CoroutinesTestRule
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*
import org.mockito.Mockito.*


@OptIn(ExperimentalCoroutinesApi::class)
class WeatherForecastViewModelTest {

    @get:Rule
    var rule = CoroutinesTestRule()


    private lateinit var viewModel: WeatherForecastViewModel
    lateinit var repository: Repository
    private val handler = CoroutineExceptionHandler { coroutineContext, throwable -> }

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScopeCoroutine = TestCoroutineScope(testDispatcher)

    @Before
    fun setUpTest() {
        repository = mockk()
        viewModel = WeatherForecastViewModel(repository, handler, testScopeCoroutine)
    }

    @After
    fun cleanup() {
        unmockkAll()
        testDispatcher.cancel()
    }


    @Test
    fun `test everything works`() = runTest(testDispatcher) {
        testDispatcher.scheduler.advanceTimeBy(2000L)
        assertTrue(true)
    }


    @Test
    fun `getGeoCode should call repository and update weatherResponse and forecastResponse with a SUCCES call`() =
        runTest(testDispatcher) {
            /**
             * Given
             */
            val city = FakeGeoCodeResponseItem.name.toString()
            val lon = FakeGeoCodeResponseItem.lon.toString()
            val lat = FakeGeoCodeResponseItem.lat.toString()
            val geoCodeResponse = listOf(FakeGeoCodeResponseItem)
            val weatherResponse = mockk<WeatherDomain>(relaxed = true)
            val forecastResponse = mockk<ForeCastResponse>(relaxed = true)

            coEvery { repository.getGeoCode(city) } returns flowOf(
                StateAction.Succes(
                    geoCodeResponse,
                    200
                )
            )
            coEvery { repository.getWeatherByCoord(lat = lat, lon = lon) } returns flowOf(
                StateAction.Succes(
                    weatherResponse,
                    200
                )
            )
            coEvery { repository.getForecastByCoord(lat = lat, lon = lon) } returns flowOf(
                StateAction.Succes(
                    forecastResponse,
                    200
                )
            )
            val stateActionList = mutableListOf<StateAction?>()
            /**
             * When
             */
            viewModel.getGeoCode(city)


            viewModel.getWeatherByCoord(
                geoCodeResponse.first().lat.toString(),
                geoCodeResponse.first().lon.toString()
            )



            viewModel.getForecastByCoord(
                geoCodeResponse.first().lat.toString(),
                geoCodeResponse.first().lon.toString()
            )

            /**
             * Then
             */
            viewModel.weatherResponse.test {
                stateActionList.add(awaitItem())
                val response = stateActionList.first() as StateAction.Succes<*>
                assertEquals(1, stateActionList.size)
                assertEquals(200, response.code)
                stateActionList.clear()
                cancel()
            }

            viewModel.forecastResponse.test {
                stateActionList.add(awaitItem())
                val response = stateActionList.first() as StateAction.Succes<*>
                assertEquals(1, stateActionList.size)
                assertEquals(200, response.code)
                stateActionList.clear()
                cancel()
            }


            // Verify the expected calls to repository
            coVerifySequence {
                repository.getGeoCode(city)
                repository.getWeatherByCoord(
                    geoCodeResponse.first().lat.toString(),
                    geoCodeResponse.first().lon.toString()
                )
                repository.getForecastByCoord(
                    geoCodeResponse.first().lat.toString(),
                    geoCodeResponse.first().lon.toString()
                )
            }

        }

    @Test
    fun `getGeoCode should call repository and not update weatherResponse and forecastResponse with an ERROR call`() =
        runTest(testDispatcher) {
            /**
             * Given
             */
            val city = FakeGeoCodeResponseItem.name.toString()
            val geoCodeResponse = listOf(FakeGeoCodeResponseItem)
            val exception = FailedNetworkResponseException()

            coEvery { repository.getGeoCode(city) } returns flowOf(
                StateAction.Error(
                    exception
                )
            )

            /**
             * When
             */
            viewModel.getGeoCode(city)

            viewModel.getWeatherByCoord(
                geoCodeResponse.first().lat.toString(),
                geoCodeResponse.first().lon.toString()
            )



            viewModel.getForecastByCoord(
                geoCodeResponse.first().lat.toString(),
                geoCodeResponse.first().lon.toString()
            )

            /**
             * Then
             */
            viewModel.weatherResponse.test() {
                assertEquals(StateAction.Loading, awaitItem())
                cancel()
            }

            viewModel.forecastResponse.test() {
                assertEquals(StateAction.Loading, awaitItem())
                cancel()
            }


            // Verify the expected calls to repository
            coVerifySequence {
                repository.getGeoCode(city)
                repository.getWeatherByCoord(
                    geoCodeResponse.first().lat.toString(),
                    geoCodeResponse.first().lon.toString()
                )
                repository.getForecastByCoord(
                    geoCodeResponse.first().lat.toString(),
                    geoCodeResponse.first().lon.toString()
                )
            }

        }

    @Test
    fun `getWeatherByCoord should call repository and update weatherResponse with an ERROR state`() =
        runTest(testDispatcher) {
            /**
             * Given
             */
            val geoCodeResponse = listOf(FakeGeoCodeResponseItem)
            val exception = FailedNetworkResponseException()

            coEvery {
                repository.getWeatherByCoord(
                    lat = geoCodeResponse.first().lat.toString(),
                    lon = geoCodeResponse.first().lon.toString()
                )
            } returns flowOf(
                StateAction.Error(
                    exception
                )
            )

            /**
             * When
             */
            viewModel.getWeatherByCoord(
                geoCodeResponse.first().lat.toString(),
                geoCodeResponse.first().lon.toString()
            )

            /**
             * Then
             */
            viewModel.weatherResponse.test() {
                val item = awaitItem()
                assertTrue(item is StateAction.Error)
                assertEquals(exception.message, (item as StateAction.Error).error.message)
                cancel()
            }


            coVerify {
                repository.getWeatherByCoord(
                    geoCodeResponse.first().lat.toString(),
                    geoCodeResponse.first().lon.toString()
                )
            }

        }

    @Test
    fun `getForecastByCoord should call repository and update forecastResponse with an ERROR state`() =
        runTest(testDispatcher) {
            /**
             * Given
             */
            val geoCodeResponse = listOf(FakeGeoCodeResponseItem)
            val exception = FailedNetworkResponseException()

            coEvery {
                repository.getForecastByCoord(
                    lat = geoCodeResponse.first().lat.toString(),
                    lon = geoCodeResponse.first().lon.toString()
                )
            } returns flowOf(
                StateAction.Error(
                    exception
                )
            )
            /**
             * When
             */
            viewModel.getForecastByCoord(
                geoCodeResponse.first().lat.toString(),
                geoCodeResponse.first().lon.toString()
            )

            /**
             * Then
             */
            viewModel.forecastResponse.test() {
                val item = awaitItem()
                assertTrue(item is StateAction.Error)
                assertEquals(exception.message, (item as StateAction.Error).error.message)
                cancel()
            }


            coVerify {
                repository.getForecastByCoord(
                    geoCodeResponse.first().lat.toString(),
                    geoCodeResponse.first().lon.toString()
                )
            }

        }


}