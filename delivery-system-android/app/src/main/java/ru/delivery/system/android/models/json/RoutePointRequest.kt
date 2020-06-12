package ru.delivery.system.android.models.json

import com.fasterxml.jackson.annotation.JsonProperty

class RoutePointRequest {
    @JsonProperty(value = "orderId")
    var orderId: Int? = null

    @JsonProperty(value = "serialNumber")
    var serialNumber: Int? = null

    @JsonProperty(value = "geoPoint")
    var geoPoint: GeoPoint = GeoPoint()

    class GeoPoint {
        @JsonProperty(value = "latitude")
        var latitude: Float? = null

        @JsonProperty(value = "longitude")
        var longitude: Float? = null
    }
}