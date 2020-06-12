package ru.delivery.system.models.json

import com.fasterxml.jackson.annotation.JsonProperty

class WarehousesInfoResponse {
    @JsonProperty("id")
    var id: Int? = null

    @JsonProperty("address")
    var address: String? = null

    @JsonProperty("latitude")
    var latitude: Float? = null

    @JsonProperty("longitude")
    var longitude: Float? = null
}