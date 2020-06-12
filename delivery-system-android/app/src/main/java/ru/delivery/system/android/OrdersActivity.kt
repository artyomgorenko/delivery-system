package ru.delivery.system.android

import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_orders.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import ru.delivery.system.android.controllers.OrderController
import ru.delivery.system.android.controllers.OrderScheduler
import ru.delivery.system.android.databinding.ActivityOrdersBinding
import ru.delivery.system.android.models.OrderData
import ru.delivery.system.android.models.OrderModel
import ru.delivery.system.android.models.json.OrderInfoResponse
import ru.delivery.system.android.models.json.OrderStatusRequest
import ru.delivery.system.android.models.json.OrderStatusResponse
import ru.delivery.system.android.utils.HttpHelper
import ru.delivery.system.android.utils.JsonSerializer
import ru.delivery.system.android.utils.snackbar
import ru.delivery.system.android.utils.toast
import java.io.IOException

class OrdersActivity : AppCompatActivity() {

    companion object {
        var orderId = ""
    }

    private val orderController = OrderController
    private val httpHelpler = HttpHelper
    private val orderModel = OrderModel.getOrderData()

    lateinit var binding: ActivityOrdersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_orders)
        binding.setVariable(BR.order, orderModel)
        binding.executePendingBindings()

        getInWorkButton.setOnClickListener { view ->
            snackbar(view, "getInWorkButton")
            getInWorkAction()
        }
        sendRequestButton.setOnClickListener {
            getOrderList()
        }
    }

    private fun getInWorkAction() {
        getOrderInWork(orderModel.id)
    }

    private fun getOrderList() {
        val context = this
//        val url = "http://10.0.2.2:8080/delivery-system/order/getOrderInfo?orderIdList=2&orderIdList=3" //Emulator
        httpHelpler.get("order/getOrderInfo?orderIdList=${OrderModel.getOrderData().id}",
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        Toast.makeText(context, "Request failed", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onResponse(call: Call, response: okhttp3.Response) {
                    runOnUiThread { Toast.makeText(context, "Request succeed", Toast.LENGTH_SHORT).show() }

                    response.body()?.let { body ->
                        val rawBody = body.string() // @Hint: buffer will be close after first usage
                        runOnUiThread { Toast.makeText(context, rawBody, Toast.LENGTH_SHORT).show() }
                        val orderInfoList = JsonSerializer().toEntity<List<OrderInfoResponse>>(rawBody)
                        val orderInfo = orderInfoList[0]
                        orderModel.id = orderInfo.orderId!!
                    }

                    runOnUiThread { binding.invalidateAll() } // TODO: Works too long on first usage
                }
            })
    }

    fun getOrderInWork(orderId: Int) {
        val context = this
        changeOrderStatus(orderId, "IN_PROGRESS", object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Toast.makeText(context, "Request failed", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        val orderStatusResponse = JsonSerializer().toEntity<OrderStatusResponse>(body.string())
                        if (orderStatusResponse.header.resultCode == 0) {
                            if (!OrderScheduler.isRunning.get()) {
                                OrderScheduler.runScheduling(orderStatusResponse.body.orderId!!)
                                runOnUiThread {
                                    toast(context, "Заказ может быть взят в работу")
                                }
                                val intent = Intent(context, MapActivity::class.java)
                                startActivity(intent)
                            } else {
                                toast(context, "Заказ c id=${OrderScheduler.currentOrderId} еще не завершен")
                            }
                        } else {
                            runOnUiThread {
                                Toast.makeText(context, "Заказ не может быть взят в работу:\n" + "${orderStatusResponse.header.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        })
    }

    fun finishOrder(orderId: Int) {
        return changeOrderStatus(orderId, "DONE", object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
            }
        })
    }

    fun cancelOrder(orderId: Int) {
        return changeOrderStatus(orderId, "CANCEL", object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
            }
        })
    }

    private fun changeOrderStatus(orderId: Int, newStatus: String, callback: Callback) {
        val requestEntity = OrderStatusRequest().apply {
            body.orderId = orderId
            body.newStatus = newStatus
            body.driverId = 1
            body.transportId = 1
        }
        val json = JsonSerializer().toJson(requestEntity)
        HttpHelper.post("order/changeOrderStatus", json, callback)
    }
}
