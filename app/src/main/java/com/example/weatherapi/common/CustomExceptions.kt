package com.example.weatherapi.common


class FailedNetworkResponseException(
    message: String = "Error: failure in the network response"
) : Exception(message)

class Exception400(
    message: String = "Error: city not found"
) : Exception(message)
