package com.example.weatherapi.model.network.response

class GeoCodeResponse : ArrayList<GeoCodeResponseItem>()

data class GeoCodeResponseItem(
    val country: String?,
    val lat: Double?,
    val lon: Double?,
    val name: String?,
    val state: String?
)