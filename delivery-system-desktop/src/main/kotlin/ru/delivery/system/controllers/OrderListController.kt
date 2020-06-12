package ru.delivery.system.controllers

import de.saring.leafletmap.LatLong
import de.saring.leafletmap.TrackColor
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import okhttp3.Response
import ru.delivery.system.common.JsonSerializer
import ru.delivery.system.executors.ScheduledMapUpdater
import ru.delivery.system.models.Track
import ru.delivery.system.models.json.OrderInfo
import ru.delivery.system.rest.HttpHelper
import ru.delivery.system.views.MapView

object OrderListController {

    private val httpHelper = HttpHelper

    private var orderListValues: List<OrderInfo> = arrayListOf(
        OrderInfo().apply {
            orderId = 2
            status = "Completed"
        }
    )
    var displayedValues: ObservableList<OrderInfo> = FXCollections.observableArrayList<OrderInfo>()

    init {
        getOrdersById(arrayListOf(2, 3))
        updateDisplayedValues(orderListValues)
    }

    fun findById(orderId: Int) {
        updateDisplayedValues(orderListValues.filter { it.orderId == orderId })
    }

    fun findById(orderId: String) {
        try {
            if (orderId.isEmpty()) {
                updateDisplayedValues(orderListValues)
            } else {
                updateDisplayedValues(orderListValues.filter { it.orderId == orderId.toInt() })
            }
        } catch (e: NumberFormatException) {
            println(e.message)
            updateDisplayedValues(orderListValues)
        }
    }

    fun findByStatus(orderStatus: String) {
//        updateDisplayedValues(orderListValues.filter { it.status == orderStatus })
    }

    @Synchronized
    private fun updateDisplayedValues(values: List<OrderInfo>) {
        Platform.runLater {
            displayedValues.clear()
            displayedValues.addAll(values)

            // remove it
//            if (displayedValues.isEmpty()) {
//                displayedValues.addAll(values)
//            } else {
//                values.forEach { value ->
//                    displayedValues.forEach { displayedValue ->
//                        if (displayedValue.orderId == value.orderId) {
//                            displayedValue.update(value)
//                        } else if (displayedValues.firstOrNull { it.orderId == value.orderId } == null) {
//                            displayedValues.add(value)
//                        }
//                    }
//                }
//            }
        }
    }

    fun getOrdersById(orderIdList: List<Int> = displayedValues.map { it.orderId!! } ) {
        try {
            println("Start to update map")
            val queryParamList = orderIdList.joinToString(separator = "&") {"orderIdList=$it"} // [1,2,3]
//            val response: Response = httpHelper.syncGet("order/getOrderInfo?$queryParamList")
            val response: Response = httpHelper.syncGet("order/getAllOrders")

            response.use {
                if (response.isSuccessful) {
                    response.body()?.string()?.let { json ->
                        val orderInfoList = JsonSerializer().toEntity<List<OrderInfo>>(json)
                        orderListValues = orderInfoList
                        updateDisplayedValues(orderListValues)
                        println("Map updated. Incoming orders count = " + orderInfoList.size)
                    }
                } else {
                    println("Map does't updated. Bad response")
                }
            }

        } catch (e : Exception) {
            println("Error during map update. " + e.message)
        }
    }

    fun displayTracks() {
        displayTracks(displayedValues)
    }

    // TODO: Refactor me, pls
    fun displayTracks(orderInfoList: List<OrderInfo>) {
        ScheduledMapUpdater.mapTracks = ArrayList()
        Platform.runLater { MapView.Map.mapView.clearMarkersAndTracks() }
        orderInfoList.forEach { orderInfo ->
            val points = ArrayList<LatLong>()
            orderInfo.orderRoute?.routePoints?.forEach { point ->
                val latLong = LatLong(point.geoPoint!!.latitude!!.toDouble(), point.geoPoint!!.longitude!!.toDouble())
                points.add(latLong)
            }
            val track = Track(points, TrackColor.BLUE)
            Platform.runLater { track.display(MapView.Map.mapView) }
        }
    }

}

