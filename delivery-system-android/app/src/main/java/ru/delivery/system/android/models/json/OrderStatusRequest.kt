package ru.delivery.system.android.models.json

import com.fasterxml.jackson.annotation.JsonProperty

class OrderStatusRequest {
    var body = Body()

    class Body {
        @JsonProperty(value = "orderId")
        var orderId: Int? = null

        @JsonProperty(value = "driverId")
        var driverId: Int? = null

        @JsonProperty(value = "transportId")
        var transportId: Int? = null

        @JsonProperty(value = "newStatus")
        var newStatus: String? = null
    }
}