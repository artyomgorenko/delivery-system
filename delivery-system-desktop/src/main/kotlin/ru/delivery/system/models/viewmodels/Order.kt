package ru.delivery.system.models.viewmodels

import javafx.beans.property.*
import javafx.scene.paint.Color
import ru.delivery.system.models.Track
import ru.delivery.system.models.json.OrderInfo
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

/**
 * Модель для хранения информации о заказе
 */
class Order {
    val orderIdProperty = SimpleIntegerProperty()
    var orderId by orderIdProperty

    val productListProperty = SimpleListProperty<OrderInfo.Product>()
    var productList by productListProperty

//    val productIdProperty = SimpleIntegerProperty()
//    var productId by productIdProperty

    val statusProperty = SimpleStringProperty()
    var status by statusProperty

    val departurePointProperty = SimpleStringProperty()
    var departurePoint by departurePointProperty

    val departureLongitudeProperty = SimpleDoubleProperty()
    var departureLongitude by departureLongitudeProperty

    val departureLatitudeProperty = SimpleDoubleProperty()
    var departureLatitude by departureLatitudeProperty

    val destinationPointProperty = SimpleStringProperty()
    var destinationPoint by destinationPointProperty

    val destinationLongitudeProperty = SimpleDoubleProperty()
    var destinationLongitude by destinationLongitudeProperty

    val destinationLatitudeProperty = SimpleDoubleProperty()
    var destinationLatitude by destinationLatitudeProperty

    val deliveryTimeProperty = SimpleStringProperty()
    var deliveryTime by deliveryTimeProperty

    val completedProperty = SimpleBooleanProperty()
    var completed by completedProperty

    val stateColorProperty = SimpleObjectProperty<StateColor>()
    var stateColor by stateColorProperty

    val trackProperty = SimpleObjectProperty<Track>()
    var track by trackProperty
}

class OrderModel(val order: Order) : ItemViewModel<Order>(order) {
    val orderId = bind(Order::orderIdProperty)
    val productList = bind(Order::productListProperty)
//    val productId = bind(Order::productIdProperty)
    val status = bind(Order::statusProperty)
    val departurePoint = bind(Order::departurePointProperty)
    val departureLongitude = bind(Order::departureLongitudeProperty)
    val departureLatitude = bind(Order::departureLatitudeProperty)
    val destinationPoint = bind(Order::destinationPointProperty)
    val destinationLongitude = bind(Order::destinationLongitudeProperty)
    val destinationLatitude = bind(Order::destinationLatitudeProperty)
    val deliveryTime = bind(Order::deliveryTimeProperty)
    val completed = bind(Order::completedProperty)
    val stateColor = bind(Order::stateColorProperty)
    val track = bind(Order::trackProperty)
}

enum class FilterState { All, InProgress, Completed }
enum class StateColor(val color: Color) {
    InProgress(Color.GREEN),
    Completed(Color.DARKGRAY),
    Unknown(Color.YELLOW)
}