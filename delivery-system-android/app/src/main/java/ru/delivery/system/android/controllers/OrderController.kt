package ru.delivery.system.android.controllers

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import ru.delivery.system.android.models.OrderModel
import ru.delivery.system.android.models.json.OrderInfoResponse
import ru.delivery.system.android.models.json.RoutePointRequest
import ru.delivery.system.android.utils.HttpHelper
import ru.delivery.system.android.utils.JsonSerializer
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

object OrderController {
    private val httpHelpler = HttpHelper
    private val serializer = JsonSerializer()

    fun addRoutePoint(requestEntity: RoutePointRequest) {
        val json = serializer.toJson(requestEntity)
        httpHelpler.post("order/changeOrderStatus", json, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                log("Failed to send request with route point.")
            }
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    log("Add route point: orderId=${requestEntity.orderId} SerialNumber=${requestEntity.serialNumber}")
                } else {
                    log("Failed to add route point: orderId=${requestEntity.orderId} SerialNumber=${requestEntity.serialNumber}")
                }
            }
        })
    }

    fun getNewOrders() : List<OrderInfoResponse> {
        var orders: List<OrderInfoResponse> = emptyList()
        try {
            val response = httpHelpler.syncGet("order/getNewOrders")
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    orders = serializer.toEntity(body.string())
                }
            } else {
                log("Can't get new orders")
            }
        } catch (e: Exception) {
            log("GetNewOrders error:${e.message}")
        }
        return orders
    }
}

object OrderScheduler {
    private lateinit var scheduledService: ScheduledExecutorService
    private val httpHelpler = HttpHelper
    private val serialNum = AtomicInteger(0)
    val isRunning = AtomicBoolean(false)
    val currentOrderId = AtomicInteger()

    init {
        initScheduler()
    }

    private fun initScheduler() {
        scheduledService = Executors.newSingleThreadScheduledExecutor()
    }

    fun runScheduling(orderId: Int) {
        currentOrderId.set(orderId)
        var orderEntity = RoutePointRequest()
        orderEntity.orderId = orderId

        // TODO: set real lat and long
        orderEntity.geoPoint = RoutePointRequest.GeoPoint().apply {
            latitude = 10.0F
            longitude = 20.0F
        }

        val task = SendCoordsTask(orderEntity)
        scheduledService.scheduleWithFixedDelay(task, 2, 2, TimeUnit.SECONDS)
        isRunning.set(true)
    }

    fun stopScheduling() {
        log("Try to stop scheduling...")
        scheduledService.shutdown()
        try {
            if (!scheduledService.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduledService.shutdownNow()
                if (!scheduledService.awaitTermination(5, TimeUnit.SECONDS)) {
                    log("Failed to stop scheduling")
                }
            }
        } catch (e: InterruptedException) {
            scheduledService.shutdownNow()
            Thread.currentThread().interrupt()
        } catch (e: Exception) {
            log("")
        } finally {
            serialNum.set(0)
            isRunning.set(false)
        }
    }

    class SendCoordsTask(private var requestEntity: RoutePointRequest) : Runnable {
        override fun run() {
            requestEntity.serialNumber = serialNum.incrementAndGet()
            OrderController.addRoutePoint(requestEntity)
        }
    }
}

fun log(message: String) {
    Log.i("OrderController", message)
}
