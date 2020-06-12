package ru.delivery.system.models.json

import com.fasterxml.jackson.annotation.JsonProperty
import ru.delivery.system.common.JsonSerializer

class OrderRoute {
    @JsonProperty(value = "orderId")
    var orderId: Int? = null

    @JsonProperty(value = "routePoints")
    var routePoints: List<RoutePoint>? = null

    @JsonProperty(value = "routeDistance")
    var routeDistance: Double? = null

    class RoutePoint {
        @JsonProperty(value = "serialNumber")
        var serialNumber: Int? = null

        @JsonProperty(value = "geoPoint")
        var geoPoint: GeoPoint? = null

        class GeoPoint {
            @JsonProperty(value = "latitude")
            var latitude: Float? = null

            @JsonProperty(value = "longitude")
            var longitude: Float? = null
        }
    }
}

fun main() {
    val jsonSerializer = JsonSerializer()
    val orderRoute = OrderRoute()
    orderRoute.orderId = 3
    val routePoints = ArrayList<OrderRoute.RoutePoint>()
    routePoints.add(OrderRoute.RoutePoint().apply {
        serialNumber = 10
        geoPoint?.apply {
            latitude = 10.1F
            longitude = 20.2F
        }
    })
    orderRoute.routePoints = routePoints

    var json = jsonSerializer.toJson(orderRoute)
    println("Json:$json")
    val entity = jsonSerializer.toEntity<OrderRoute>(json)
    json = jsonSerializer.toJson(entity)
    println(json)
}