package ru.delivery.system.executors

import de.saring.leafletmap.LatLong
import de.saring.leafletmap.TrackColor
import io.github.rybalkinsd.kohttp.ext.httpGet
import javafx.application.Platform
import okhttp3.Response
import ru.delivery.system.common.JsonSerializer
import ru.delivery.system.models.Track
import ru.delivery.system.models.json.OrderRoute
import ru.delivery.system.views.MapView
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object ScheduledMapUpdater {
    private val executorService = Executors.newScheduledThreadPool(1)
    var mapTracks: List<Track> = ArrayList()

    fun runScheduling() {
        executorService.scheduleWithFixedDelay(
            MapUpdateTask(),
            5,
            5,
            TimeUnit.SECONDS
        )
    }

    fun stopScheduling() {
        executorService.shutdownNow()
    }

    class MapUpdateTask : Runnable {
        override fun run() {
            try {
                println("Trying to update map")
                val response: Response = "http://localhost:8080/delivery-system/orderList/getOrderRoute?orderId=4".httpGet()
                response.use {
                    if (response.isSuccessful) {
                        response.body()?.string()?.let { json ->
                            mapTracks = ArrayList()
                            val orderRoute = JsonSerializer().toEntity<OrderRoute>(json)
                            val points = ArrayList<LatLong>()
                            orderRoute.routePoints?.forEach { point ->
                                val latLong = LatLong(point.geoPoint!!.latitude!!.toDouble(), point.geoPoint!!.longitude!!.toDouble())
                                points.add(latLong)
                            }
                            val track = Track(points, TrackColor.BLUE)

                            Platform.runLater {
                                MapView.Map.mapView.clearMarkersAndTracks()
                                track.display(MapView.Map.mapView)
                            }
                        }
                    }
                    println("Map updated!")
                }
            } catch (e : Exception) {
                println("Error during map update. " + e.message)
            }
        }
    }
}

fun main() {
    ScheduledMapUpdater.runScheduling()
}