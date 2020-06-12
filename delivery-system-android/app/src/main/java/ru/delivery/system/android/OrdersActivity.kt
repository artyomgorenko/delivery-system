package ru.delivery.system.android

import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_orders.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import ru.delivery.system.android.databinding.ActivityOrdersBinding
import ru.delivery.system.android.models.OrderData
import ru.delivery.system.android.models.json.OrderInfoResponse
import ru.delivery.system.android.utils.HttpHelper
import ru.delivery.system.android.utils.JsonSerializer
import ru.delivery.system.android.utils.toast
import java.io.IOException

/**
 * Список новых заказов на доставку
 */
class OrdersActivity : AppCompatActivity() {
    private val TAG = "OrderActivity"
    private val httpHelper = HttpHelper
    private val jsonSerializer = JsonSerializer()
    private val orders = ObservableArrayList<OrderData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateOrdersList()
        val binding: ActivityOrdersBinding = DataBindingUtil.setContentView(this, R.layout.activity_orders)
        binding.setVariable(BR.orders, orders)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        updateOrdersList.setOnClickListener {
            updateOrdersList()
        }
    }

    override fun onResume() {
        updateOrdersList()
        super.onResume()
    }

    private fun updateOrdersList() {
        val context = this
        try {
            httpHelper.get("order/getNewOrders", object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread { toast(context, "Error during new orders list update:${e.message}") }
                    Log.e(TAG, "Error during new orders list update:", e)
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        if  (response.isSuccessful) {
                            response.body()?.let{ body ->
                                val responseEntity = jsonSerializer.toEntity<List<OrderInfoResponse>>(body.string())
                                runOnUiThread {
                                    orders.clear()
                                    orders.addAll(responseEntity.map { OrderData(
                                        it.orderId!!,
                                        it.status!!,
                                        it.departurePoint!!,
                                        it.destinationPoint!!
                                    //TODO: add order products
                                    ) })
                                    toast(context, "New orders list update. Size ${responseEntity.size}")
                                }
                                Log.i(TAG, "New orders list update. Size ${responseEntity.size}")
                            }
                        } else {
                            runOnUiThread { toast(context, "Failed to update new orders list. Bad response.") }
                            Log.i(TAG, "Failed to update new orders list. Bad response.")
                        }
                    } catch (e: IOException) {
                        runOnUiThread { toast(context, "Error during new orders list update:${e.message}") }
                        Log.e(TAG, "Error during new orders list update:", e)
                    }
                }

            })
        } catch (e: Exception) {
            runOnUiThread { toast(context, "Failed to update new orders list. Bad response.") }
            Log.e(TAG, "Error during new orders list update:", e)
        }
    }
}
