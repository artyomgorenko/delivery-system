package ru.delivery.system.executors

import ru.delivery.system.controllers.OrderListController
import ru.delivery.system.models.Track
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object ScheduledMapUpdater {
    private val executorService = Executors.newScheduledThreadPool(1)
    private val orderListController = OrderListController
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
            orderListController.getOrdersById()
            orderListController.displayTracks()
        }
    }

}

fun main() {
    ScheduledMapUpdater.runScheduling()
}