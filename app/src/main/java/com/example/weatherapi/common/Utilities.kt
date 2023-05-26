package com.example.weatherapi.common

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.content.ContextCompat
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

fun Context.hasLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

fun Context.provideAlertPopUp(
    title: String,
    message: String
) {
    AlertDialog.Builder(this).setTitle(title)
        .setMessage(message)
        .setNegativeButton("OK") { _, _ -> }
        .setCancelable(false)
        .create()
        .show()
}
