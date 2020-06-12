package ru.delivery.system.models.viewmodels

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.ItemViewModel
import java.util.*
import tornadofx.getValue
import tornadofx.setValue

class Cargo {

    val cargoIdProperty = SimpleIntegerProperty()
    var cargoId by cargoIdProperty


    val productIdProperty = SimpleIntegerProperty()
    var productId by productIdProperty


    val warehouseIdProperty = SimpleIntegerProperty()
    var warehouseId by warehouseIdProperty


//    val orderIdListProperty = SimpleObjectProperty<List>()
//    var orderIdList by orderIdListProperty


    val statusProperty = SimpleStringProperty()
    var status by statusProperty

}

class CargoModel : ItemViewModel<Cargo>() {
    val cargoId = bind(Cargo::cargoIdProperty)
    val productId = bind(Cargo::productIdProperty)
    val warehouseId = bind(Cargo::warehouseIdProperty)
//    val orderIdList = bind(Cargo::orderIdListProperty)
    val status = bind(Cargo::statusProperty)
}



