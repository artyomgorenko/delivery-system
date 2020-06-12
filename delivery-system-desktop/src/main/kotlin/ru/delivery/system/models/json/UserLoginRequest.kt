package ru.delivery.system.models.json

import com.fasterxml.jackson.annotation.JsonProperty

class UserLoginRequest(
    @field:JsonProperty(value = "username") var username: String? = null,
    @field:JsonProperty(value = "password") var password: String? = null
)