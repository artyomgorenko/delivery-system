package ru.delivery.system.models.json

import com.fasterxml.jackson.annotation.JsonProperty

class OrderCreateResponse {
    @JsonProperty("orderId")
    var orderId: Int? = null
}