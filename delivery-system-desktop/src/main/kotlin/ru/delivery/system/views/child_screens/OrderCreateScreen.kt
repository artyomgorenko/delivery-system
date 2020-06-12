package ru.delivery.system.views.child_screens

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Pos
import ru.delivery.system.controllers.OrderController
import ru.delivery.system.controllers.PorductListController
import ru.delivery.system.models.json.ProductListEntity
import tornadofx.*
import java.time.LocalDate

/**
 * Modal window for order creation
 */
class OrderCreateScreen : Fragment("OrderControl") {

    var selectedProducts: ObservableList<ProductListItem> = FXCollections.observableArrayList<ProductListItem>()

    private val orderController: OrderController by inject()

    private val model = ViewModel()
    private val departure = model.bind { SimpleStringProperty() }
    private val destination = model.bind { SimpleStringProperty() }
    private val delivetyDate = model.bind { SimpleObjectProperty<LocalDate>() }

    val totalCost = doubleBinding(selectedProducts) {
        selectedProducts.sumByDouble { it.cost * it.count }
    }

    /*For combobox*/
    var productList = PorductListController.productList
    var productListItem = SimpleObjectProperty<ProductListEntity.Product>(productList.first())


    override val root = borderpane {
        prefHeight = 400.0
        center {
            label("Создание заказа")

            form {
                fieldset {
                    field("Адрес отправления") {
                        textfield(departure) {
                            required(ValidationTrigger.OnChange(), "Адрес отправления - обязательное поле")
                            text = "г. Краснодар"
                        }
                    }
                    field("Адрес доставки") {
                        textfield(destination) {
                            required(ValidationTrigger.OnChange(), "Адрес доставки - обязательное поле")
                            text = "г. Краснодар"
                        }
                    }
                    field("Дата доставки") {
                        datepicker(delivetyDate) {
                            value = LocalDate.now()
                        }
                    }
                    field("Список товаров") {
                    }
                    scrollpane {
                        vbox {
                            children.bind(selectedProducts) { product ->
                                hbox {
                                    label("${product.name}  ${product.count} шт.  ${product.count * product.cost} руб.")

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
                    label("Итого: 0 руб.") {
                        totalCost.onChange { text = "Итого: $it руб." }
                    }

                    field("Список товаров") {
                        combobox(productListItem, productList) {
                            cellFormat { text = it.name }
                        }
                        button("Добавить").action { addOrUpdate(ProductListItem(
                            productListItem.value.productId!!,
                            productListItem.value.name!!,
                            productListItem.value.cost!!,
                            1)) }
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
                            delivetyDate.value,
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

class ProductListItem(var id:Int, var name: String, var cost: Double, var count: Int)