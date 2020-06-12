package ru.delivery.system.common

import okhttp3.Response
import java.lang.Exception

interface DeliveryCallback {

    fun onSuccess(response: Response)

    fun onFailure(response: Response?, e: Exception)
}