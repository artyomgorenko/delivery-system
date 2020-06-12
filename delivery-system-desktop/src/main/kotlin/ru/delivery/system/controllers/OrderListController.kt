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
import ru.delivery.system.models.viewmodels.Order
import ru.delivery.system.rest.HttpHelper
import ru.delivery.system.views.MapView
import tornadofx.observable

object OrderListController {

    private val httpHelper = HttpHelper

    var allOrders: List<OrderInfo> = arrayListOf(
        OrderInfo().apply {
            orderId = 2
            status = "Completed"
        }
    ).observable()
    var filteredOrders: ObservableList<OrderInfo> = FXCollections.observableArrayList<OrderInfo>()

    fun gerOrderList(): List<Order> {
        return allOrders.map { orderInfo -> Order().apply {
            orderInfo.orderId?.let { orderId = it }
            orderInfo.status?.let { status = it }
            orderInfo.departurePoint?.let { departurePoint = it }
            orderInfo.destinationPoint?.let { destinationPoint = it }
            orderInfo.destinationPoint?.let { destinationPoint = it }
            deliveryTime = "12:00"
        } }
    }

    init {
        getOrdersById(arrayListOf(2, 3))
        updateDisplayedValues(allOrders)
    }

    fun findById(orderId: Int) {
        updateDisplayedValues(allOrders.filter { it.orderId == orderId })
    }

    fun findById(orderId: String) {
        try {
            if (orderId.isEmpty()) {
                updateDisplayedValues(allOrders)
            } else {
                updateDisplayedValues(allOrders.filter { it.orderId == orderId.toInt() })
            }
        } catch (e: NumberFormatException) {
            println(e.message)
            updateDisplayedValues(allOrders)
        }
    }

    fun findByStatus(orderStatus: String) {
//        updateDisplayedValues(allOrders.filter { it.status == orderStatus })
    }

    @Synchronized
    private fun updateDisplayedValues(values: List<OrderInfo>) {
        Platform.runLater {
            filteredOrders.clear()
            filteredOrders.addAll(values)
            filteredOrders.sortBy {it.orderId  }

            // remove it
//            if (filteredOrders.isEmpty()) {
//                filteredOrders.addAll(values)
//            } else {
//                values.forEach { value ->
//                    filteredOrders.forEach { displayedValue ->
//                        if (displayedValue.orderId == value.orderId) {
//                            displayedValue.update(value)
//                        } else if (filteredOrders.firstOrNull { it.orderId == value.orderId } == null) {
//                            filteredOrders.add(value)
//                        }
//                    }
//                }
//            }
        }
    }

    fun getOrdersById(orderIdList: List<Int> = filteredOrders.map { it.orderId!! } ) {
        try {
            println("Start orderList updating")
            val queryParamList = orderIdList.joinToString(separator = "&") {"orderIdList=$it"} // [1,2,3]
//            val response: Response = httpHelper.syncGet("order/getOrderInfo?$queryParamList")
            val response: Response = httpHelper.syncGet("order/getAllOrders")

            response.use {
                if (response.isSuccessful) {
                    response.body()?.string()?.let { json ->
                        val orderInfoList = JsonSerializer().toEntity<List<OrderInfo>>(json)
                        allOrders = orderInfoList
                        updateDisplayedValues(allOrders)
                        println("OrderList updated. Incoming orders count = " + orderInfoList.size)
                    }
                } else {
                    println("OrderList does't updated. Bad response")
                }
            }
            println("OrderList updating finished")
        } catch (e : Exception) {
            println("Error during OrderList update. " + e.message)
        }
    }

    fun displayTracks() {
        displayTracks(filteredOrders)
    }

    // TODO: Refactor me, pls
    fun displayTracks(orderInfoList: List<OrderInfo>) {
        try {
            println("Start map updating")
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
            println("Map updating finished")
        } catch (e: Exception) {
            println("Error, during map updating: ${e.message}")
        }
    }

}

