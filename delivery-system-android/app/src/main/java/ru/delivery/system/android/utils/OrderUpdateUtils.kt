package ru.delivery.system.android.utils

import okhttp3.Callback
import ru.delivery.system.android.models.json.OrderStatusRequest

fun changeOrderStatus(orderId: Int, newStatus: String, callback: Callback) {
    val requestEntity = OrderStatusRequest().apply {
        body.orderId = orderId
        body.newStatus = newStatus
        body.driverId = 1
        body.transportId = 1
    }
    val json = JsonSerializer().toJson(requestEntity)
    HttpHelper.post("order/changeOrderStatus", json, callback)
}