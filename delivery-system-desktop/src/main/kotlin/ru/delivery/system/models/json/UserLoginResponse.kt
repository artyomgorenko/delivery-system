package ru.delivery.system.models.json

import com.fasterxml.jackson.annotation.JsonProperty

class UserLoginResponse {
    @JsonProperty(value = "name")
    var name: String? = null

    @JsonProperty(value = "surname")
    var surname: String? = null

    /**
     * User role: driver/dispatcher
     */
    @JsonProperty(value = "role")
    var role: String? = null

    @JsonProperty(value = "appAdmin")
    var isAppAdmin: Boolean = false

    /**
     * TODO: Not used yet. SessionId validation must be implemented in the future
     */
    @JsonProperty(value = "sessionId")
    var sessionId: String? = null

    @JsonProperty(value = "errorMessage")
    var errorMessage: String? = null
}