package ru.delivery.system.android.models.json

import org.osmdroid.util.GeoPoint
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*


class OrderInfoResponse {
    @JsonProperty(value = "orderId")
    val orderId: Int? = null

    @JsonProperty(value = "createDate")
    val createDate: Date? = null

    @JsonProperty(value = "status")
    val status: String? = null

    @JsonProperty(value = "driverId")
    val driverId: Int? = null

    @JsonProperty(value = "transportId")
    val transportId: Int? = null

    @JsonProperty(value = "departurePoint")
    val departurePoint: String? = null

    @JsonProperty(value = "destinationPoint")
    val destinationPoint: String? = null

    /**
     * Список продуктов в заказе
     */
    @JsonProperty(value = "productList")
    val productList: List<Product>? = null

    class Product {
        @JsonProperty(value = "productId")
        val productId: Int? = null

        @JsonProperty(value = "count")
        val count: Int? = null

        @JsonProperty(value = "cost")
        val cost: Double? = null

        @JsonProperty("name")
        val name: String? = null

        @JsonProperty("weight")
        val weight: Double? = null

        @JsonProperty("height")
        val height: Double? = null

        @JsonProperty("width")
        val width: Double? = null

        @JsonProperty("length")
        val length: Double? = null
    }

    /**
     * Маршрут доставки заказа
     */
    @JsonProperty(value = "orderRoute")
    val orderRoute: Route? = null

    class Route {
        @JsonProperty(value = "routeDistance")
        val routeDistance: Double? = null

        @JsonProperty(value = "routePoints")
        val routePoints: List<RoutePoint>? = null

        class RoutePoint {
            @JsonProperty(value = "serialNumber")
            val serialNumber: Int? = null

            @JsonProperty(value = "geoPoint")
            val geoPoint: GeoPoint? = null
        }
    }
}