package ru.delivery.system.executors

import javafx.application.Platform
import ru.delivery.system.controllers.OrderListController
import ru.delivery.system.httpcontrollers.MapUpdater
import ru.delivery.system.models.Track
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object ScheduledMapUpdater {
    private const val SYNC_FREQUENCY_S: Long = 15

    private val executorService = Executors.newScheduledThreadPool(1)
    private val orderListController = OrderListController
    private var warehouseController = MapUpdater()
    var mapTracks: List<Track> = ArrayList()

    fun runScheduling() {
        executorService.scheduleWithFixedDelay(
            MapUpdateTask(),
            SYNC_FREQUENCY_S,
            SYNC_FREQUENCY_S,
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
            //orderListController.displayTracks() // temporally commented
            warehouseController.displayWarehouses()
            warehouseController.displayDrivers()
            println("Finish UI updating")
        }
    }

}