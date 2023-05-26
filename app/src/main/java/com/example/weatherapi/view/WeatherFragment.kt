package com.example.weatherapi.view

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.weatherapi.common.BaseFragment
import com.example.weatherapi.common.IMAGES_URL
import com.example.weatherapi.common.IMAGE_FORMAT
import com.example.weatherapi.common.StateAction
import com.example.weatherapi.common.UNITS_DEFAULT
import com.example.weatherapi.common.ZIP_CODE_DEFAULT
import com.example.weatherapi.common.getTemperatureFormat
import com.example.weatherapi.common.toast
import com.example.weatherapi.databinding.FragmentWeatherBinding
import com.example.weatherapi.domain.WeatherDomain.WeatherDomain
import com.example.weatherapi.model.network.response.ForeCast
import com.example.weatherapi.model.network.response.ForeCastResponse
import com.example.weatherapi.view.adapters.ForeCastAdapter
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class WeatherFragment : BaseFragment() {

    private var units: String = UNITS_DEFAULT
    private var zipCode: String = ZIP_CODE_DEFAULT
    private var isMessage: Boolean = false

    private val binding by lazy {
        FragmentWeatherBinding.inflate(layoutInflater)
    }

    private val forecastAdapter by lazy {
        ForeCastAdapter()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initObservers()


        binding.apply {
            imVSearch.setOnClickListener {
                isMessage = true
                zipCode = binding.eTZipCode.text.toString()
                weatherForecastViewModel.getWeather(code = zipCode, unit = units)
                initObservers()
            }

            rvForecastTomorrow.adapter = forecastAdapter
            rvWeatherToday.adapter = forecastAdapter


            return root
        }
    }

    override fun onResume() {
        super.onResume()

        binding.swipeRefresh.apply {
            setColorSchemeResources(
                android.R.color.holo_blue_dark,
                android.R.color.holo_purple,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_bright,
                android.R.color.holo_red_light,
                android.R.color.holo_green_dark,
            )
            setOnRefreshListener {

                weatherForecastViewModel.getWeather(code = zipCode, unit = units)
                weatherForecastViewModel.getForecast(code = zipCode, unit = units)
                isRefreshing = false
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initObservers() {

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val tomorrowsDate = LocalDateTime.now().plusDays(1)
        val dayAfterTomorrowDate = LocalDateTime.now().plusDays(2)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                weatherForecastViewModel.weatherResponse.collect { stateAction ->
                    when (stateAction) {
                        is StateAction.Succes<*> -> {
                            val retrievedInfo = stateAction.response as WeatherDomain
                            binding.apply {
                                tvCity.text = retrievedInfo.name
                                tvTemperature.text =
                                    retrievedInfo.main?.temp?.getTemperatureFormat(units)
                                tvDescription.text = retrievedInfo.weather.first()?.main
                                Picasso.get()
                                    .load(IMAGES_URL + retrievedInfo.weather.first()?.icon + IMAGE_FORMAT)
                                    .into(imVCurrentWeather)
                            }
                        }

                        is StateAction.Error -> {
                            val msj = stateAction.error.message.toString()

                            if (isMessage && msj.isNotEmpty()) {
                                isMessage = activity?.toast(msj) == true

                            }
                        }

                        else -> {}
                    }
                }
            }


        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                weatherForecastViewModel.forecastResponse.collect { stateAction ->
                    when (stateAction) {
                        is StateAction.Succes<*> -> {
                            val response = stateAction.response as ForeCastResponse
                            val makingForecastList = mutableListOf<ForeCast>()

                            binding.apply {
                                forecastInfo.isVisible = true
                            }

                            response.list?.filter { forecastFilter ->
                                forecastFilter.dt_txt?.substring(
                                    0,
                                    10
                                ) == dayAfterTomorrowDate.format(formatter)
                            }?.forEach { forecast ->
                                makingForecastList.add(forecast)
                            }
                            forecastAdapter.updateData(makingForecastList)
                            makingForecastList.clear()

                            response.list?.filter { forecastFilter ->
                                forecastFilter.dt_txt?.substring(0, 10) == tomorrowsDate.format(
                                    formatter
                                )
                            }?.forEach { forecast ->
                                makingForecastList.add(forecast)
                            }
                            forecastAdapter.updateData(makingForecastList)
                            makingForecastList.clear()


                        }

                        is StateAction.Error -> {
                            val msj = stateAction.error.message.toString()

                            if (isMessage && msj.isNotEmpty()) {
                                isMessage = activity?.toast(msj) == true

                            }

                            binding.apply {
                                forecastInfo.isVisible = false
                            }
                        }

                        else -> {}
                    }
                }
            }
        }

    }
}


