package ru.delivery.system.android.models.json

import java.util.*

class OrderStatusResponse : BaseJsonModel() {
    var body = Body()

    class Body {
        var orderId: Int? = null
        var distanceInMeters: Float? = null
        var deliveryTimeMs: Float? = null

        val getInWorkTime: Date = Date()
        val outgoingTime: Date = Date(Date().time + 2000)
        val deliveredTime: Date = Date(Date().time + 4000)
        val wareHouseRouteLength: Int = 10
        val deliveryRouteLength: Int = 20
        val totalCost: Double = 0.0
    }
}