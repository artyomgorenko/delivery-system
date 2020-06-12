package ru.delivery.system.models.json

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

class OrderCreateRequest {

    @JsonProperty(value = "createDate")
    var createDate: Date? = null

    @JsonProperty(value = "driverId")
    var driverId: Int? = null

    @JsonProperty(value = "transportId")
    var transportId: Int? = null

    @JsonProperty(value = "departurePoint")
    var departurePoint: String? = null

    @JsonProperty(value = "destinationPoint")
    var destinationPoint: String? = null

    /**
     * Список продуктов в заказе
     */
    @JsonProperty(value = "productList")
    var productList: List<Product>? = null

    class Product {
        @JsonProperty(value = "productId")
        var productId: Int? = null

        @JsonProperty(value = "count")
        var count: Int? = null

    }

}
