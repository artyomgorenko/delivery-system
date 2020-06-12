package ru.delivery.system.android.utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class JsonSerializer {
    val mapper = jacksonObjectMapper()

    fun <T> toJson(entity: T) : String {
        return mapper.writeValueAsString(entity)
    }

    inline fun <reified T> toEntity(json : String) : T {
        return mapper.readValue(json)
    }

}