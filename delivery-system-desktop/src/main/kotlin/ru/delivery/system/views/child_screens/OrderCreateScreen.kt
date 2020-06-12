package ru.delivery.system.views.child_screens

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import javafx.scene.paint.Color
import ru.delivery.system.common.getCoordsFromAddress
import ru.delivery.system.controllers.OrderController
import ru.delivery.system.controllers.ProductListController
import ru.delivery.system.models.json.OrderCreateRequest
import ru.delivery.system.models.json.ProductListEntity
import tornadofx.*
import java.time.LocalDate
import java.time.ZoneId
import java.util.*


/**
 * Modal window for order creation
 */
class OrderCreateScreen : Fragment("Новый заказ") {

    var selectedProducts: ObservableList<ProductListItem> = FXCollections.observableArrayList<ProductListItem>()

    private val orderController: OrderController by inject()

    private val model = ViewModel()
    private val departure = model.bind { SimpleStringProperty() }
    private val destination = model.bind { SimpleStringProperty() }
    private val deliveryDateField = model.bind { SimpleObjectProperty<LocalDate>() }
    private lateinit var comboboxOrderType: ComboBox<String>
    private lateinit var comboboxDeliveryUrgency: ComboBox<String>
    private lateinit var departureAddressField: TextField
    private lateinit var destinationAddressField: TextField

    private val totalCost = doubleBinding(selectedProducts) {
        selectedProducts.sumByDouble { it.cost * it.count }
    }
    private val totalWeight = doubleBinding(selectedProducts) {
        selectedProducts.sumByDouble { it.weight * it.count }
    }
    private val totalVolume = doubleBinding(selectedProducts) {
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
                        departureAddressField = textfield(departure) {
                            required(ValidationTrigger.OnChange(), "Адрес отправления - обязательное поле")
                            text = "Краснодар Тюляева 2"
                        }
                    }
                    field("Адрес доставки") {
                        destinationAddressField = textfield(destination) {
                            required(ValidationTrigger.OnChange(), "Адрес доставки - обязательное поле")
                            text = "Краснодар Демуса 43"
                        }
                    }
                    field("Дата доставки") {
                        datepicker(deliveryDateField) {
                            value = LocalDate.now()
                        }
                    }
                    field("Время доставки") {
                        textfield("12:00")
                        textfield("16:00")
                    }

                    field("Список товаров")
                    scrollpane {
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
                        comboboxOrderType = combobox(item, types)
                    }
                    field("Срочность доставки") {
                        val deliveryTypes = listOf("Срочная доставка", "Экспресс доставка")
                        val deliveryTypeItem = SimpleStringProperty(deliveryTypes.firstOrNull())
                        comboboxDeliveryUrgency = combobox(deliveryTypeItem, deliveryTypes)
                    }
                    field("Вес груза") {
                        label(totalWeight) {
                            totalWeight.onChange { text = "$it кг." }
                        }
                    }
                    field("Объём груза") {
                        label(totalVolume) {
                            totalVolume.onChange { text = "$it м3." }
                        }
                    }
                    field("Итого") {
                        label(totalCost) {
                             totalCost.onChange { text = "$it руб." }
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
//
//                        val orderId = orderController.createOrder(
//                            departure.value,
//                            destination.value,
//                            deliveryDateField.value,
//                            isOrderCommon,
//                            deliveryUrgency_,
//                            selectedProducts.toList())
//                        when (orderId) {
//                            null -> println("Failed to create order.")
//                            else -> println("Created new order with id=$orderId")
//                        }
                        val orderId = orderController.createOrder(OrderCreateRequest().apply {
                            createDate = Date()
                            departurePoint = departure.value
                            destinationPoint = destination.value
                            isOrderCommon = comboboxOrderType.selectedItem == "Обычная доставка"
                            val date = Date.from(
                                deliveryDateField.value
                                    .atStartOfDay(ZoneId.systemDefault()).toInstant()
                            )
                            deliveryDate = date
                            deliveryUrgency = comboboxDeliveryUrgency.selectedItem

                            baseCost = 100F // TODO: implement calculation by formula

                            /**
                             * Пункт отправления
                             */
                            val departureAddressParts = departure.value.split(" ")
                            val departureLatLong = getCoordsFromAddress(departureAddressParts[0], departureAddressParts[1], departureAddressParts[2])!!
                            departurePoint = destination.value
                            departureLongitude = departureLatLong.longitude.toFloat()
                            departureLatitude = departureLatLong.latitude.toFloat()

                            /**
                             * Пункт назначения
                             */
                            val destinationAddressParts = destination.value.split(" ")
                            val destinationLatLong = getCoordsFromAddress(destinationAddressParts[0], destinationAddressParts[1], destinationAddressParts[2])!!
                            destinationPoint = destination.value
                            destinationLongitude = destinationLatLong.longitude.toFloat()
                            destinationLatitude = destinationLatLong.latitude.toFloat()

                            /**
                             * Список товаров
                             */
                            productList = selectedProducts.toList().map { product ->
                                OrderCreateRequest.Product().apply {
                                    productId = product.id
                                    count = product.count
                                }
                            }
                        })
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