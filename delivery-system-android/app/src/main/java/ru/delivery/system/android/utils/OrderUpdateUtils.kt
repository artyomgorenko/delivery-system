package ru.delivery.system.android.utils

import android.os.AsyncTask
import okhttp3.Call
import okhttp3.Callback
import ru.delivery.system.android.models.json.OrderStatusRequest

fun changeOrderStatus(orderId: Int, newStatus: String, callback: Callback) : Call {
    val requestEntity = OrderStatusRequest().apply {
        body.orderId = orderId
        body.newStatus = newStatus
        body.driverId = 1
        body.transportId = 1
    }
    val json = JsonSerializer().toJson(requestEntity)
    return HttpHelper.post("order/changeOrderStatus", json, callback)
}

fun changeOrderStatusTask(orderId: Int, newStatus: String, callback: Callback): AsyncTask<Void, Void, Call> {
    return object : AsyncTask<Void, Void, Call>() {
        override fun doInBackground(vararg params: Void): Call {
            return changeOrderStatus(orderId, newStatus, callback)
        }
    }
}