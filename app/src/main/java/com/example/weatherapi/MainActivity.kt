package com.example.weatherapi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.weatherapi.common.ConnectionStatus
import com.example.weatherapi.common.ConnectivityObserver
import com.example.weatherapi.common.MESSAGE_FOR_NO_INTERNET_CONNECTION
import com.example.weatherapi.common.NetworkConnectivityObserver
import com.example.weatherapi.common.TITLE_FOR_NO_INTERNET_CONNECTION
import com.example.weatherapi.common.provideAlertPopUp
import com.example.weatherapi.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var connectivityObserver: ConnectivityObserver
    private lateinit var mainActivityBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)

        connectivityObserver = NetworkConnectivityObserver(applicationContext)

        connectivityObserver.observe().onEach {
            if (!it.equals(ConnectionStatus.Available)) {
                this.provideAlertPopUp(
                    TITLE_FOR_NO_INTERNET_CONNECTION,
                    MESSAGE_FOR_NO_INTERNET_CONNECTION
                )
            }
        }.launchIn(lifecycleScope)
    }


}