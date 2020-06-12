package ru.delivery.system.models.viewmodels

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

/**
 * Модель для хранения информации о заказе
 */
class Order {
    val numberProperty = SimpleIntegerProperty()
    var number by numberProperty

    val productIdProperty = SimpleIntegerProperty()
    var productId by productIdProperty

    val statusProperty = SimpleStringProperty()
    var status by statusProperty

    val completedProperty = SimpleBooleanProperty()
    var completed by completedProperty
}

class OrderModel : ItemViewModel<Order>() {
    val number = bind(Order::numberProperty)
    val productId = bind(Order::productIdProperty)
    val status = bind(Order::statusProperty)
    val completed = bind(Order::completedProperty)
}

enum class FilterState { All, InProgress, Completed }