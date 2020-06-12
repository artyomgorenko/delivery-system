package ru.delivery.system.android.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Модель пользователя - Singleton
 */
@JsonIgnoreProperties(ignoreUnknown = true)
object UserModel {
    @JsonProperty("userId")
    var userId: Int = 1

    @JsonProperty("name")
    var name: String = "Json"

    @JsonProperty("surname")
    var surname: String = "Smith"

    @JsonProperty("role")
    var role: String = "DRIVER"

    fun fill(userId: Int, name: String, surname: String, role: String) {
        this.userId = userId
        this.name = name
        this.surname = surname
        this.role = role
    }

    fun clear() {
        userId = 0
        name = ""
        surname = ""
        role = ""
    }
}