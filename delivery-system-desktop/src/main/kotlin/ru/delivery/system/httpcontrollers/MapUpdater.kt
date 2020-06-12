package ru.delivery.system.httpcontrollers

import de.saring.leafletmap.ColorMarker
import de.saring.leafletmap.LatLong
import de.saring.leafletmap.LeafletMapView
import javafx.application.Platform
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import ru.delivery.system.common.JsonSerializer
import ru.delivery.system.models.json.UserInfoResponse
import ru.delivery.system.models.json.WarehousesInfoResponse
import ru.delivery.system.rest.HttpHelper
import ru.delivery.system.views.MapView
import java.io.IOException

class MapUpdater {

    private val httpHelper = HttpHelper
    private val jsonSerializer = JsonSerializer()
    private var mapView: LeafletMapView = MapView.Map.mapView

    fun displayWarehouses() {
        try {
            httpHelper.get("warehouse/getAllWarehouses", object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println("Warehouses updating failed: $e.message")
                }

                override fun onResponse(call: Call, response: Response) {
                    println("Warehouses updating started.")
                    if (response.isSuccessful) {
                        response.body()?.let { body ->
                            val warehouseList = jsonSerializer.toEntity<List<WarehousesInfoResponse>>(body.string())
                            warehouseList.forEach { warehouse ->
                                 Platform.runLater {
                                     mapView.addMarker(
                                         LatLong(warehouse.latitude!!.toDouble(), warehouse.longitude!!.toDouble()),
                                         "Склад №${warehouse.id}",
                                         ColorMarker.WAREHOUSE_MARKER,
                                         0)
                                 }
                            }
                        }
                        println("Warehouses updating finished.")
                    } else {
                        println("Warehouses updating failed: Bad response")
                    }
                    response.close()
                }
            })
        } catch (e: Exception) {
            println("Warehouses updating failed: $e.message")
        }
    }

    fun displayDrivers() {
        try {
            httpHelper.get("user/getAllUsers", object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println("Transport updating failed: $e.message")
                }

                override fun onResponse(call: Call, response: Response) {
                    println("Transport updating started.")
                    if (response.isSuccessful) {
                        response.body()?.let { body ->
                            val userList = jsonSerializer.toEntity<List<UserInfoResponse>>(body.string())
                            userList.forEach { user ->
                                Platform.runLater {
                                    mapView.addMarker(
                                        LatLong(user.lastLatitude!!.toDouble(), user.lastLongitude!!.toDouble()),
                                        "Водитель №${user.id}",
                                        ColorMarker.TRANSPORT_MARKER)
                                }
                            }
                        }
                        println("Transport updating finished.")
                    } else {
                        println("Transport updating failed: Bad response")
                    }
                    response.close()
                }
            })
        } catch (e: Exception) {
            println("Transport updating failed: $e.message")
        }

    }
}