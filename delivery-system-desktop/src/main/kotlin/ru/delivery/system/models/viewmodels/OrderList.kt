package ru.delivery.system.models.viewmodels

import javafx.beans.property.SimpleListProperty
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

class OrderList {
    val orderListProperty = SimpleListProperty<Int>()
    var orderList by orderListProperty
}

class OrderListModel : ItemViewModel<OrderList>() {
    val orderList = bind(OrderList::orderListProperty)
}