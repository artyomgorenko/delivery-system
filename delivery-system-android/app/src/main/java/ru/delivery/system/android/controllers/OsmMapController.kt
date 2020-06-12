package ru.delivery.system.android.controllers

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.support.v4.content.res.ResourcesCompat
import android.util.Log
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import ru.delivery.system.android.R

class OsmMapController(private var mapView: MapView) {

    /**
     * Расчитывает и отображает путь между 3 точками.
     * Между 1 и 2 точкой - одним цветом, между 2 и 3 точкой - другим
     */
    fun calcAndDisplayDeliveryRoute(
        currentPoint: GeoPoint,
        startPoint: GeoPoint,
        endPoint: GeoPoint,
        width: Float = 6.0f
    ) {
//        addMarker(currentPoint)
        addWarehouseMarker(startPoint)
        addEndpointMarker(endPoint)
        calculateAndDisplyRoute(currentPoint, startPoint, 0x8000FFFF.toInt(), width)
        calculateAndDisplyRoute(startPoint, endPoint)
        mapView.invalidate()
    }

    /**
     * Расчитывает и отображает путь между 2 точками.
     */
    fun calculateAndDisplyRoute(
        startPoint: GeoPoint,
        endPoint: GeoPoint,
        color: Int = 0x800000FF.toInt(),
        width: Float = 5.0f
    ) {
        val context = mapView.context
        @SuppressLint("StaticFieldLeak")
        val calcRouteTask = object : AsyncTask<Void, Void, Void?>() {
            override fun doInBackground(vararg params: Void?): Void? {
                val waypoints: ArrayList<GeoPoint> = arrayListOf(startPoint, endPoint)
                val road: Road = OSRMRoadManager(context).getRoad(waypoints)
                val roadOverlay = RoadManager.buildRoadOverlay(road, color, width)
                mapView.overlays.add(roadOverlay)
                return null
            }
        }
        calcRouteTask.execute()
    }

    fun addWarehouseMarker(geoPoint: GeoPoint) {
        addMarker(geoPoint, ResourcesCompat.getDrawable(mapView.resources, R.drawable.ic_warehouse_blue_24dp, null)!!)
    }

    fun addEndpointMarker(geoPoint: GeoPoint) {
        addMarker(geoPoint, ResourcesCompat.getDrawable(mapView.resources, R.drawable.ic_flag_green_24dp, null)!!)
    }

    fun addMarker(geoPoint: GeoPoint, icon: Drawable) {
        try { // @HINT: try to fix map initialization NPE
            val marker = Marker(mapView)
            marker.position = geoPoint
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.icon = icon
            marker.title = "StartPoint"
            mapView.overlays.add(marker)
        } catch (e : Exception) {
            Log.e("OsmMapController", "Failed to create marker. ${e.message}")
        }
    }

    fun invalidate() {
        mapView.invalidate()
    }
}