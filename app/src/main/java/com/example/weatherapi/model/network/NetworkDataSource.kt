package com.example.weatherapi.model.network

import javax.inject.Inject


interface NetworkDataSource {

}
class NetworkDataSourceImpl @Inject constructor(
    private val service: WeatherApi
):NetworkDataSource {
}