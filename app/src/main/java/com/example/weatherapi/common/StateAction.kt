package com.example.weatherapi.common

import java.lang.Exception

sealed class StateAction {

    class Succes<T>(val response: T, val code: Int): StateAction()
    class Error(val error: Exception): StateAction()
    object Loading: StateAction()
}

enum class ConnectionStatus {
    Available, Unavailable, Losing, Lost
}