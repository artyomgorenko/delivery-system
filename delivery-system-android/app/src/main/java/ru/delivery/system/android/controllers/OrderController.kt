package ru.delivery.system.android.controllers

import android.location.Location
import android.preference.PreferenceManager
import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import ru.delivery.system.android.MainActivity
import ru.delivery.system.android.MapActivity
import ru.delivery.system.android.models.UserModel
import ru.delivery.system.android.models.json.OrderInfoResponse
import ru.delivery.system.android.models.json.RoutePointRequest
import ru.delivery.system.android.utils.HttpHelper
import ru.delivery.system.android.utils.JsonSerializer
import ru.delivery.system.android.utils.getLastLocation
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
        httpHelpler.post("order/addRoutePoint", json, object : Callback {
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

/**
 * Отправляет информацию о текущем местоположении курьера через заданные промежутки времени.
 * Если есть заказ в работе, то дополнительно отправляет orderId и serialNum
 */
object OrderScheduler {
    private lateinit var scheduledService: ScheduledExecutorService
    private val userModel = UserModel
    private val serialNum = AtomicInteger(0)
    val isRunning = AtomicBoolean(false)
    @Volatile var currentOrderId: Int? = null

    init {
        initScheduler()
    }

    private fun initScheduler() {
        if (!::scheduledService.isInitialized) {
            scheduledService = Executors.newSingleThreadScheduledExecutor()
        }
    }

    private fun getSchedulingDelay() : Long {
        val prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.context)
        return prefs.getString("sync_frequency_s", "10")!!.toLong()
    }

    fun runScheduling() {
        val task = SendCoordsTask()
        val delay = getSchedulingDelay()
        scheduledService.scheduleWithFixedDelay(task, delay, delay, TimeUnit.SECONDS)
        isRunning.set(true)
    }

    fun startOrderTracking(orderId: Int) {
        if (!isRunning.get()) {
            runScheduling()
        }
        currentOrderId = orderId
        serialNum.set(0)
    }

    fun stopOrderTracking() {
        currentOrderId = null
        serialNum.set(0)
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

    /**
     * Таск для переодической отправки координат.
     * OrderId отправляется в зависимости от того, есть  ли заказ в работе,
     */
    class SendCoordsTask : Runnable {
        override fun run() {
            val serialNum = serialNum.incrementAndGet()
            val location: Location? = getLastLocation(MainActivity.context, MapActivity.mGoogleApiClient)
            location?.let { it ->
                isRunning.set(true)
                val requestEntity = RoutePointRequest().apply {
                    driverId = userModel.userId
                    currentOrderId?.let {
                        orderId = currentOrderId
                        serialNumber = serialNum
                    }
                    geoPoint = RoutePointRequest.GeoPoint().apply {
                        latitude = location.latitude.toFloat()
                        longitude = location.longitude.toFloat()
                    }
                }

                OrderController.addRoutePoint(requestEntity)
            }
        }
    }
}

fun log(message: String) {
    Log.i("OrderController", message)
}
