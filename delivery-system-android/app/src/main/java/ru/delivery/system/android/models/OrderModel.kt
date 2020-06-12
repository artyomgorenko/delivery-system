package ru.delivery.system.android.models

object OrderModel {
    private var orderData: OrderData = OrderData(999, "####")

    @Synchronized
    fun setOrderData(orderData: OrderData) {
        this.orderData = orderData
    }

    fun getOrderData() : OrderData {
        return orderData
    }
}