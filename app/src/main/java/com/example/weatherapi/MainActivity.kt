package com.example.weatherapi

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.weatherapi.common.CITY_BY_DEFAULT
import com.example.weatherapi.common.ConnectionStatus
import com.example.weatherapi.common.ConnectivityObserver
import com.example.weatherapi.common.MESSAGE_FOR_NO_INTERNET_CONNECTION
import com.example.weatherapi.common.NetworkConnectivityObserver
import com.example.weatherapi.common.TITLE_ALERT
import com.example.weatherapi.common.provideAlertPopUp
import com.example.weatherapi.common.toast
import com.example.weatherapi.databinding.ActivityMainBinding
import com.example.weatherapi.view_model.WeatherForecastViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var connectivityObserver: ConnectivityObserver
    private lateinit var mainActivityBinding: ActivityMainBinding
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            requestForLocationPermission(isGranted)

        }
    private val weatherForecastViewModel: WeatherForecastViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)

        requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)

        connectivityObserver = NetworkConnectivityObserver(applicationContext)

        connectivityObserver.observe().onEach {
            if (!it.equals(ConnectionStatus.Available)) {
                this.provideAlertPopUp(
                    TITLE_ALERT,
                    MESSAGE_FOR_NO_INTERNET_CONNECTION
                )
            }
        }.launchIn(lifecycleScope)
    }

    @SuppressLint("MissingPermission")
    private fun requestForLocationPermission(isGranted: Boolean) {
        var message = ""
        if (isGranted) {
            message = "Permission Location Granted"
            fusedLocationClient.lastLocation.addOnCompleteListener {

                it.result?.let { location ->
                    weatherForecastViewModel.apply {
                        getWeatherByCoord(
                            lat = location.latitude.toString(),
                            long = location.longitude.toString()
                        )
                        getForecastByCoord(
                            lat = location.latitude.toString(),
                            long = location.longitude.toString()
                        )
                        lat = location.latitude.toString()
                        lon = location.longitude.toString()
                    }


                }

            }

        } else {
            message = "Permission Location Rejected"
            locationPermissionRejectedOrLocationNull()

        }

        toast(message)


    }

    private fun locationPermissionRejectedOrLocationNull() {
        weatherForecastViewModel.getGeoCode(city = CITY_BY_DEFAULT)

    }


}