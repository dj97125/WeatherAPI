package com.example.weatherapi.common

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
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

@SuppressLint("SimpleDateFormat")
fun String.giveMeFormatDate(): String {
    val df: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val outputformat: DateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a")

    val date = df.parse(this) as Date

    return outputformat.format(date)
}
