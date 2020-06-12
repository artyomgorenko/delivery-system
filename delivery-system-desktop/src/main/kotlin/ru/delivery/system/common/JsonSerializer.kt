package ru.delivery.system.common

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ru.delivery.system.models.json.MapMarker

class JsonSerializer {

    val mapper = jacksonObjectMapper()

    fun <T> toJson(entity: T) : String {
        return mapper.writeValueAsString(entity)
    }

    inline fun <reified T> toEntity(json : String) : T {
        return mapper.readValue(json)
    }

}

// Test
fun main(args: Array<String>) {
    val jsonSerializer = JsonSerializer()
    val marker = MapMarker(10.1, 20.1)

    val json = jsonSerializer.toJson(marker)
    println("Json:$json")
    val entity = jsonSerializer.toEntity<MapMarker>(json)
    println(entity.latitude.toString() + " " + entity.longitude)
}
