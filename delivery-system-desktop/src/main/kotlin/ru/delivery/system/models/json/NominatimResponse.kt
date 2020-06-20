package ru.delivery.system.models.json

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class NominatimResponse {
    var type: String? = null
    var geocoding: Geocoding? = null
    var features: Array<Features>? = null

    class Geocoding {
        var licence: String? = null
        var query: String? = null
        var attribution: String? = null
        var version: String? = null
    }

    class Features {
        var type: String? = null
        var properties: Properties? = null
        var geometry: Geometry? = null

        class Properties {

            var geocoding: Geocoding? = null

            class Geocoding {
                var place_id: String? = null
                var osm_type: String? = null
                var osm_id: String? = null
                var type: String? = null
                var label: String? = null
                var name: String? = null
            }
        }

        class Geometry {
            var type: String? = null
            var coordinates: Array<Float>? = null
        }
    }
}