package ru.delivery.system.android

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.databinding.ObservableArrayList
import android.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.activity_order.*
import ru.delivery.system.android.controllers.OrderController
import ru.delivery.system.android.databinding.ActivityOrderBinding
import ru.delivery.system.android.models.OrderData


class OrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val orders = ObservableArrayList<OrderData>()
        val binding: ActivityOrderBinding = DataBindingUtil.setContentView(this, R.layout.activity_order)
        orders.addAll(OrderController.getNewOrders().map { order -> OrderData(order.orderId!!, order.departurePoint!!) })
        binding.setVariable(BR.orders, orders)
        
        addOrderButton.setOnClickListener {
            orders.add(OrderData(2, "ada3"))
        }

//        setContentView(R.layout.activity_order)
    }
}
