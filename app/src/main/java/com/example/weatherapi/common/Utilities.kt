package com.example.weatherapi.common

import android.content.Context
import android.widget.Toast
import kotlin.math.roundToInt

fun Double.getTemperatureFormat(units: String): String {
    var finalTemp = ""
    when (units) {
        "imperial" -> finalTemp = "${this.roundToInt()} °F"
        "metric" -> finalTemp = "${this.roundToInt()} °C"
        "" -> finalTemp = "${this.roundToInt()} °K"
    }

    return finalTemp
}

fun Context.toast(message: String): Boolean {
    Toast.makeText(
        this,
        message,
        Toast.LENGTH_SHORT
    ).show()
    return false
}
