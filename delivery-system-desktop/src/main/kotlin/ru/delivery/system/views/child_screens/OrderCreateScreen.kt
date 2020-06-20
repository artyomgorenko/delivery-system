package ru.delivery.system.views.child_screens

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.paint.Color
import ru.delivery.system.controllers.OrderController
import ru.delivery.system.controllers.ProductListController
import ru.delivery.system.models.json.ProductListEntity
import tornadofx.*
import java.time.LocalDate

/**
 * Modal window for order creation
 */
class OrderCreateScreen : Fragment("Новый заказ") {

    var selectedProducts: ObservableList<ProductListItem> = FXCollections.observableArrayList<ProductListItem>()

    private val orderController: OrderController by inject()

    private val model = ViewModel()
    private val departure = model.bind { SimpleStringProperty() }
    private val destination = model.bind { SimpleStringProperty() }
    private val deliveryDate = model.bind { SimpleObjectProperty<LocalDate>() }

    private val totalCost = doubleBinding(selectedProducts) {
        selectedProducts.sumByDouble { it.cost * it.count }
    }
    private val totalWeight = doubleBinding(selectedProducts) {
        selectedProducts.sumByDouble { it.weight * it.count }
    }
    private val minSquare = doubleBinding(selectedProducts) {
        selectedProducts.sumByDouble { it.width * it.length * it.count }
    }

    /*For combobox*/
    var productList = ProductListController.productList
    var productListItem = SimpleObjectProperty<ProductListEntity.Product>(productList.firstOrNull())


    override val root = borderpane {
        prefHeight = 400.0
        center {
            label("Создание заказа")

            form {
                style {
                    backgroundColor += Color.WHITE
                }

                fieldset {
                    field("Адрес отправления") {
                        textfield(departure) {
                            required(ValidationTrigger.OnChange(), "Адрес отправления - обязательное поле")
                            text = "г. Краснодар ул. Тюляева 2"
                        }
                    }
                    field("Адрес доставки") {
                        textfield(destination) {
                            required(ValidationTrigger.OnChange(), "Адрес доставки - обязательное поле")
                            text = "г. Краснодар ул. Демуса 43"
                        }
                    }
                    field("Дата доставки") {
                        datepicker(deliveryDate) {
                            value = LocalDate.now()
                        }
                    }
                    field("Время доставки") {
                        textfield("12:00")
                        textfield("16:00")

                    }
                    field("Список товаров") {
                    }
                    scrollpane {
                        tooltip("Информация о товарах, доавленных в список")
                        minHeightProperty().set(100.0)
                        vbox {
                            children.bind(selectedProducts) { product ->
                                hbox {
                                    spacingProperty().set(50.0)
                                    label("${product.name}  ${product.count} шт.  ${product.count * product.cost} руб.")
                                    hbox {
                                        spacingProperty().set(5.0)
                                        button("+") {
                                            action { incCount(product) }
                                            style { alignment = Pos.BASELINE_RIGHT }
                                        }
                                        button("-") {
                                            action { decCount(product) }
                                            style { alignment = Pos.BASELINE_RIGHT }
                                        }
                                    }
                                }
                            }
                        }
                    }


                    field("Список товаров") {
                        combobox(productListItem, productList) {
                            cellFormat { text = it.name }
                        }
                        button("Добавить").action {
                            addOrUpdate(
                                ProductListItem(
                                    productListItem.value.productId!!,
                                    productListItem.value.name!!,
                                    productListItem.value.cost!!,
                                    productListItem.value.height!!,
                                    productListItem.value.width!!,
                                    productListItem.value.length!!,
                                    productListItem.value.weight!!,
                                    1
                                ))
                        }
                    }


                    field("Тип доставки") {
                        val types = listOf("Внутреннее перемещение", "Обычная доставка")
                        val item = SimpleStringProperty(types.firstOrNull())
                        combobox(item, types)
                    }

                    field("Срочность доставки") {
                        val deliveryTypes = listOf("Срочная доставка", "Экспресс доставка")
                        val deliveryTypeItem = SimpleStringProperty(deliveryTypes.firstOrNull())
                        combobox(deliveryTypeItem, deliveryTypes)
                    }
                    field("Вес груза") {
                        label("543 кг.") {
                            //TODO: uncomment It
                            //totalWeight.onChange { text = "$it кг." }
                        }
                    }
                    field("Объём груза") {
                        label("6.72 м3.") {
                            //TODO: uncomment It
//                            minSquare.onChange { text = "$it м3." }
                        }
                    }
                    field("Итого") {
                        label("912 руб.") {
                            //TODO: uncomment It
                            // totalCost.onChange { text = "$it руб." }
                        }
                    }
                }
            }
        }

        bottom {
            hbox {
                paddingBottom = 20
                alignment = Pos.CENTER
                spacing = 20.0
                button("Создать") {
                    enableWhen(model.valid)
                    action {
                        val orderId = orderController.createOrder(
                            departure.value,
                            destination.value,
                            deliveryDate.value,
                            selectedProducts.toList())
                        when (orderId) {
                            null -> println("Failed to create order.")
                            else -> println("Created new order with id=$orderId")
                        }
                        close()
                    }
                }
                button("Отмена").action { close() }
            }
        }
    }

    private fun decCount(product: ProductListItem) {
        val target = selectedProducts.find { it.name == product.name }
        if (target != null) {
            if (target.count > 0) {
                target.count--
                updateList(target)
            } else {
                selectedProducts.remove(target)
            }
        }
    }

    private fun incCount(product: ProductListItem) {
        val target = selectedProducts.find { it.name == product.name }
        if (target != null) {
            target.count++
            updateList(target)
        }
    }

    private fun addOrUpdate(product: ProductListItem) {
        val target = selectedProducts.find { it.name == product.name }
        if (target == null) {
            selectedProducts.add(product)
        } else {
            incCount(product)
        }
    }

    private fun updateList(product: ProductListItem) {
        selectedProducts.remove(product)
        selectedProducts.add(product)
        selectedProducts.sortBy { it.name }
    }

}

class ProductListItem(
    var id:Int,
    var name: String,
    var cost: Double,
    var height: Double,
    var width: Double,
    var length: Double,
    var weight: Double,
    var count: Int
)