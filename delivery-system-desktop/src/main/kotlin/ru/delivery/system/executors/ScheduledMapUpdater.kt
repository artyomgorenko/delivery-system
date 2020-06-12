package ru.delivery.system.executors

import javafx.application.Platform
import ru.delivery.system.controllers.OrderListController
import ru.delivery.system.httpcontrollers.MapUpdater
import ru.delivery.system.models.Track
import ru.delivery.system.views.MapView
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object ScheduledMapUpdater {
    private val executorService = Executors.newScheduledThreadPool(1)
    private val orderListController = OrderListController
    private var warehouseController = MapUpdater()
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
            println("Start UI updating")
            orderListController.getOrdersById()
            orderListController.displayTracks()
            warehouseController.displayWarehouses()
            warehouseController.displayDrivers()
            println("Finish UI updating")
        }
    }

}

fun main() {
    ScheduledMapUpdater.runScheduling()
}