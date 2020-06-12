package ru.delivery.system.models.viewmodels

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue
import java.util.*

class Movement {
    val movementIdProperty = SimpleIntegerProperty()
    var movementId by movementIdProperty


    val newStatusProperty = SimpleStringProperty()
    var newStatus by newStatusProperty


    val prevStatusProperty = SimpleStringProperty()
    var prevStatus by prevStatusProperty


    val dateProperty = SimpleObjectProperty<Date>()
    var date by dateProperty

}

class MovementModel : ItemViewModel<Movement>() {
    val movementId = bind(Movement::movementIdProperty)
    val newStatus = bind(Movement::newStatusProperty)
    val prevStatus = bind(Movement::prevStatusProperty)
    val date = bind(Movement::dateProperty)
}