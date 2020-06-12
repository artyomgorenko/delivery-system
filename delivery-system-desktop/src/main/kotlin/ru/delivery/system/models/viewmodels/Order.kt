package ru.delivery.system.models.viewmodels

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.paint.Color
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue
import javax.sound.midi.Track

/**
 * Модель для хранения информации о заказе
 */
class Order {
    val orderIdProperty = SimpleIntegerProperty()
    var orderId by orderIdProperty

    val productIdProperty = SimpleIntegerProperty()
    var productId by productIdProperty

    val statusProperty = SimpleStringProperty()
    var status by statusProperty

    val completedProperty = SimpleBooleanProperty()
    var completed by completedProperty

    val stateColorProperty = SimpleObjectProperty<StateColor>()
    var stateColor by stateColorProperty

    val trackProperty = SimpleObjectProperty<Track>()
    var track by trackProperty
}

class OrderModel : ItemViewModel<Order>() {
    val order = bind(Order::orderIdProperty)
    val productId = bind(Order::productIdProperty)
    val status = bind(Order::statusProperty)
    val completed = bind(Order::completedProperty)
    val stateColor = bind(Order::stateColorProperty)
}

enum class FilterState { All, InProgress, Completed }
enum class StateColor(val color: Color) {
    InProgress(Color.GREEN),
    Completed(Color.DARKGRAY),
    Unknown(Color.YELLOW)
}