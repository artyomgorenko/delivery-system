package ru.delivery.system.models.json

import com.fasterxml.jackson.annotation.JsonProperty

class UserInfoResponse {
    @JsonProperty("id")
    var id: Int? = null

    @JsonProperty("username")
    var username: String? = null

    @JsonProperty("name")
    var name: String? = null

    @JsonProperty("surname")
    var surname: String? = null

    @JsonProperty("role")
    var role: String? = null

    @JsonProperty("lastLatitude")
    var lastLatitude: Float? = null

    @JsonProperty("lastLongitude")
    var lastLongitude: Float? = null
}
