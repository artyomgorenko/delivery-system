package ru.delivery.system.android.handlers

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.view.View
import ru.delivery.system.android.OrdersActivity
import ru.delivery.system.android.models.OrderData
import ru.delivery.system.android.models.OrderModel

class ButtonHandler {

    fun showOrderDetails(view: View, orderData: OrderData) {
        val context = view.context
        val randomIntent = Intent(context, OrdersActivity::class.java)
        OrderModel.setOrderData(orderData)
//        randomIntent.putExtra(OrdersActivity.orderId, .id.toString())
        startActivity(context, randomIntent, Bundle())
    }
}