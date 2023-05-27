package com.example.weatherapi.common

//http://api.openweathermap.org/geo/1.0/reverse?lat=51.5098&lon=-0.1180&appid=0694cd38862ff5aecda5594fdfb11184
//https://api.openweathermap.org/data/2.5/weather?zip=85210&appid=0694cd38862ff5aecda5594fdfb11184&units=imperial
//http://api.openweathermap.org/geo/1.0/direct?q=London&limit=5&appid=0694cd38862ff5aecda5594fdfb11184&units=imperial
const val BASE_URL = "https://api.openweathermap.org/"
const val IMAGES_URL = "https://openweathermap.org/img/wn/"
const val END_POINT_FORECAST = "data/2.5/forecast"
const val END_POINT_WEATHER = "data/2.5/weather"
const val END_POINT_GEO_CODE = "geo/1.0/direct"

const val TOKEN = "0694cd38862ff5aecda5594fdfb11184"

const val PARAM_ZIP = "zip"
const val PARAM_CITY_NAME = "q"
const val PARAM_LIMIT = "limit"
const val PARAM_APPID = "appid"
const val PARAM_UNITS = "units"
const val PARAM_LAT = "lat"
const val PARAM_LON = "lon"

const val TAG = "ERROR VIEW MODEL"
const val IMAGE_FORMAT = ".png"

const val DATABASE_NAME = "WeatherDB"
const val LIMIT_VALUE = "5"


const val UNITS_DEFAULT = "imperial"
const val CITY_BY_DEFAULT = "Mesa"
const val LAT_DEFAULT = "41.63963963963964"
const val LON_DEFAULT = "-83.70845595895945"
const val ACTION_START = "ACTION_START"
const val ACTION_STOP = "ACTION_STOP"
const val TITLE_ALERT = "Alert"
const val MESSAGE_FOR_NO_INTERNET_CONNECTION =
    "You dont have internet conection"

const val MESSAGE_FOR_ASKING_LOCATION_PERMISION =
    "Do you grant we acces your current location, this is just for showing you the weather"



