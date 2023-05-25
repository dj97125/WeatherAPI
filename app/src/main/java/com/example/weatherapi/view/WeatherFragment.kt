package com.example.weatherapi.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.weatherapi.common.BaseFragment
import com.example.weatherapi.common.IMAGES_URL
import com.example.weatherapi.common.StateAction
import com.example.weatherapi.common.UNITS_DEFAULT
import com.example.weatherapi.common.ZIP_CODE_DEFAULT
import com.example.weatherapi.common.getTemperatureFormat
import com.example.weatherapi.common.toast
import com.example.weatherapi.databinding.FragmentWeatherBinding
import com.example.weatherapi.domain.WeatherDomain.WeatherDomain
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WeatherFragment : BaseFragment() {
    private val binding by lazy {
        FragmentWeatherBinding.inflate(layoutInflater)
    }

    private var units: String = UNITS_DEFAULT
    private var zipCode: String = ZIP_CODE_DEFAULT
    private var isMessage: Boolean = false
//    private val nycdapter by lazy {
//        NYCAdapter(this)
//    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initObservers()

        binding.imVSearch.setOnClickListener {
            isMessage = true
            zipCode = binding.eTZipCode.text.toString()
            weatherForecastViewModel.getWeather(code = zipCode, unit = units)
            initObservers()
        }


        return binding.root
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
                isRefreshing = false
            }
        }
    }

    private fun initObservers() {
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
                                    .load(IMAGES_URL + retrievedInfo.weather.first()?.icon + ".png")
                                    .into(imVCurrentWeather)
                            }
                        }

                        is StateAction.Error -> {
                            val msj = stateAction.error.message.toString()

                            if (isMessage && msj.isNotEmpty()) {
                                isMessage = activity?.toast(msj) == true

                            }
                        }

                    }
                }
            }
        }


    }


}


