package ru.delivery.system.models

import de.saring.leafletmap.LatLong
import de.saring.leafletmap.LeafletMapView
import de.saring.leafletmap.TrackColor
import javafx.application.Platform
import ru.delivery.system.models.json.OrderRoute

class Track (
    var coords: List<LatLong>,
    var color: TrackColor = TrackColor.BLUE,
    var name: String? = null
) {

    fun display(orderRoute: OrderRoute, map: LeafletMapView) {
        val points = ArrayList<LatLong>()
        orderRoute.routePoints?.forEach { point ->
            val latLong = LatLong(point.geoPoint!!.latitude!!.toDouble(), point.geoPoint!!.longitude!!.toDouble())
            points.add(latLong)
        }

        Platform.runLater {
            map.addTrackWithMarkers(points, color)
        }
    }

    fun display(map: LeafletMapView) {
        map.addTrackWithMarkers(coords, color)
    }

}