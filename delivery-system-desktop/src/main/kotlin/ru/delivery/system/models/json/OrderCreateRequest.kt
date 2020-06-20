package ru.delivery.system.models.json

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

class OrderCreateRequest {

    @JsonProperty("createDate")
    var createDate: Date? = null

    @JsonProperty("driverId")
    var driverId: Int? = null

    @JsonProperty("transportId")
    var transportId: Int? = null

    /**
     * Адрес пункта отправления
     */
    @JsonProperty("departurePoint")
    var departurePoint: String? = null

    @JsonProperty("departureLatitude")
    var departureLatitude: Float? = null

    @JsonProperty("departureLongitude")
    var departureLongitude: Float? = null

    /**
     * Адрес пункта прибытия
     */
    @JsonProperty("destinationPoint")
    var destinationPoint: String? = null

    @JsonProperty("destinationLatitude")
    var destinationLatitude: Float? = null

    @JsonProperty("destinationLongitude")
    var destinationLongitude: Float? = null

    /**
     * Дополнительная информация
     */
    @JsonProperty("deliveryDate")
    var deliveryDate: Date? = null
    
    @JsonProperty("isOrderCommon")
    var isOrderCommon: Boolean? = null

    @JsonProperty("deliveryUrgency")
    var deliveryUrgency: String? = null

    @JsonProperty("baseCost")
    var baseCost: Float? = null

    /**
     * Список продуктов в заказе
     */
    @JsonProperty("productList")
    var productList: List<Product>? = null

    class Product {
        @JsonProperty("productId")
        var productId: Int? = null

        @JsonProperty("count")
        var count: Int? = null

    }

}
