package ru.delivery.system.controllers

import okhttp3.Response
import ru.delivery.system.common.JsonSerializer
import ru.delivery.system.models.json.OrderCreateRequest
import ru.delivery.system.models.json.OrderCreateResponse
import ru.delivery.system.rest.HttpHelper
import ru.delivery.system.views.child_screens.ProductListItem
import tornadofx.Controller
import java.lang.Exception
import java.time.LocalDate
import java.util.*
import java.time.ZoneId
import java.time.temporal.TemporalQueries.localDate



class OrderController : Controller() {

    private val httpHelper =  HttpHelper
    private val jsonSerializer = JsonSerializer()

    /**
     * Отправляет запрос на создание заказа
     */
    fun createOrder(
//        deaprute: String,
//        destination: String,
//        deliveryDate_: LocalDate,
//        isOrderCommon_: Boolean,
//        deliveryUrgency_: String?,
//        productList_: List<ProductListItem>
        orderInfo: OrderCreateRequest
    ) : Int? {
//        val orderInfo = OrderCreateRequest().apply {
//            createDate = Date()
//            departurePoint = deaprute
//            destinationPoint = destination
//            isOrderCommon = isOrderCommon_
//            val date = Date.from(deliveryDate_.atStartOfDay(ZoneId.systemDefault()).toInstant())
//            deliveryDate = date
//            deliveryUrgency = deliveryUrgency_
//            productList = productList_.map { product ->
//                OrderCreateRequest.Product().apply {
//                    productId = product.id
//                    count = product.count
//                }
//            }
//        }

        var orderId: Int? = null
        try {
            val response = httpHelper.syncPost("order/addOrder", jsonSerializer.toJson(orderInfo))
            response.use { resp ->
                if (resp.isSuccessful) {
                    resp.body()?.let { body ->
                        val respEntity = jsonSerializer.toEntity<OrderCreateResponse>(body.string())
                        orderId = respEntity.orderId
                    }
                }
            }
        } catch (e: Exception) {
            println("CreateOrder error: ${e.message}")
        }
        return orderId
    }

}