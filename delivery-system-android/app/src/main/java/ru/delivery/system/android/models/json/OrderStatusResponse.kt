package ru.delivery.system.android.models.json

import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*
import com.fasterxml.jackson.annotation.JsonProperty



@JsonInclude(JsonInclude.Include.NON_NULL)
class OrderStatusResponse : BaseJsonModel() {
    var body = Body()

    @JsonInclude(JsonInclude.Include.NON_NULL)
    class Body {
        var orderId: Int? = null
        var distanceInMeters: Float? = null
        var deliveryTimeMs: Float? = null

        @JsonProperty("startPoint")
        val startPoint: RoutePointRequest.GeoPoint? = null

        @JsonProperty("warehousePoint")
        val warehousePoint: RoutePointRequest.GeoPoint? = null

        @JsonProperty("deliveryPoint")
        val deliveryPoint: RoutePointRequest.GeoPoint? = null

        @JsonProperty("startDate")
        val startDate: Date? = null

        @JsonProperty("startShipmentDate")
        val startShipmentDate: Date? = null

        @JsonProperty("startDeliveringDate")
        val startDeliveringDate: Date? = null

        @JsonProperty("doneDate")
        val doneDate: Date? = null

        @JsonProperty("wareHouseRouteLength")
        val wareHouseRouteLength: Int? = null

        @JsonProperty("deliveryRouteLength")
        val deliveryRouteLength: Double? = null

        @JsonProperty("totalCost")
        val totalCost: Double? = null

//        val getInWorkTime: Date = Date()
//        val outgoingTime: Date = Date(Date().time + 2000)
//        val deliveredTime: Date = Date(Date().time + 4000)
//        val wareHouseRouteLength: Int = 10
//        val deliveryRouteLength: Int = 20
//        val totalCost: Double = 0.0
    }
}