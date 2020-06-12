package ru.delivery.system.models.viewmodels

import com.fasterxml.jackson.annotation.JsonProperty
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
class Driver {
    val driverIdProperty = SimpleIntegerProperty()
    var driverId by driverIdProperty


    val usernameProperty = SimpleStringProperty()
    var username by usernameProperty


    val nameProperty = SimpleStringProperty()
    var name by nameProperty


    val surnameProperty = SimpleStringProperty()
    var surname by surnameProperty


    val roleProperty = SimpleStringProperty()
    var role by roleProperty

}

class DriverModel : ItemViewModel<Driver>() {
    val driverId = bind(Driver::driverIdProperty)
    val username = bind(Driver::usernameProperty)
    val name = bind(Driver::nameProperty)
    val surname = bind(Driver::surnameProperty)
    val role = bind(Driver::roleProperty)
}

