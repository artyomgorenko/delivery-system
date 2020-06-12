package ru.delivery.system.models.viewmodels

import com.fasterxml.jackson.annotation.JsonProperty
import javafx.beans.property.*
import javafx.scene.paint.Color
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue
import javax.sound.midi.Track

/**
 * Модель для хранения информации о заказе
 */
class Product {
    val productIdProperty = SimpleIntegerProperty()
    var productId by productIdProperty

    val nameProperty = SimpleStringProperty()
    var name by nameProperty

    val costProperty = SimpleDoubleProperty()
    var cost by costProperty

    val categoryProperty = SimpleStringProperty()
    var category by categoryProperty

    val weightProperty = SimpleDoubleProperty()
    var weight by weightProperty

    val heightProperty = SimpleDoubleProperty()
    var height by heightProperty

    val widthProperty = SimpleDoubleProperty()
    var width by widthProperty

    val lengthProperty = SimpleDoubleProperty()
    var length by lengthProperty

}

class ProductModel(val product: Product) : ItemViewModel<Product>() {
    val productId = bind(Product::productIdProperty)
    val name = bind(Product::nameProperty)
    val cost = bind(Product::costProperty)
    val category = bind(Product::categoryProperty)
    val weight = bind(Product::weightProperty)
    val height = bind(Product::heightProperty)
    val width = bind(Product::widthProperty)
    val length = bind(Product::lengthProperty)
}