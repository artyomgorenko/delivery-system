package ru.delivery.system.android.models.json

class OrderStatusResponse : BaseJsonModel() {
    var body = Body()

    class Body {
        var orderId: Int? = null
        var distanceInMeters: Float? = null
        var deliveryTimeMs: Float? = null
    }
}