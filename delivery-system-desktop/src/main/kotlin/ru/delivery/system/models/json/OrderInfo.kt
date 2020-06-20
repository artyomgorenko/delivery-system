package ru.delivery.system.models.json

import com.fasterxml.jackson.annotation.JsonProperty

import java.util.Date

/**
 * Полная информация о заказе, включая список товаров и маршрут
 */
class OrderInfo {

    @JsonProperty("orderId")
    var orderId: Int? = null

    @JsonProperty("createDate")
    var createDate: Date? = null

    @JsonProperty("status")
    var status: String? = null

    @JsonProperty("driverId")
    var driverId: Int? = null

    @JsonProperty("transportId")
    var transportId: Int? = null

    @JsonProperty("departurePoint")
    var departurePoint: String? = null

    @JsonProperty("departureLongitude")
    var departureLongitude: Double? = null

    @JsonProperty("departureLatitude")
    var departureLatitude: Double? = null

    @JsonProperty("destinationPoint")
    var destinationPoint: String? = null

    @JsonProperty("destinationLongitude")
    var destinationLongitude: Double? = null

    @JsonProperty("destinationLatitude")
    var destinationLatitude: Double? = null

    /**
     * Список продуктов в заказе
     */
    @JsonProperty("productList")
    var productList: List<Product>? = null

    /**
     * Маршрут доставки заказа
     */
    @JsonProperty("orderRoute")
    var orderRoute: Route? = null

    class Product {
        @JsonProperty("productId")
        var productId: Int? = null

        @JsonProperty("count")
        var count: Int? = null

        @JsonProperty("cost")
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
        @JsonProperty("routeDistance")
        var routeDistance: Double? = null

        @JsonProperty("routePoints")
        var routePoints: List<RoutePoint>? = null

        class RoutePoint {
            @JsonProperty("serialNumber")
            var serialNumber: Int? = null

            @JsonProperty("geoPoint")
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
