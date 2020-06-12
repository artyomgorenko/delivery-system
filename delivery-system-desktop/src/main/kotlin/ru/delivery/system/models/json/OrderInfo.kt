package ru.delivery.system.models.json

import com.fasterxml.jackson.annotation.JsonProperty

import java.util.Date

/**
 * Полная информация о заказе, включая список товаров и маршрут
 */
class OrderInfo {

    @JsonProperty(value = "orderId")
    var orderId: Int? = null

    @JsonProperty(value = "createDate")
    var createDate: Date? = null

    @JsonProperty(value = "status")
    var status: String? = null

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

    /**
     * Маршрут доставки заказа
     */
    @JsonProperty(value = "orderRoute")
    var orderRoute: Route? = null

    class Product {
        @JsonProperty(value = "productId")
        var productId: Int? = null

        @JsonProperty(value = "count")
        var count: Int? = null

        @JsonProperty(value = "cost")
        var cost: Double? = null

        @JsonProperty("name")
        var name: String? = null

        @JsonProperty("weight")
        var weight: Double? = null

        @JsonProperty("height")
        var height: Double? = null

        @JsonProperty("width")
        var width: Double? = null

        @JsonProperty("length")
        var length: Double? = null
    }

    class Route {
        @JsonProperty(value = "routeDistance")
        var routeDistance: Double? = null

        @JsonProperty(value = "routePoints")
        var routePoints: List<RoutePoint>? = null

        class RoutePoint {
            @JsonProperty(value = "serialNumber")
            var serialNumber: Int? = null

            @JsonProperty(value = "geoPoint")
            var geoPoint: OrderRoute.RoutePoint.GeoPoint? = null
        }
    }

    fun update(obj: OrderInfo) {
        createDate = obj.createDate
        status = obj.status
        driverId = obj.driverId
        transportId = obj.transportId
        orderRoute = obj.orderRoute
        productList = obj.productList
        destinationPoint = obj.destinationPoint
        departurePoint = obj.departurePoint
    }
}
